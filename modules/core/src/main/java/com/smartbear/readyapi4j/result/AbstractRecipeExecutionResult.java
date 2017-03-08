package com.smartbear.readyapi4j.result;

import com.google.common.collect.Lists;
import com.smartbear.readyapi.client.model.ProjectResultReport;
import com.smartbear.readyapi.client.model.TestCaseResultReport;
import com.smartbear.readyapi.client.model.TestStepResultReport;
import com.smartbear.readyapi.client.model.TestSuiteResultReport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public abstract class AbstractRecipeExecutionResult implements RecipeExecutionResult {
    protected final ProjectResultReport report;
    protected final List<TestStepResult> results = new ArrayList<>();

    public AbstractRecipeExecutionResult(ProjectResultReport currentReport, TestStepResultBuilder testStepResultBuilder) {
        report = currentReport;

        for (TestSuiteResultReport testSuiteReport : report.getTestSuiteResultReports()) {
            for (TestCaseResultReport testCaseResultReport : testSuiteReport.getTestCaseResultReports()) {
                for (TestStepResultReport testStepResultReport : testCaseResultReport.getTestStepResultReports()) {
                    results.add(testStepResultBuilder.buildTestStepResult(testStepResultReport));
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
        List<String> result = new ArrayList<>();

        for (TestStepResult testStepResultReport : results) {
            if (testStepResultReport.getAssertionStatus() == TestStepResultReport.AssertionStatusEnum.FAILED) {
                result.addAll(testStepResultReport.getMessages());
            }
        }

        return result;
    }

    @Override
    public Optional<TestStepResult> getFirstTestStepResult(String name) {
        return results.stream().filter(result -> result.getTestStepName().equalsIgnoreCase(name)).findAny();
    }

    @Override
    public Optional<TestStepResult> getLastTestStepResult(String testStepName) {
        return Lists.reverse(results).stream().filter(result -> result.getTestStepName().equalsIgnoreCase(testStepName)).findAny();
    }

    @Override
    public List<TestStepResult> getTestStepResults() {
        return Collections.unmodifiableList(results);
    }

    @Override
    public List<TestStepResult> getFailedTestStepsResults() {
        List<TestStepResult> result = new ArrayList<>();

        for (TestStepResult testStepResultReport : results) {
            if (testStepResultReport.getAssertionStatus() == TestStepResultReport.AssertionStatusEnum.FAILED) {
                result.add(testStepResultReport);
            }
        }

        return result;
    }

    @Override
    public List<TestStepResult> getFailedTestStepsResults(String testStepName) {
        List<TestStepResult> result = new ArrayList<>();

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
        List<TestStepResult> result = new ArrayList<>();

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

    public interface TestStepResultBuilder {
        TestStepResult buildTestStepResult(TestStepResultReport testStepResultReport);
    }
}
