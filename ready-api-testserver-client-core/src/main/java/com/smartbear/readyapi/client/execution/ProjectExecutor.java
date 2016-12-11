package com.smartbear.readyapi.client.execution;

import com.smartbear.readyapi.client.RepositoryProjectExecutionRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.File;

public class ProjectExecutor extends AbstractTestServerExecutor {
    private static final Logger logger = LoggerFactory.getLogger(ProjectExecutor.class);

    ProjectExecutor(TestServerClient testServerClient) {
        super(testServerClient);
    }

    public Execution submitRepositoryProject(RepositoryProjectExecutionRequest executionRequest) {
        TestServerExecution execution = doExecuteProjectFromRepository(executionRequest, true);
        notifyRequestSubmitted(execution);
        return execution;
    }

    public Execution executeRepositoryProject(RepositoryProjectExecutionRequest executionRequest) {
        Execution execution = doExecuteProjectFromRepository(executionRequest, false);
        if (execution != null) {
            notifyExecutionFinished(execution.getCurrentReport());
        }
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
        ProjectExecutionRequest executionRequest = ProjectExecutionRequest.Builder.newInstance()
                .withProjectFile(project)
                .forTestCase(testCaseName)
                .forTestSuite(testSuiteName)
                .forEnvironment(environment)
                .build();
        return submitProject(executionRequest);
    }

    public Execution submitProject(ProjectExecutionRequest projectExecutionRequest) throws ApiException {
        TestServerExecution execution = doExecuteProject(projectExecutionRequest, true);
        notifyRequestSubmitted(execution);
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
        ProjectExecutionRequest executionRequest = ProjectExecutionRequest.Builder.newInstance()
                .withProjectFile(project)
                .forTestCase(testCaseName)
                .forTestSuite(testSuiteName)
                .forEnvironment(environment)
                .build();
        return executeProject(executionRequest);
    }

    public Execution executeProject(ProjectExecutionRequest projectExecutionRequest) throws ApiException {
        Execution execution = doExecuteProject(projectExecutionRequest, false);
        if (execution != null) {
            notifyExecutionFinished(execution.getCurrentReport());
        }
        return execution;
    }

    private TestServerExecution doExecuteProjectFromRepository(RepositoryProjectExecutionRequest executionRequest, boolean async) {
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
