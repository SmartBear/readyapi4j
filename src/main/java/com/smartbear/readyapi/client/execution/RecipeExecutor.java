package com.smartbear.readyapi.client.execution;

import com.smartbear.readyapi.client.ExecutionListener;
import com.smartbear.readyapi.client.RecipeFilter;
import com.smartbear.readyapi.client.TestRecipe;
import com.smartbear.readyapi.client.model.HarLogRoot;
import com.smartbear.readyapi.client.model.ProjectResultReport;
import com.smartbear.readyapi.client.model.ProjectResultReports;
import com.smartbear.readyapi.client.model.RequestTestStepBase;
import com.smartbear.readyapi.client.model.TestCase;
import com.smartbear.readyapi.client.model.TestStep;
import com.smartbear.readyapi.client.model.UnresolvedFile;
import io.swagger.client.auth.HttpBasicAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Responsible for executing test recipes on a Ready! API Server, synchronously or asynchronously.
 */
public class RecipeExecutor {

    private static Logger logger;

    static {
        if (System.getProperty("org.slf4j.simpleLogger.defaultLogLevel") == null) { //Don't set if user has defined the log level
            System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "trace");
        }
        logger = LoggerFactory.getLogger(RecipeExecutor.class);
    }

    private static final int NUMBER_OF_RETRIES_IN_CASE_OF_ERRORS = 3;
    private final TestServerApi apiStub;
    private HttpBasicAuth authentication;
    private final List<ExecutionListener> executionListeners = new CopyOnWriteArrayList<>();
    private final List<RecipeFilter> recipeFilters = new CopyOnWriteArrayList<>();

    public RecipeExecutor(Scheme scheme, String host, int port) {
        this(scheme, host, port, ServerDefaults.VERSION_PREFIX, new CodegenBasedTestServerApi());
    }

    public RecipeExecutor(String host, int port) {
        this(ServerDefaults.DEFAULT_SCHEME, host, port);
    }

    public RecipeExecutor(String host) {
        this(host, ServerDefaults.DEFAULT_PORT);
    }

    // Used for testing
    RecipeExecutor(Scheme scheme, String host, int port, String basePath, TestServerApi apiStub) {
        this.apiStub = apiStub;
        apiStub.setBasePath(String.format("%s://%s:%d%s", scheme.getValue(), host, port, basePath));
    }

    public void setCredentials(String username, String password) {
        authentication = new HttpBasicAuth();
        authentication.setUsername(username);
        authentication.setPassword(password);
    }

    public void addRecipeFilter(RecipeFilter recipeFilter) {
        recipeFilters.add(recipeFilter);
    }

    public void removeRecipeFilter(RecipeFilter recipeFilter) {
        recipeFilters.remove(recipeFilter);
    }

    public void addExecutionListener(ExecutionListener listener) {
        executionListeners.add(listener);
    }

    public void removeExecutionListener(ExecutionListener listener) {
        executionListeners.remove(listener);
    }

    public Execution submitRecipe(TestRecipe recipe) throws ApiException {

        for (RecipeFilter recipeFilter : recipeFilters) {
            recipeFilter.filterRecipe(recipe);
        }

        Execution execution = doExecuteTestCase(recipe.getTestCase(), true);
        if (execution != null) {
            for (ExecutionListener executionListener : executionListeners) {
                executionListener.requestSent(execution.getCurrentReport());
            }
            new ExecutionStatusChecker(execution).start();
        }
        return execution;
    }

    public Execution executeRecipe(TestRecipe recipe) throws ApiException {
        Execution execution = doExecuteTestCase(recipe.getTestCase(), false);
        if (execution != null) {
            notifyExecutionFinished(execution.getCurrentReport());
        }
        return execution;
    }

    public Execution cancelExecution(final Execution execution) {
        ProjectResultReport projectResultReport = apiStub.cancelExecution(execution.getId(), authentication);
        execution.addResultReport(projectResultReport);
        return execution;
    }

    public HarLogRoot getTransactionLog(final Execution execution, String transactionId) {
        return apiStub.getTransactionLog(execution.getId(), transactionId, authentication);
    }

    public List<Execution> getExecutions() {
        List<Execution> executions = new ArrayList<>();
        ProjectResultReports projectResultReport = apiStub.getExecutions(authentication);
        for (ProjectResultReport resultReport : projectResultReport.getProjectResultReports()) {
            executions.add(new Execution(apiStub, authentication, resultReport));
        }
        return executions;
    }

    private Execution doExecuteTestCase(TestCase testCase, boolean async) throws ApiException {
        try {
            ProjectResultReport projectResultReport = apiStub.postTestRecipe(testCase, async, authentication);
            cancelExecutionAndThrowExceptionIfPendingDueToMissingClientCertificate(projectResultReport, testCase);
            return new Execution(apiStub, authentication, projectResultReport);
        } catch (ApiException e) {
            invokeListeners(e);
            logger.debug("An error occurred when sending test recipe to server. Details: " + e.toString());
            throw e;
        } catch (Exception e) {
            invokeListeners(e);
            logger.debug("An error occurred when sending test recipe to server", e);
            throw new ApiException(e);
        }
    }

    private void invokeListeners(Exception e) {
        for (ExecutionListener executionListener : executionListeners) {
            executionListener.errorOccurred(e);
        }
    }

    private void cancelExecutionAndThrowExceptionIfPendingDueToMissingClientCertificate(ProjectResultReport projectResultReport, TestCase testCase) {
        if (ProjectResultReport.StatusEnum.PENDING.equals(projectResultReport.getStatus())) {
            List<UnresolvedFile> unresolvedFiles = projectResultReport.getUnresolvedFiles();
            if (unresolvedFiles.size() > 0) {
                apiStub.cancelExecution(projectResultReport.getExecutionID(), authentication);
            }
            for (UnresolvedFile unresolvedFile : unresolvedFiles) {
                if (unresolvedFile.getFileName().equals(testCase.getClientCertFileName())) {
                    throw new ApiException(400, "Couldn't find client certificate file");
                }
                throwExceptionIfTestStepCertificateIsUnresolved(projectResultReport, testCase, unresolvedFile);
            }
        }
    }

    private void throwExceptionIfTestStepCertificateIsUnresolved(ProjectResultReport projectResultReport, TestCase testCase, UnresolvedFile unresolvedFile) {
        for (TestStep testStep : testCase.getTestSteps()) {
            if (testStep instanceof RequestTestStepBase) {
                RequestTestStepBase requestTestStepBase = (RequestTestStepBase) testStep;
                if (unresolvedFile.getFileName().equals(requestTestStepBase.getClientCertificateFileName())) {
                    throw new ApiException(400, "Couldn't find test step client certificate file: " + requestTestStepBase.getClientCertificateFileName());
                }
            }
        }
    }

    private void notifyExecutionFinished(ProjectResultReport executionStatus) {
        for (ExecutionListener executionListener : executionListeners) {
            executionListener.executionFinished(executionStatus);
        }
    }

    private class ExecutionStatusChecker {
        private final Timer timer;

        private final Execution execution;

        private int errorCount = 0;

        public ExecutionStatusChecker(Execution execution) {
            this.execution = execution;
            timer = new Timer();
        }

        public void start() {
            timer.schedule(new CheckingExpireDateTask(), 0, 1000);
        }

        class CheckingExpireDateTask extends TimerTask {
            @Override
            public void run() {
                try {
                    ProjectResultReport executionStatus = apiStub.getExecutionStatus(execution.getId(), authentication);
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
