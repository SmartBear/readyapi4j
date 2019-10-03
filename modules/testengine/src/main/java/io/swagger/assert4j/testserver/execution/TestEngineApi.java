package io.swagger.assert4j.testserver.execution;

import io.swagger.assert4j.HttpBasicAuth;
import io.swagger.assert4j.TestRecipe;
import io.swagger.assert4j.client.model.HarLogRoot;
import io.swagger.assert4j.client.model.TestJobReport;

/**
 * Defines an API stub that can communicate with the ReadyAPI Server.
 */
public interface TestEngineApi {

    TestJobReport postProject(ProjectExecutionRequest projectExecutionRequest, boolean async, HttpBasicAuth auth) throws ApiException;

    TestJobReport cancelExecution(String executionID, HttpBasicAuth auth) throws ApiException;

    HarLogRoot getTransactionLog(String executionID, String transactionId, HttpBasicAuth auth) throws ApiException;

    TestJobReport getExecutionStatus(String executionID, HttpBasicAuth auth) throws ApiException;

    void setBasePath(String basePath);

    void setDebugging(boolean debugging);

    TestJobReport postTestRecipe(TestRecipe testRecipe, boolean async, HttpBasicAuth auth) throws ApiException;

    void setConnectTimeout(int connectionTimeout);
}
