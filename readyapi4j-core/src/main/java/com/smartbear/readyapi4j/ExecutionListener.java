package com.smartbear.readyapi4j;

import com.smartbear.readyapi.client.model.ProjectResultReport;

public interface ExecutionListener {
    void executionStarted(ProjectResultReport projectResultReport);

    void executionFinished(ProjectResultReport projectResultReport);

    void errorOccurred(Exception exception);
}
