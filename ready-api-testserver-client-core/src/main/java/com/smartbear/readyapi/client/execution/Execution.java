package com.smartbear.readyapi.client.execution;

import com.smartbear.readyapi.client.model.ProjectResultReport;
import com.smartbear.readyapi.client.result.RecipeExecutionResult;

import java.util.List;

public interface Execution {
    String getId();

    ProjectResultReport.StatusEnum getCurrentStatus();

    ProjectResultReport getCurrentReport();

    RecipeExecutionResult getExecutionResult();

    List<String> getErrorMessages();

    void cancelExecution();
}
