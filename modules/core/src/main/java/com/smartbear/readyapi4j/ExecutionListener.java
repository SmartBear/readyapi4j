package com.smartbear.readyapi4j;

import com.smartbear.readyapi.client.model.ProjectResultReport;

/**
 * Listener for events related to TestRecipe execution
 */

public interface ExecutionListener {

    /**
     * Called when a recipe has been submitted for execution - this is only called for asynchronous executions
     *
     * @param projectResultReport the current ProjectResultReport
     */

    void executionStarted(ProjectResultReport projectResultReport);

    /**
     * Called when a recipe execution has finished - this is called for both synchronous and asynchronous executions
     *
     * @param projectResultReport the final ProjectResultReport
     */

    void executionFinished(ProjectResultReport projectResultReport);

    /**
     * Called if an error occurs during recipe execution
     * @param exception
     */

    void errorOccurred(Exception exception);
}
