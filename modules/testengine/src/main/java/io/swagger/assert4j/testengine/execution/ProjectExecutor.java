package io.swagger.assert4j.testengine.execution;

import io.swagger.assert4j.execution.Execution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.File;

/**
 * Executor for existing SoapUI / ReadyAPI project files
 */

public class ProjectExecutor extends AbstractTestEngineExecutor {

    private static final Logger logger = LoggerFactory.getLogger(ProjectExecutor.class);

    ProjectExecutor(TestEngineClient testEngineClient, PendingResonsePolicy pendingResonsePolicy) {
        super(testEngineClient, pendingResonsePolicy);
    }

    ProjectExecutor(TestEngineClient testEngineClient) {
        this(testEngineClient, PendingResonsePolicy.REJECT);
    }

    /**
     * @deprecated Use TestEngineRecipeExecutor#submitProject(ProjectExecutionRequest) instead.
     */
    @Deprecated
    public Execution submitProject(File project) {
        return submitProject(project, null, null, null);
    }

    /**
     * @deprecated Use TestEngineRecipeExecutor#submitProject(ProjectExecutionRequest) instead.
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
        TestEngineExecution execution = doExecuteProject(projectExecutionRequest, true);
        notifyExecutionStarted(execution);
        return execution;
    }

    /**
     * @deprecated Use TestEngineRecipeExecutor#executeProject(ProjectExecutionRequest) instead.
     */
    @Deprecated
    public Execution executeProject(File project) {
        return executeProject(project, null, null, null);
    }

    /**
     * @deprecated Use TestEngineRecipeExecutor#executeProject(ProjectExecutionRequest) instead.
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

    public Execution executeProject(ProjectExecutionRequest projectExecutionRequest) {
        Execution execution = doExecuteProject(projectExecutionRequest, false);
        notifyExecutionFinished(execution);
        return execution;
    }

    private TestEngineExecution doExecuteProject(ProjectExecutionRequest projectExecutionRequest, boolean async) throws ApiException {
        try {
            TestEngineExecution execution = testEngineClient.postProject(projectExecutionRequest, async);
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
