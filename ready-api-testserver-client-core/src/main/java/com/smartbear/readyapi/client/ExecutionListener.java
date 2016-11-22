package com.smartbear.readyapi.client;

import com.smartbear.readyapi.client.model.ProjectResultReport;

public interface ExecutionListener {
    void requestSent(ProjectResultReport projectResultReport);

    void executionFinished(ProjectResultReport projectResultReport);

    void errorOccurred(Exception exception);
}
