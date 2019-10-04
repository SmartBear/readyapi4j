package io.swagger.assert4j.testengine.execution;

import io.swagger.assert4j.client.model.TestJobReport;
import io.swagger.assert4j.execution.Execution;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExecutionTestHelper {

    public static TestJobReport makeRunningReport(String executionID) {
        TestJobReport startReport = new TestJobReport();
        startReport.setTestjobId(executionID);
        startReport.setStatus(TestJobReport.StatusEnum.RUNNING);

        Execution execution = mock(Execution.class);
        when(execution.getId()).thenReturn(executionID);
        when(execution.getCurrentReport()).thenReturn(startReport);
        when(execution.getCurrentStatus()).thenReturn(startReport.getStatus());

        return startReport;
    }

    public static TestJobReport makeFinishedReport(String executionID) {
        TestJobReport report = new TestJobReport();
        report.setTestjobId(executionID);
        report.setStatus(TestJobReport.StatusEnum.FINISHED);
        return report;
    }

    public static TestJobReport makeCancelledReport(String executionID) {
        TestJobReport startReport = new TestJobReport();
        startReport.setTestjobId(executionID);
        startReport.setStatus(TestJobReport.StatusEnum.CANCELED);
        return startReport;
    }
}
