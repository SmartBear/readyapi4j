package io.swagger.assert4j.testengine.execution;

import io.swagger.assert4j.client.model.TestJobReport;
import io.swagger.assert4j.result.AbstractRecipeExecutionResult;

/**
 * ExecutionResult for an execution executed on a TestEngine instance
 */

public class TestEngineRecipeExecutionResult extends AbstractRecipeExecutionResult {
    private final TestEngineExecution testEngineExecution;

    TestEngineRecipeExecutionResult(TestEngineExecution testEngineExecution, TestJobReport currentReport) {
        super(currentReport, e -> new TestEngineTestStepResult(e, testEngineExecution));
        this.testEngineExecution = testEngineExecution;
    }

    public TestEngineExecution getTestEngineExecution() {
        return testEngineExecution;
    }
}
