package com.smartbear.readyapi4j;

import com.smartbear.readyapi.client.model.ProjectResultReport;
import com.smartbear.readyapi4j.execution.Execution;

/**
 * Listener for events related to test execution
 */
public interface ExecutionListener {

    /**
     * Called when a recipe has been submitted for execution - this is only called for asynchronous executions
     *
     * @param projectResultReport the current ProjectResultReport
     * @deprecated use the version that takes an Execution instead
     */

    @Deprecated
    void executionStarted(ProjectResultReport projectResultReport);

    /**
     * Called when a recipe execution has finished - this is called for both synchronous and asynchronous executions
     *
     * @param projectResultReport the final ProjectResultReport
     * @deprecated use the version that takes an Execution instead
     */

    @Deprecated
    void executionFinished(ProjectResultReport projectResultReport);

    /**
     * Called if an error occurs during recipe execution

     * @param exception
     */

    void errorOccurred(Exception exception);

    /**
     * Called when a recipe has been submitted for execution - this is only called for asynchronous executions
     *
     * @param execution the started execution
     */

    void executionStarted(Execution execution);

    /**
     * Called when a recipe execution has finished - this is called for both synchronous and asynchronous executions
     *
     * @param execution the finished execution
     */

    void executionFinished(Execution execution);
}
