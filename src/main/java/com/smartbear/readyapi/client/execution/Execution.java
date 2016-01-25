package com.smartbear.readyapi.client.execution;

import io.swagger.client.model.ProjectResultReport;

import java.util.Deque;
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
}
