package com.smartbear.readyapi.client.execution;

import com.google.common.collect.Lists;
import com.smartbear.readyapi.client.model.ProjectResultReport;
import com.smartbear.readyapi.client.model.TestCaseResultReport;
import com.smartbear.readyapi.client.model.TestStepResultReport;
import com.smartbear.readyapi.client.model.TestSuiteResultReport;
import com.smartbear.readyapi.client.result.RecipeExecutionResult;

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

    public Execution(ProjectResultReport projectResultReport) {
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

    static class ProjectRecipeExecutionResult implements RecipeExecutionResult {
        private final ProjectResultReport report;
        private final List<TestStepResultReport> results = Lists.newArrayList();

        public ProjectRecipeExecutionResult(ProjectResultReport currentReport) {
            report = currentReport;

            for (TestSuiteResultReport testSuiteReport : report.getTestSuiteResultReports()) {
                for (TestCaseResultReport testCaseResultReport : testSuiteReport.getTestCaseResultReports()) {
                    for (TestStepResultReport testStepResultReport : testCaseResultReport.getTestStepResultReports()) {
                        results.add(testStepResultReport);
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

            for (TestStepResultReport testStepResultReport : results) {
                if (testStepResultReport.getAssertionStatus() == TestStepResultReport.AssertionStatusEnum.FAILED) {
                    result.addAll(testStepResultReport.getMessages());
                }
            }

            return result;
        }

        @Override
        public TestStepResultReport getFirstTestStepResult(String name) {
            for (TestStepResultReport testStepResultReport : results) {
                if (testStepResultReport.getTestStepName().equalsIgnoreCase(name)) {
                    return testStepResultReport;
                }
            }

            return null;
        }

        @Override
        public TestStepResultReport getLastTestStepResult(String testStepName) {
            for (TestStepResultReport testStepResultReport : Lists.reverse(results)) {
                if (testStepResultReport.getTestStepName().equalsIgnoreCase(testStepName)) {
                    return testStepResultReport;
                }
            }

            return null;
        }

        @Override
        public List<TestStepResultReport> getTestStepResults() {
            return Collections.unmodifiableList(results);
        }

        @Override
        public List<TestStepResultReport> getFailedTestStepsResults() {
            List<TestStepResultReport> result = Lists.newArrayList();

            for (TestStepResultReport testStepResultReport : results) {
                if (testStepResultReport.getAssertionStatus() == TestStepResultReport.AssertionStatusEnum.FAILED) {
                    result.add(testStepResultReport);
                }
            }

            return result;
        }

        @Override
        public List<TestStepResultReport> getFailedTestStepsResults(String testStepName) {
            List<TestStepResultReport> result = Lists.newArrayList();

            for (TestStepResultReport testStepResultReport : results) {
                if (testStepResultReport.getAssertionStatus() == TestStepResultReport.AssertionStatusEnum.FAILED &&
                    testStepResultReport.getTestStepName().equalsIgnoreCase(testStepName)) {
                    result.add(testStepResultReport);
                }
            }

            return result;
        }

        @Override
        public List<TestStepResultReport> getTestStepResults(String testStepName) {
            List<TestStepResultReport> result = Lists.newArrayList();

            for (TestStepResultReport testStepResultReport : results) {
                if (testStepResultReport.getTestStepName().equalsIgnoreCase(testStepName)) {
                    result.add(testStepResultReport);
                }
            }

            return result;
        }

        @Override
        public TestStepResultReport getTestStepResult(int index) {
            return results.get(index);
        }
    }
}
