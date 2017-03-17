package com.smartbear.readyapi4j.testserver.execution;

import com.smartbear.readyapi4j.execution.Execution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.File;

/**
 * Executor for existing SoapUI / ReadyAPI project files
 */

public class ProjectExecutor extends AbstractTestServerExecutor {
    private static final Logger logger = LoggerFactory.getLogger(ProjectExecutor.class);

    ProjectExecutor(TestServerClient testServerClient) {
        super(testServerClient);
    }

    public Execution submitRepositoryProject(RepositoryProjectExecutionRequest executionRequest) {
        TestServerExecution execution = doExecuteProjectFromRepository(executionRequest, true);
        notifyExecutionStarted(execution);
        return execution;
    }

    public Execution executeRepositoryProject(RepositoryProjectExecutionRequest executionRequest) {
        Execution execution = doExecuteProjectFromRepository(executionRequest, false);
        notifyExecutionFinished(execution);
        return execution;
    }

    /**
     * @deprecated Use TestServerRecipeExecutor#submitProject(ProjectExecutionRequest) instead.
     */
    @Deprecated
    public Execution submitProject(File project) {
        return submitProject(project, null, null, null);
    }

    /**
     * @deprecated Use TestServerRecipeExecutor#submitProject(ProjectExecutionRequest) instead.
     */
    @Deprecated
    public Execution submitProject(File project, @Nullable String testCaseName, @Nullable String testSuiteName,
                                   @Nullable String environment) throws ApiException {
        ProjectExecutionRequest executionRequest = ProjectExecutionRequest.Builder.forProjectFile(project)
                .forTestCase(testCaseName)
                .forTestSuite(testSuiteName)
                .forEnvironment(environment)
                .build();
        return submitProject(executionRequest);
    }

    /**
     * Submits the specified request for asynchronous execution
     *
     * @param projectExecutionRequest a configured execution request
     * @return the execution object for the executing project
     * @throws ApiException if an error occurs executing the project
     */

    public Execution submitProject(ProjectExecutionRequest projectExecutionRequest) throws ApiException {
        TestServerExecution execution = doExecuteProject(projectExecutionRequest, true);
        notifyExecutionStarted(execution);
        return execution;
    }

    /**
     * @deprecated Use TestServerRecipeExecutor#executeProject(ProjectExecutionRequest) instead.
     */
    @Deprecated
    public Execution executeProject(File project) {
        return executeProject(project, null, null, null);
    }

    /**
     * @deprecated Use TestServerRecipeExecutor#executeProject(ProjectExecutionRequest) instead.
     */
    @Deprecated
    public Execution executeProject(File project, @Nullable String testCaseName, @Nullable String testSuiteName,
                                    @Nullable String environment) throws ApiException {
        ProjectExecutionRequest executionRequest = ProjectExecutionRequest.Builder.forProjectFile(project)
                .forTestCase(testCaseName)
                .forTestSuite(testSuiteName)
                .forEnvironment(environment)
                .build();
        return executeProject(executionRequest);
    }

    /**
     * Submits the specified request for synchronous execution
     *
     * @param projectExecutionRequest a configured execution request
     * @return the execution object for the executing project
     * @throws ApiException if an error occurs executing the project
     */

    public Execution executeProject(ProjectExecutionRequest projectExecutionRequest) throws ApiException {
        Execution execution = doExecuteProject(projectExecutionRequest, false);
        notifyExecutionFinished(execution);
        return execution;
    }

    private TestServerExecution doExecuteProjectFromRepository(RepositoryProjectExecutionRequest executionRequest, boolean async) throws ApiException {
        try {
            TestServerExecution execution = testServerClient.postRepositoryProject(executionRequest, async);
            cancelExecutionAndThrowExceptionIfPendingDueToMissingClientCertificate(execution.getCurrentReport(), null);
            return execution;
        } catch (ApiException e) {
            notifyErrorOccurred(e);
            logger.debug("An error occurred when sending project to server. Details: " + e.toString());
            throw e;
        } catch (Exception e) {
            notifyErrorOccurred(e);
            logger.debug("An error occurred when sending project to server", e);
            throw new ApiException(e);
        }
    }

    private TestServerExecution doExecuteProject(ProjectExecutionRequest projectExecutionRequest, boolean async) throws ApiException {
        try {
            TestServerExecution execution = testServerClient.postProject(projectExecutionRequest, async);
            cancelExecutionAndThrowExceptionIfPendingDueToMissingClientCertificate(execution.getCurrentReport(), null);
            return execution;
        } catch (ApiException e) {
            notifyErrorOccurred(e);
            logger.debug("An error occurred when sending project to server. Details: " + e.toString());
            throw e;
        } catch (Exception e) {
            notifyErrorOccurred(e);
            logger.debug("An error occurred when sending project to server", e);
            throw new ApiException(e);
        }
    }

}
