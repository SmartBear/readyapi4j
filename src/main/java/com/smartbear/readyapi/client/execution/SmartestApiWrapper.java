package com.smartbear.readyapi.client.execution;

import io.swagger.client.auth.HttpBasicAuth;
import io.swagger.client.model.ProjectResultReport;
import io.swagger.client.model.ProjectResultReports;
import io.swagger.client.model.TestCase;

/**
 * Defines an API stub that can communicate with the Ready! API Server.
 */
public interface SmartestApiWrapper {
    ProjectResultReport postTestRecipe(TestCase body, boolean async, HttpBasicAuth auth) throws ApiException;

    ProjectResultReport getExecutionStatus(String executionID, HttpBasicAuth auth) throws ApiException;

    ProjectResultReports getExecutions(HttpBasicAuth auth) throws ApiException;

    ProjectResultReport cancelExecution(String executionID, HttpBasicAuth auth) throws ApiException;

    void setBasePath(String basePath);
}
