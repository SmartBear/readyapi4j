package io.swagger.assert4j.testserver.execution;

import io.swagger.assert4j.client.model.ProjectResultReport;
import io.swagger.assert4j.result.AbstractRecipeExecutionResult;

/**
 * ExecutionResult for an execution executed on a TestServer instance
 */

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
