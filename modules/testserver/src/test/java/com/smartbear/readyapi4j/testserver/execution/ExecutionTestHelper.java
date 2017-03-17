package com.smartbear.readyapi4j.testserver.execution;


import com.smartbear.readyapi.client.model.ProjectResultReport;
import com.smartbear.readyapi.client.model.ProjectResultReports;
import com.smartbear.readyapi.client.model.UnresolvedFile;
import com.smartbear.readyapi4j.execution.Execution;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

        Execution execution = mock( Execution.class );
        when(execution.getId()).thenReturn(executionID);
        when(execution.getCurrentReport()).thenReturn(startReport);
        when(execution.getCurrentStatus()).thenReturn(startReport.getStatus());

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

    public static ProjectResultReport makePendingReportWithUnresolvedFiles(String exceutionId, String... fileNames) {
        ProjectResultReport pendingReport = makePendingReport(exceutionId);
        List<UnresolvedFile> unresolvedFiles = new ArrayList<>();
        for (String fileName : fileNames) {
            UnresolvedFile unresolvedFile = new UnresolvedFile();
            unresolvedFile.setFileName(fileName);
            unresolvedFiles.add(unresolvedFile);
        }
        pendingReport.setUnresolvedFiles(unresolvedFiles);
        return pendingReport;
    }

    public static ProjectResultReport makePendingReport(String executionID) {
        ProjectResultReport startReport = new ProjectResultReport();
        startReport.setExecutionID(executionID);
        startReport.setStatus(ProjectResultReport.StatusEnum.PENDING);
        return startReport;
    }

}
