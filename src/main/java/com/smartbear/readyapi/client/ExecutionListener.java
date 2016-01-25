package com.smartbear.readyapi.client;

import io.swagger.client.model.ProjectResultReport;
import io.swagger.client.model.TestCase;

public interface ExecutionListener {
    void requestSent(ProjectResultReport projectResultReport);

    void executionFinished(ProjectResultReport projectResultReport);

    void errorOccurred(Exception exception);
}
