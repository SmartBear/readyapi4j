package com.smartbear.readyapi4j.testengine.execution;

import com.smartbear.readyapi4j.client.model.TestJobReport;
import com.smartbear.readyapi4j.execution.Execution;
import com.smartbear.readyapi4j.execution.ExecutionListener;
import com.smartbear.readyapi4j.extractor.DataExtractors;
import com.smartbear.readyapi4j.extractor.ExtractorData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Base class for the various TestEngine executors
 */

abstract class AbstractTestEngineExecutor {
    public enum PendingResonsePolicy {
        ACCEPT,
        REJECT
    }

    private static Logger logger = LoggerFactory.getLogger(AbstractTestEngineExecutor.class);
    private static final int NUMBER_OF_RETRIES_IN_CASE_OF_ERRORS = 3;

    List<ExtractorData> extractorDataList = new LinkedList<>();

    private final PendingResonsePolicy pendingResonsePolicy;
    private final List<ExecutionListener> executionListeners = new CopyOnWriteArrayList<>();

    final TestEngineClient testEngineClient;

    AbstractTestEngineExecutor(TestEngineClient testEngineClient, PendingResonsePolicy pendingResonsePolicy) {
        this.testEngineClient = testEngineClient;
        this.pendingResonsePolicy = pendingResonsePolicy;
    }

    AbstractTestEngineExecutor(TestEngineClient testEngineClient) {
        this(testEngineClient, PendingResonsePolicy.REJECT);
    }

    public void addExecutionListener(ExecutionListener listener) {
        executionListeners.add(listener);
    }

    public void removeExecutionListener(ExecutionListener listener) {
        executionListeners.remove(listener);
    }

    void notifyExecutionStarted(TestEngineExecution execution) {
        if (execution != null) {
            for (ExecutionListener executionListener : executionListeners) {
                executionListener.executionStarted(execution);
            }
            new ExecutionStatusChecker(execution).start();
        }
    }

    void notifyErrorOccurred(Exception e) {
        for (ExecutionListener executionListener : executionListeners) {
            executionListener.errorOccurred(e);
        }
    }

    void notifyExecutionFinished(Execution execution) {
        TestJobReport executionReport = execution.getCurrentReport();
        DataExtractors.runDataExtractors(executionReport, extractorDataList);
        for (ExecutionListener executionListener : executionListeners) {
            executionListener.executionFinished(execution);
        }
    }

    private class ExecutionStatusChecker {
        private final Timer timer;

        private final TestEngineExecution execution;

        private int errorCount = 0;

        ExecutionStatusChecker(TestEngineExecution execution) {
            this.execution = execution;
            timer = new Timer();
        }

        void start() {
            timer.schedule(new CheckingExpireDateTask(), 1000, 1000);
        }

        class CheckingExpireDateTask extends TimerTask {
            @Override
            public void run() {
                try {
                    TestJobReport executionStatus = testEngineClient.getExecutionStatus(execution.getId());
                    execution.addResultReport(executionStatus);
                    if (!TestJobReport.StatusEnum.RUNNING.equals(executionStatus.getStatus())) {
                        notifyExecutionFinished(execution);
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
