package com.smartbear.readyapi.client.result;

import com.smartbear.readyapi.client.model.TestStepResultReport;

import java.util.List;

public interface RecipeExecutionResult {
    long getTimeTaken();

    String getExecutionId();

    Status getStatus();

    int getResultCount();

    TestStepResultReport getTestStepResult(int index);

    List<TestStepResultReport> getTestStepResults();

    List<TestStepResultReport> getFailedTestStepsResults();

    List<TestStepResultReport> getTestStepResults(String testStepName);

    enum Status {
        INITIALIZED, PENDING, RUNNING, CANCELED, FINISHED, FAILED, WARNING
    }
}
