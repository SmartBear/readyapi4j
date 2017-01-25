package com.smartbear.readyapi4j.execution;

import com.smartbear.readyapi.client.model.ProjectResultReport;
import com.smartbear.readyapi4j.result.RecipeExecutionResult;

import java.util.List;

/**
 * Represents an execution, synchronous or asynchronous, and encapsulates all
 * available information about the execution.
 */

public interface Execution {

    /**
     * @return the internal ID of the execution
     */
    String getId();

    /**
     * @return the current status for the execution
     */
    ProjectResultReport.StatusEnum getCurrentStatus();

    /**
     * @return the current result report for the execution
     */
    ProjectResultReport getCurrentReport();

    /**
     * @return the current ExecutionResult for this execution - might not be complete if the execution has not finished
s     */

    RecipeExecutionResult getExecutionResult();

    /**
     * Gets a list of error messages collected during this execution
     */

    List<String> getErrorMessages();

    /**
     * Cancels this execution if it is still running
     */

    void cancelExecution();
}
