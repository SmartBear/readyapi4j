package io.swagger.assert4j.execution;

/**
 * Listener for events related to test execution
 */
public interface ExecutionListener {

    /**
     * Called if an error occurs during recipe execution
     *
     * @param exception
     */

    default void errorOccurred(Exception exception) {
    }

    /**
     * Called when a recipe has been submitted for execution - this is only called for asynchronous executions
     *
     * @param execution the started execution
     */

    default void executionStarted(Execution execution) {
    }

    /**
     * Called when a recipe execution has finished - this is called for both synchronous and asynchronous executions
     *
     * @param execution the finished execution
     */

    default void executionFinished(Execution execution) {
    }
}
