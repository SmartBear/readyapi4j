package io.swagger.assert4j.result;

import io.swagger.assert4j.client.model.ProjectResultReport;

import java.util.List;
import java.util.Optional;

/**
 * Provides direct access to TestStepResults after a Recipe has been executed
 */

public interface RecipeExecutionResult {
    /**
     * @return the time it took to execute this execution
     */

    long getTimeTaken();

    /**
     * @return the unique id to identify the execution. This id is used to get execution status/report and transaction
     * for specific test step in the recipe.
     */

    String getExecutionId();

    /**
     * @return current status of the execution: INITIALIZED/RUNNING/PASSED/FAILED
     */

    ProjectResultReport.StatusEnum getStatus();

    /**
     * @return a list of collected error messages
     */

    List<String> getErrorMessages();

    /**
     * @return the number of TestStepResults collected for this execution
     */

    int getResultCount();

    /**
     * @param index
     * @return the TestStepResult at the specified index
     */
    TestStepResult getTestStepResult(int index);

    /**
     * @param testStepName
     * @return the first TestStepResult for the specified TestStep
     */
    Optional<TestStepResult> getFirstTestStepResult(String testStepName);

    /**
     * @param testStepName
     * @return the last TestStepResult for the specified TestStep
     */
    Optional<TestStepResult> getLastTestStepResult(String testStepName);

    /**
     * @return a list of all TestStepResults collected for this execution
     */
    List<TestStepResult> getTestStepResults();

    /**
     * @param testStepName
     * @return a list of all TestStepResults collected for the specified TestStep
     */
    List<TestStepResult> getTestStepResults(String testStepName);

    /**
     * @return a list of all failed TestStepResults collected for this execution
     */
    List<TestStepResult> getFailedTestStepsResults();

    /**
     * @param testStepName
     * @return a list of all failed TestStepResults collected for the specified TestStep
     */
    List<TestStepResult> getFailedTestStepsResults(String testStepName);
}
