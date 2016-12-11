package com.smartbear.readyapi.client.execution;

import com.smartbear.readyapi.client.model.ProjectResultReport;
import com.smartbear.readyapi.client.result.AbstractRecipeExecutionResult;
import com.smartbear.readyapi.client.result.TestServerTestStepResult;

public class TestServerRecipeExecutionResult extends AbstractRecipeExecutionResult {
    private final TestServerExecution testServerExecution;

    TestServerRecipeExecutionResult(TestServerExecution testServerExecution, ProjectResultReport currentReport) {
        super(currentReport, e -> new TestServerTestStepResult(e, testServerExecution));
        this.testServerExecution = testServerExecution;
    }

    public TestServerExecution getTestServerExecution() {
        return testServerExecution;
    }
}
