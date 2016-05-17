package com.smartbear.readyapi.client.execution;

import com.google.common.collect.Lists;
import com.smartbear.readyapi.client.model.ProjectResultReport;
import com.smartbear.readyapi.client.model.TestCaseResultReport;
import com.smartbear.readyapi.client.model.TestStepResultReport;
import com.smartbear.readyapi.client.model.TestSuiteResultReport;
import com.smartbear.readyapi.client.result.RecipeExecutionResult;
import com.smartbear.readyapi.client.result.TestStepResult;
import io.swagger.client.auth.HttpBasicAuth;

import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Represents an execution, synchronous or asynchronous, and encapsulates all available information about the execution.
 */

public class Execution {
    private final Deque<ProjectResultReport> executionStatusReports = new ConcurrentLinkedDeque<>();
    private final String id;
    private final TestServerApi testServerApi;
    private final HttpBasicAuth auth;

    public Execution(TestServerApi testServerApi, HttpBasicAuth auth, ProjectResultReport projectResultReport) {
        this.testServerApi = testServerApi;
        this.auth = auth;
        executionStatusReports.add(projectResultReport);
        this.id = projectResultReport.getExecutionID();
    }

    public String getId() {
        return id;
    }

    public ProjectResultReport.StatusEnum getCurrentStatus() {
        return executionStatusReports.getLast().getStatus();
    }

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

    public RecipeExecutionResult getExecutionResult() {
        ProjectResultReport lastReport = executionStatusReports.getLast();
        return lastReport == null ? null : new ProjectRecipeExecutionResult(getCurrentReport());
    }

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

     class ProjectRecipeExecutionResult implements RecipeExecutionResult {
        private final ProjectResultReport report;
        private final List<TestStepResult> results = Lists.newArrayList();

        public ProjectRecipeExecutionResult(ProjectResultReport currentReport ) {
            report = currentReport;

            for (TestSuiteResultReport testSuiteReport : report.getTestSuiteResultReports()) {
                for (TestCaseResultReport testCaseResultReport : testSuiteReport.getTestCaseResultReports()) {
                    for (TestStepResultReport testStepResultReport : testCaseResultReport.getTestStepResultReports()) {
                        results.add(new TestStepResult(testStepResultReport, Execution.this ));
                    }
                }
            }
        }

        @Override
        public long getTimeTaken() {
            return report.getTimeTaken();
        }

        @Override
        public String getExecutionId() {
            return report.getExecutionID();
        }

        @Override
        public ProjectResultReport.StatusEnum getStatus() {
            return report.getStatus();
        }

        @Override
        public int getResultCount() {
            return results.size();
        }

        public List<String> getErrorMessages() {
            List<String> result = Lists.newArrayList();

            for (TestStepResult testStepResultReport : results) {
                if (testStepResultReport.getAssertionStatus() == TestStepResultReport.AssertionStatusEnum.FAILED) {
                    result.addAll(testStepResultReport.getMessages());
                }
            }

            return result;
        }

        @Override
        public TestStepResult getFirstTestStepResult(String name) {
            for (TestStepResult testStepResultReport : results) {
                if (testStepResultReport.getTestStepName().equalsIgnoreCase(name)) {
                    return testStepResultReport;
                }
            }

            return null;
        }

        @Override
        public TestStepResult getLastTestStepResult(String testStepName) {
            for (TestStepResult testStepResultReport : Lists.reverse(results)) {
                if (testStepResultReport.getTestStepName().equalsIgnoreCase(testStepName)) {
                    return testStepResultReport;
                }
            }

            return null;
        }

        @Override
        public List<TestStepResult> getTestStepResults() {
            return Collections.unmodifiableList(results);
        }

        @Override
        public List<TestStepResult> getFailedTestStepsResults() {
            List<TestStepResult> result = Lists.newArrayList();

            for (TestStepResult testStepResultReport : results) {
                if (testStepResultReport.getAssertionStatus() == TestStepResultReport.AssertionStatusEnum.FAILED) {
                    result.add(testStepResultReport);
                }
            }

            return result;
        }

        @Override
        public List<TestStepResult> getFailedTestStepsResults(String testStepName) {
            List<TestStepResult> result = Lists.newArrayList();

            for (TestStepResult testStepResultReport : results) {
                if (testStepResultReport.getAssertionStatus() == TestStepResultReport.AssertionStatusEnum.FAILED &&
                    testStepResultReport.getTestStepName().equalsIgnoreCase(testStepName)) {
                    result.add(testStepResultReport);
                }
            }

            return result;
        }

        @Override
        public List<TestStepResult> getTestStepResults(String testStepName) {
            List<TestStepResult> result = Lists.newArrayList();

            for (TestStepResult testStepResultReport : results) {
                if (testStepResultReport.getTestStepName().equalsIgnoreCase(testStepName)) {
                    result.add(testStepResultReport);
                }
            }

            return result;
        }

        @Override
        public TestStepResult getTestStepResult(int index) {
            return results.get(index);
        }

        @Override
        public Execution getExecution() {
            return Execution.this;
        }
    }
}
