package com.smartbear.readyapi4j.result;

import com.smartbear.readyapi.client.model.ProjectResultReport;

import java.util.List;
import java.util.Optional;

public interface RecipeExecutionResult {

    long getTimeTaken();

    String getExecutionId();

    ProjectResultReport.StatusEnum getStatus();

    List<String> getErrorMessages();

    int getResultCount();

    TestStepResult getTestStepResult(int index);

    Optional<TestStepResult> getFirstTestStepResult(String testStepName);

    Optional<TestStepResult> getLastTestStepResult(String testStepName);

    List<TestStepResult> getTestStepResults();

    List<TestStepResult> getTestStepResults(String testStepName);

    List<TestStepResult> getFailedTestStepsResults();

    List<TestStepResult> getFailedTestStepsResults(String testStepName);
}
