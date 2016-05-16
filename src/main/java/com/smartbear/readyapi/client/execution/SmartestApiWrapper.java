package com.smartbear.readyapi.client.execution;

import com.smartbear.readyapi.client.model.HarLogRoot;
import com.smartbear.readyapi.client.model.ProjectResultReport;
import com.smartbear.readyapi.client.model.ProjectResultReports;
import com.smartbear.readyapi.client.model.TestCase;
import io.swagger.client.auth.HttpBasicAuth;

/**
 * Defines an API stub that can communicate with the Ready! API Server.
 */
public interface SmartestApiWrapper {
    ProjectResultReport postTestRecipe(TestCase body, boolean async, HttpBasicAuth auth) throws ApiException;

    ProjectResultReport getExecutionStatus(String executionID, HttpBasicAuth auth) throws ApiException;

    ProjectResultReports getExecutions(HttpBasicAuth auth) throws ApiException;

    ProjectResultReport cancelExecution(String executionID, HttpBasicAuth auth) throws ApiException;

    HarLogRoot getTransactionLog(String executionID, String transactionId, HttpBasicAuth auth) throws ApiException;

    void setBasePath(String basePath);
}
