package com.smartbear.readyapi.client.execution;

import com.smartbear.readyapi.client.RepositoryProjectExecutionRequest;
import com.smartbear.readyapi.client.model.HarLogRoot;
import com.smartbear.readyapi.client.model.ProjectResultReport;
import com.smartbear.readyapi.client.model.ProjectResultReports;
import com.smartbear.readyapi.client.model.TestCase;
import io.swagger.client.auth.HttpBasicAuth;

import javax.annotation.Nullable;
import java.io.File;

/**
 * Defines an API stub that can communicate with the Ready! API Server.
 */
public interface TestServerApi {

    ProjectResultReport postProject(File file, boolean async, HttpBasicAuth auth, @Nullable String testCaseName, @Nullable String testSuiteName, @Nullable String environment) throws ApiException;

    /**
     * Executes a project from an existing repository on TestServer
     *
     * @param executionRequest request with execution details
     * @param async            true if request should be executed asynchronously
     * @param auth             credentials container
     * @return ProjectResultReport with current state of the execution.
     * @throws ApiException
     */
    ProjectResultReport postRepositoryProject(RepositoryProjectExecutionRequest executionRequest, boolean async, HttpBasicAuth auth) throws ApiException;

    ProjectResultReport postTestRecipe(TestCase body, boolean async, HttpBasicAuth auth) throws ApiException;

    ProjectResultReport getExecutionStatus(String executionID, HttpBasicAuth auth) throws ApiException;

    ProjectResultReports getExecutions(HttpBasicAuth auth) throws ApiException;

    ProjectResultReport cancelExecution(String executionID, HttpBasicAuth auth) throws ApiException;

    HarLogRoot getTransactionLog(String executionID, String transactionId, HttpBasicAuth auth) throws ApiException;

    void setBasePath(String basePath);

    void setConnectTimeout(int connectionTimeout);

    void setDebugging(boolean debugging);
}
