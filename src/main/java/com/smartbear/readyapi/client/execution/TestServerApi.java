package com.smartbear.readyapi.client.execution;

import com.smartbear.readyapi.client.RepositoryProjectExecutionRequest;
import com.smartbear.readyapi.client.model.HarLogRoot;
import com.smartbear.readyapi.client.model.ProjectResultReport;
import com.smartbear.readyapi.client.model.ProjectResultReports;
import com.smartbear.readyapi.client.model.TestCase;
import io.swagger.client.auth.HttpBasicAuth;

import java.io.File;
import java.net.URL;

/**
 * Defines an API stub that can communicate with the Ready! API Server.
 */
public interface TestServerApi {

    ProjectResultReport postProject(ProjectExecutionRequest projectExecutionRequest, boolean async, HttpBasicAuth auth) throws ApiException;

    /**
     * Executes a project from an existing repository on TestServer
     *
     * @param executionRequest request with execution details
     * @param async            true if request should be executed asynchronously
     * @param auth             credentials container
     * @return ProjectResultReport with current state of the execution.
     * @throws ApiException if validation fails or server returns with an error
     */
    ProjectResultReport postRepositoryProject(RepositoryProjectExecutionRequest executionRequest, boolean async, HttpBasicAuth auth) throws ApiException;

    ProjectResultReport postTestRecipe(TestCase body, boolean async, HttpBasicAuth auth) throws ApiException;

    /**
     * Submit Swagger specification file to TestServer to create and execute tests for each api defined in specifications.
     *
     * @param swaggerFile   Swagger file
     * @param swaggerFormat format
     * @param endpoint      endpoint against which tests should be executed.
     *                      Tests will be executed against the host specified in Swagger definition if endpoint is not provided.
     * @param async         true if request should be executed asynchronously
     * @param auth          credentials container
     * @return execution
     */
    ProjectResultReport postSwagger(File swaggerFile, RecipeExecutor.SwaggerFormat swaggerFormat,
                                    String endpoint, boolean async, HttpBasicAuth auth) throws ApiException;

    /**
     * Submit URL of Swagger specification to TestServer to create and execute tests for each api defined in specifications.
     *
     * @param swaggerApiURL URL of Swagger API
     * @param endpoint      endpoint against which tests should be executed.
     *                      Tests will be executed against the host specified in Swagger definition if endpoint is not provided.
     * @param async         true if request should be executed asynchronously
     * @param auth          credentials container
     * @return execution
     */
    ProjectResultReport postSwagger(URL swaggerApiURL, String endpoint, boolean async, HttpBasicAuth auth) throws ApiException;

    ProjectResultReport getExecutionStatus(String executionID, HttpBasicAuth auth) throws ApiException;

    ProjectResultReports getExecutions(HttpBasicAuth auth) throws ApiException;

    ProjectResultReport cancelExecution(String executionID, HttpBasicAuth auth) throws ApiException;

    HarLogRoot getTransactionLog(String executionID, String transactionId, HttpBasicAuth auth) throws ApiException;

    void setBasePath(String basePath);

    void setConnectTimeout(int connectionTimeout);

    void setDebugging(boolean debugging);
}
