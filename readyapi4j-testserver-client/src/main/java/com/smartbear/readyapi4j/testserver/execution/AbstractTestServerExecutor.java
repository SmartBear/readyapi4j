package com.smartbear.readyapi4j.testserver.execution;

import com.smartbear.readyapi.client.model.ProjectResultReport;
import com.smartbear.readyapi.client.model.RequestTestStepBase;
import com.smartbear.readyapi.client.model.TestCase;
import com.smartbear.readyapi.client.model.TestStep;
import com.smartbear.readyapi.client.model.UnresolvedFile;
import com.smartbear.readyapi4j.ExecutionListener;
import com.smartbear.readyapi4j.execution.DataExtractors;
import com.smartbear.readyapi4j.extractor.ExtractorData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

abstract class AbstractTestServerExecutor {
    private static Logger logger = LoggerFactory.getLogger(AbstractTestServerExecutor.class);
    private static final int NUMBER_OF_RETRIES_IN_CASE_OF_ERRORS = 3;
    private final List<ExecutionListener> executionListeners = new CopyOnWriteArrayList<>();
    protected List<ExtractorData> extractorDataList = new LinkedList<>();

    final TestServerClient testServerClient;

    AbstractTestServerExecutor(TestServerClient testServerClient) {
        this.testServerClient = testServerClient;
    }

    public void addExecutionListener(ExecutionListener listener) {
        executionListeners.add(listener);
    }

    public void removeExecutionListener(ExecutionListener listener) {
        executionListeners.remove(listener);
    }

    void notifyExecutionStarted(TestServerExecution execution) {
        if (execution != null) {
            for (ExecutionListener executionListener : executionListeners) {
                executionListener.executionStarted(execution.getCurrentReport());
            }
            new ExecutionStatusChecker(execution).start();
        }
    }

    void notifyErrorOccurred(Exception e) {
        for (ExecutionListener executionListener : executionListeners) {
            executionListener.errorOccurred(e);
        }
    }

    void notifyExecutionFinished(ProjectResultReport executionReport) {
        DataExtractors.runDataExtractors(executionReport, extractorDataList);
        for (ExecutionListener executionListener : executionListeners) {
            executionListener.executionFinished(executionReport);
        }
    }

    void cancelExecutionAndThrowExceptionIfPendingDueToMissingClientCertificate(ProjectResultReport projectResultReport, TestCase testCase) {
        if (ProjectResultReport.StatusEnum.PENDING.equals(projectResultReport.getStatus())) {
            List<UnresolvedFile> unresolvedFiles = projectResultReport.getUnresolvedFiles();
            if (unresolvedFiles.size() > 0) {
                testServerClient.cancelExecution(projectResultReport.getExecutionID());
            }
            for (UnresolvedFile unresolvedFile : unresolvedFiles) {
                if (testCase == null || unresolvedFile.getFileName().equals(testCase.getClientCertFileName())) {
                    throw new ApiException(400, "Couldn't find client certificate file: " + unresolvedFile.getFileName());
                }
                throwExceptionIfTestStepCertificateIsUnresolved(testCase, unresolvedFile);
            }
        }
    }

    private void throwExceptionIfTestStepCertificateIsUnresolved(TestCase testCase, UnresolvedFile unresolvedFile) {
        for (TestStep testStep : testCase.getTestSteps()) {
            if (testStep instanceof RequestTestStepBase) {
                RequestTestStepBase requestTestStepBase = (RequestTestStepBase) testStep;
                if (unresolvedFile.getFileName().equals(requestTestStepBase.getClientCertificateFileName())) {
                    throw new ApiException(400, "Couldn't find test step client certificate file: " + requestTestStepBase.getClientCertificateFileName());
                }
            }
        }
    }

    private class ExecutionStatusChecker {
        private final Timer timer;

        private final TestServerExecution execution;

        private int errorCount = 0;

        ExecutionStatusChecker(TestServerExecution execution) {
            this.execution = execution;
            timer = new Timer();
        }

        void start() {
            timer.schedule(new CheckingExpireDateTask(), 0, 1000);
        }

        class CheckingExpireDateTask extends TimerTask {
            @Override
            public void run() {
                try {
                    ProjectResultReport executionStatus = testServerClient.getExecutionStatus(execution.getId());
                    execution.addResultReport(executionStatus);
                    if (!ProjectResultReport.StatusEnum.RUNNING.equals(executionStatus.getStatus())) {
                        notifyExecutionFinished(executionStatus);
                        timer.cancel();
                    }
                    errorCount = 0;
                } catch (Exception e) {
                    if (errorCount > NUMBER_OF_RETRIES_IN_CASE_OF_ERRORS) {
                        timer.cancel();
                    }
                    logger.debug("Error while checking for execution status", e);
                    errorCount++;
                }
            }
        }
    }
}
