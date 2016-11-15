package com.smartbear.readyapi.client.execution;

import com.smartbear.readyapi.client.model.ProjectResultReport;
import com.smartbear.readyapi.client.model.ProjectResultReports;
import com.smartbear.readyapi.client.model.TestCase;
import io.swagger.client.auth.HttpBasicAuth;

/**
 * Defines a class that can execute recipes, either locally or on a remote server.
 */
public interface RecipeExecutionSupport {

    /**
     * Submit a Test recipe for execution.
     * @param testCase a Test Case struct representing the Test recipe to be executed.
     * @param async a boolean indicating whether the execution should be done in the background
     * @param auth an object encapsulating the Http authentication to use when connecting to a server, or <code>null</code> if the invocation is local
     * @return
     * @throws ApiException
     */
    ProjectResultReport postTestRecipe(TestCase testCase, boolean async, HttpBasicAuth auth) throws ApiException;

    ProjectResultReport getExecutionStatus(String executionID, HttpBasicAuth auth) throws ApiException;

    ProjectResultReports getExecutions(HttpBasicAuth auth) throws ApiException;

    ProjectResultReport cancelExecution(String executionID, HttpBasicAuth auth) throws ApiException;

}
