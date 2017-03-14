package com.smartbear.readyapi4j;

import com.smartbear.readyapi.client.model.ProjectResultReport;
import com.smartbear.readyapi4j.execution.Execution;

public abstract class ExecutionListenerAdapter implements ExecutionListener {
    @Override
    public void executionStarted(ProjectResultReport projectResultReport) {

    }

    @Override
    public void executionFinished(ProjectResultReport projectResultReport) {

    }

    @Override
    public void errorOccurred(Exception exception) {

    }

    @Override
    public void executionStarted(Execution execution) {

    }

    @Override
    public void executionFinished(Execution execution) {

    }
}
