package io.swagger.assert4j.testserver.execution;

import com.google.common.collect.Lists;
import io.swagger.assert4j.client.model.ProjectResultReport;
import io.swagger.assert4j.client.model.TestCaseResultReport;
import io.swagger.assert4j.client.model.TestStepResultReport;
import io.swagger.assert4j.client.model.TestSuiteResultReport;
import io.swagger.assert4j.execution.Execution;
import io.swagger.assert4j.result.RecipeExecutionResult;
import io.swagger.client.auth.HttpBasicAuth;

import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Class corresponding to an execution on a TestServer instance. The execution can be either ongoing or completed.
 */

public class TestServerExecution implements Execution {
    private final Deque<ProjectResultReport> executionStatusReports = new ConcurrentLinkedDeque<>();
    private final String id;
    private final TestServerApi testServerApi;
    private final HttpBasicAuth auth;

    /**
     * Package-scoped constructor since this class should only be created by executors or tests
     */

    TestServerExecution(TestServerApi testServerApi, HttpBasicAuth auth, ProjectResultReport projectResultReport) {
        this.testServerApi = testServerApi;
        this.auth = auth;
        executionStatusReports.add(projectResultReport);
        this.id = projectResultReport.getExecutionID();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public ProjectResultReport.StatusEnum getCurrentStatus() {
        return executionStatusReports.getLast().getStatus();
    }

    @Override
    public ProjectResultReport getCurrentReport() {
        return executionStatusReports.getLast();
    }

    TestServerApi getTestServerApi() {
        return testServerApi;
    }

    HttpBasicAuth getAuth() {
        return auth;
    }

    void addResultReport(ProjectResultReport newReport) {
        executionStatusReports.add(newReport);
    }

    @Override
    public RecipeExecutionResult getExecutionResult() {
        ProjectResultReport lastReport = executionStatusReports.getLast();
        return lastReport == null ? null : new TestServerRecipeExecutionResult(this, getCurrentReport());
    }

    @Override
    public List<String> getErrorMessages() {
        List<String> result = Lists.newArrayList();

        if (executionStatusReports.getLast() != null) {
            for (TestSuiteResultReport testSuiteReport : executionStatusReports.getLast().getTestSuiteResultReports()) {
                for (TestCaseResultReport testCaseResultReport : testSuiteReport.getTestCaseResultReports()) {
                    for (TestStepResultReport testStepResultReport : testCaseResultReport.getTestStepResultReports()) {
                        if (testStepResultReport.getAssertionStatus() == TestStepResultReport.AssertionStatusEnum.FAILED) {
                            result.add(String.format("TestStepName: %s, messages: %s",
                                    testStepResultReport.getTestStepName(),
                                    String.join(", ", testStepResultReport.getMessages())));
                        }
                    }
                }
            }
        }

        return result;
    }

    @Override
    public void cancelExecution() {
        testServerApi.cancelExecution(id, auth);
    }
}
