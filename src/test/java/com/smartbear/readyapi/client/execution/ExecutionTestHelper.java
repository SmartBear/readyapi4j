package com.smartbear.readyapi.client.execution;


import com.smartbear.readyapi.client.model.ProjectResultReport;
import com.smartbear.readyapi.client.model.ProjectResultReports;

import java.util.ArrayList;
import java.util.List;

public class ExecutionTestHelper {
    public static ProjectResultReports makeProjectResultReports() {
        List<ProjectResultReport> projectResultReportList = new ArrayList<>();
        projectResultReportList.add(makeRunningReport("ExecutionIdRunning"));
        projectResultReportList.add(makeFinishedReport("ExecutionIdFinished1"));
        ProjectResultReports projectStatusReports = new ProjectResultReports();
        projectStatusReports.setProjectResultReports(projectResultReportList);
        return projectStatusReports;
    }

    public static ProjectResultReport makeRunningReport(String executionID) {
        ProjectResultReport startReport = new ProjectResultReport();
        startReport.setExecutionID(executionID);
        startReport.setStatus(ProjectResultReport.StatusEnum.RUNNING);
        return startReport;
    }

    public static ProjectResultReport makeFinishedReport(String executionID) {
        ProjectResultReport report = new ProjectResultReport();
        report.setExecutionID(executionID);
        report.setStatus(ProjectResultReport.StatusEnum.FINISHED);
        return report;
    }

    public static ProjectResultReport makeCancelledReport(String executionID) {
        ProjectResultReport startReport = new ProjectResultReport();
        startReport.setExecutionID(executionID);
        startReport.setStatus(ProjectResultReport.StatusEnum.CANCELED);
        return startReport;
    }

}
