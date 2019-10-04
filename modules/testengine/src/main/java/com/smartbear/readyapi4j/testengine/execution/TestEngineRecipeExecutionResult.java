package com.smartbear.readyapi4j.testengine.execution;

import com.smartbear.readyapi4j.client.model.TestJobReport;
import com.smartbear.readyapi4j.result.AbstractRecipeExecutionResult;

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
