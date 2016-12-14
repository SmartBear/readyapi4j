package com.smartbear.readyapi.client.execution;

import com.google.common.collect.Lists;
import com.smartbear.readyapi.client.model.ProjectResultReport;
import com.smartbear.readyapi.client.model.TestCaseResultReport;
import com.smartbear.readyapi.client.model.TestStepResultReport;
import com.smartbear.readyapi.client.model.TestSuiteResultReport;
import com.smartbear.readyapi.client.result.RecipeExecutionResult;
import io.swagger.client.auth.HttpBasicAuth;

import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

public class TestServerExecution implements Execution {
    private final Deque<ProjectResultReport> executionStatusReports = new ConcurrentLinkedDeque<>();
    private final String id;
    private final TestServerApi testServerApi;
    private final HttpBasicAuth auth;

    public TestServerExecution(TestServerApi testServerApi, HttpBasicAuth auth, ProjectResultReport projectResultReport) {
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

    public TestServerApi getTestServerApi() {
        return testServerApi;
    }

    public HttpBasicAuth getAuth() {
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
                            result.addAll(testStepResultReport.getMessages());
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
