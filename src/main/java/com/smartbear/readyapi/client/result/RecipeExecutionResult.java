package com.smartbear.readyapi.client.result;

import com.smartbear.readyapi.client.model.ProjectResultReport;
import com.smartbear.readyapi.client.model.TestStepResultReport;

import java.util.List;

public interface RecipeExecutionResult {
    long getTimeTaken();

    String getExecutionId();

    ProjectResultReport.StatusEnum getStatus();

    List<String> getErrorMessages();

    int getResultCount();

    TestStepResultReport getTestStepResult(int index);

    TestStepResultReport getFirstTestStepResult(String testStepName);

    TestStepResultReport getLastTestStepResult(String testStepName);

    List<TestStepResultReport> getTestStepResults();

    List<TestStepResultReport> getTestStepResults(String testStepName);

    List<TestStepResultReport> getFailedTestStepsResults();

    List<TestStepResultReport> getFailedTestStepsResults(String testStepName);
}
