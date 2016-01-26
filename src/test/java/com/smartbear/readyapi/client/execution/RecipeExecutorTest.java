package com.smartbear.readyapi.client.execution;


import com.smartbear.readyapi.client.ExecutionListener;
import com.smartbear.readyapi.client.TestRecipe;
import com.smartbear.readyapi.client.model.ProjectResultReport;
import com.smartbear.readyapi.client.model.ProjectResultReports;
import com.smartbear.readyapi.client.model.TestCase;
import io.swagger.client.auth.HttpBasicAuth;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the RecipeExecutor.
 */
public class RecipeExecutorTest {

    private static final String HOST = "thehost";
    private static final int PORT = 6234;
    private static final String BASE_PATH = "/custom_path";

    private SmartestApiWrapper apiWrapper;
    private RecipeExecutor executor;
    private TestRecipe recipeToSubmit;

    @Before
    public void setUp() throws Exception {
        apiWrapper = mock(SmartestApiWrapper.class);
        executor = new RecipeExecutor(ServerDefaults.DEFAULT_SCHEME, HOST, PORT, BASE_PATH, apiWrapper);
        executor.setCredentials("theUser", "thePassword");
        recipeToSubmit = new TestRecipe(new TestCase());
    }

    @Test
    public void setsBasePathCorrectly() throws Exception {
        verify(apiWrapper).setBasePath("https://" + HOST + ":" + PORT + BASE_PATH);
    }

    @Test
    public void submitsRecipeToApi() throws Exception {
        String executionID = "the_id";
        ProjectResultReport startReport = makeRunningReport(executionID);
        ProjectResultReport endReport = makeFinishedReport(executionID);
        when(apiWrapper.postTestRecipe(eq(recipeToSubmit.getTestCase()), eq(true), any(HttpBasicAuth.class))).thenReturn(startReport);
        when(apiWrapper.getExecutionStatus(eq(executionID), any(HttpBasicAuth.class))).thenReturn(endReport);

        Execution execution = executor.submitRecipe(recipeToSubmit);
        assertThat(execution.getCurrentStatus(), is(ProjectResultReport.StatusEnum.RUNNING));
        assertThat(execution.getCurrentReport(), is(startReport));
        Thread.sleep(1500);
        assertThat(execution.getCurrentStatus(), is(ProjectResultReport.StatusEnum.FINISHED));
        assertThat(execution.getCurrentReport(), is(endReport));
    }

    @Test
    public void sendsNotificationsOnAsynchrounousRequests() throws Exception {
        String executionID = "the_id";
        ProjectResultReport startReport = makeRunningReport(executionID);
        ProjectResultReport endReport = makeFinishedReport(executionID);
        when(apiWrapper.postTestRecipe(eq(recipeToSubmit.getTestCase()), eq(true), any(HttpBasicAuth.class))).thenReturn(startReport);
        when(apiWrapper.getExecutionStatus(eq(executionID), any(HttpBasicAuth.class))).thenReturn(endReport);
        ExecutionListener executionListener = mock(ExecutionListener.class);

        executor.addExecutionListener(executionListener);
        executor.submitRecipe(recipeToSubmit);
        Thread.sleep(1500);
        verify(executionListener).requestSent(startReport);
        verify(executionListener).executionFinished(endReport);
    }

    @Test
    public void executesRecipeSynchronously() throws Exception {
        ProjectResultReport report = makeFinishedReport("execution_ID");
        when(apiWrapper.postTestRecipe(eq(recipeToSubmit.getTestCase()), eq(false), any(HttpBasicAuth.class))).thenReturn(report);

        Execution execution = executor.executeRecipe(recipeToSubmit);
        assertThat(execution.getCurrentReport(), is(report));
    }

    @Test
    public void notifiesListenerSynchronousExecutionLifecycleEvents() throws Exception {
        ProjectResultReport report = makeFinishedReport("execution_ID");
        when(apiWrapper.postTestRecipe(eq(recipeToSubmit.getTestCase()), eq(false), any(HttpBasicAuth.class))).thenReturn(report);
        ExecutionListener executionListener = mock(ExecutionListener.class);

        executor.addExecutionListener(executionListener);
        executor.executeRecipe(recipeToSubmit);
        verify(executionListener).executionFinished(report);

    }

    @Test
    public void getsExecutions() throws Exception {
        ProjectResultReports projectStatusReports = makeProjectResultReports();
        when(apiWrapper.getExecutions(any(HttpBasicAuth.class))).thenReturn(projectStatusReports);
        List<Execution> executions = executor.getExecutions();
        assertThat(executions.size(), is(2));
    }

    @Test
    public void cancelsExecutions() throws Exception {
        ProjectResultReport runningReport = makeRunningReport("execution_ID");
        when(apiWrapper.postTestRecipe(eq(recipeToSubmit.getTestCase()), eq(true), any(HttpBasicAuth.class))).thenReturn(runningReport);
        ProjectResultReport cancelledReport = makeCancelledReport("execution_ID");
        when(apiWrapper.cancelExecution(eq(cancelledReport.getExecutionID()), any(HttpBasicAuth.class))).thenReturn(cancelledReport);
        when(apiWrapper.getExecutionStatus(eq(cancelledReport.getExecutionID()), any(HttpBasicAuth.class))).thenReturn(cancelledReport);
        Execution execution = executor.submitRecipe(recipeToSubmit);
        assertThat(execution.getCurrentStatus(), is(ProjectResultReport.StatusEnum.RUNNING));

        execution = executor.cancelExecution(execution);
        assertThat(execution.getCurrentStatus(), is(ProjectResultReport.StatusEnum.CANCELED));
    }

    private ProjectResultReports makeProjectResultReports() {
        List<ProjectResultReport> projectResultReportList = new ArrayList<>();
        projectResultReportList.add(makeRunningReport("ExecutionIdRunning"));
        projectResultReportList.add(makeFinishedReport("ExecutionIdFinished1"));
        ProjectResultReports projectStatusReports = new ProjectResultReports();
        projectStatusReports.setProjectResultReports(projectResultReportList);
        return projectStatusReports;
    }

    private ProjectResultReport makeRunningReport(String executionID) {
        ProjectResultReport startReport = new ProjectResultReport();
        startReport.setExecutionID(executionID);
        startReport.setStatus(ProjectResultReport.StatusEnum.RUNNING);
        return startReport;
    }

    private ProjectResultReport makeFinishedReport(String executionID) {
        ProjectResultReport report = new ProjectResultReport();
        report.setExecutionID(executionID);
        report.setStatus(ProjectResultReport.StatusEnum.FINISHED);
        return report;
    }

    private ProjectResultReport makeCancelledReport(String executionID) {
        ProjectResultReport startReport = new ProjectResultReport();
        startReport.setExecutionID(executionID);
        startReport.setStatus(ProjectResultReport.StatusEnum.CANCELED);
        return startReport;
    }
}