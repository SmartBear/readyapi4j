package com.smartbear.readyapi.client.execution;

import com.smartbear.readyapi.client.ExecutionListener;
import com.smartbear.readyapi.client.TestRecipe;
import com.smartbear.readyapi.client.TestRecipeBuilder;
import com.smartbear.readyapi.client.model.ProjectResultReport;
import com.smartbear.readyapi.client.model.ProjectResultReports;
import com.smartbear.readyapi.client.model.TestCase;
import com.smartbear.readyapi.client.teststeps.TestSteps;
import io.swagger.client.auth.HttpBasicAuth;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.smartbear.readyapi.client.execution.ExecutionTestHelper.makeCancelledReport;
import static com.smartbear.readyapi.client.execution.ExecutionTestHelper.makeFinishedReport;
import static com.smartbear.readyapi.client.execution.ExecutionTestHelper.makePendingReportWithUnresolvedFiles;
import static com.smartbear.readyapi.client.execution.ExecutionTestHelper.makeProjectResultReports;
import static com.smartbear.readyapi.client.execution.ExecutionTestHelper.makeRunningReport;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the RecipeExecutor.
 */
public class TestServerRequestExecutorTest extends ProjectExecutionTestBase {

    private static final String CLIENT_CERTIFICATE_FILE_NAME = "ClientCertificate.cert";

    private TestRecipe recipeToSubmit;

    @Before
    public void setUp() throws Exception {
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

        Execution execution = recipeExecutor.submitRecipe(recipeToSubmit);
        assertThat(execution.getCurrentStatus(), is(ProjectResultReport.StatusEnum.RUNNING));
        assertThat(execution.getCurrentReport(), is(startReport));
        Thread.sleep(1500);
        assertThat(execution.getCurrentStatus(), is(ProjectResultReport.StatusEnum.FINISHED));
        assertThat(execution.getCurrentReport(), is(endReport));
    }

    @Test
    public void sendsNotificationsOnAsynchronousRequests() throws Exception {
        String executionID = "the_id";
        ProjectResultReport startReport = makeRunningReport(executionID);
        ProjectResultReport endReport = makeFinishedReport(executionID);
        when(apiWrapper.postTestRecipe(eq(recipeToSubmit.getTestCase()), eq(true), any(HttpBasicAuth.class))).thenReturn(startReport);
        when(apiWrapper.getExecutionStatus(eq(executionID), any(HttpBasicAuth.class))).thenReturn(endReport);
        ExecutionListener executionListener = mock(ExecutionListener.class);

        recipeExecutor.addExecutionListener(executionListener);
        recipeExecutor.submitRecipe(recipeToSubmit);
        Thread.sleep(1500);
        verify(executionListener).requestSent(startReport);
        verify(executionListener).executionFinished(endReport);
    }

    @Test
    public void executesRecipeSynchronously() throws Exception {
        ProjectResultReport report = makeFinishedReport("execution_ID");
        when(apiWrapper.postTestRecipe(eq(recipeToSubmit.getTestCase()), eq(false), any(HttpBasicAuth.class))).thenReturn(report);

        Execution execution = recipeExecutor.executeRecipe(recipeToSubmit);
        assertThat(execution.getCurrentReport(), is(report));
    }

    @Test
    public void notifiesListenerSynchronousExecutionLifecycleEvents() throws Exception {
        ProjectResultReport report = makeFinishedReport("execution_ID");
        when(apiWrapper.postTestRecipe(eq(recipeToSubmit.getTestCase()), eq(false), any(HttpBasicAuth.class))).thenReturn(report);
        ExecutionListener executionListener = mock(ExecutionListener.class);

        recipeExecutor.addExecutionListener(executionListener);
        recipeExecutor.executeRecipe(recipeToSubmit);
        verify(executionListener).executionFinished(report);

    }

    @Test
    public void getsExecutions() throws Exception {
        ProjectResultReports projectStatusReports = makeProjectResultReports();
        when(apiWrapper.getExecutions(any(HttpBasicAuth.class))).thenReturn(projectStatusReports);
        List<Execution> executions = testServerClient.getExecutions();
        assertThat(executions.size(), is(2));
    }

    @Test
    public void cancelsExecutions() throws Exception {
        ProjectResultReport runningReport = makeRunningReport("execution_ID");
        when(apiWrapper.postTestRecipe(eq(recipeToSubmit.getTestCase()), eq(true), any(HttpBasicAuth.class))).thenReturn(runningReport);
        ProjectResultReport cancelledReport = makeCancelledReport("execution_ID");
        when(apiWrapper.cancelExecution(eq(cancelledReport.getExecutionID()), any(HttpBasicAuth.class))).thenReturn(cancelledReport);
        when(apiWrapper.getExecutionStatus(eq(cancelledReport.getExecutionID()), any(HttpBasicAuth.class))).thenReturn(cancelledReport);
        Execution execution = recipeExecutor.submitRecipe(recipeToSubmit);
        assertThat(execution.getCurrentStatus(), is(ProjectResultReport.StatusEnum.RUNNING));

        execution = testServerClient.cancelExecution(execution);
        assertThat(execution.getCurrentStatus(), is(ProjectResultReport.StatusEnum.CANCELED));
    }

    @Test
    public void throwsExceptionAndExecutionIsNullIfClientCertificateNotProvidedAndNotFoundOnServer() throws Exception {
        TestRecipe testRecipe = new TestRecipeBuilder()
                .withClientCertificate(CLIENT_CERTIFICATE_FILE_NAME)
                .withClientCertificatePassword("password")
                .buildTestRecipe();

        ProjectResultReport pendingReport = makePendingReportWithUnresolvedFiles("executionId", CLIENT_CERTIFICATE_FILE_NAME);
        when(apiWrapper.postTestRecipe(eq(testRecipe.getTestCase()), eq(true), any(HttpBasicAuth.class))).thenReturn(pendingReport);

        recipeExecutor.addExecutionListener(createExecutionListenerWithExpectedErrorMessage("Couldn't find client certificate file: " + CLIENT_CERTIFICATE_FILE_NAME));
        try {
            recipeExecutor.submitRecipe(testRecipe);
            assertTrue(false);
        } catch (com.smartbear.readyapi.client.execution.ApiException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void throwsExceptionIfTestStepClientCertificateNotProvidedAndNotFoundOnServer() throws Exception {
        TestRecipe testRecipe = new TestRecipeBuilder()
                .addStep(TestSteps.getRequest("http://localhost:8080")
                        .withClientCertificate("clientCertificate.jks")
                )
                .buildTestRecipe();

        ProjectResultReport pendingReport = makePendingReportWithUnresolvedFiles("executionId", "clientCertificate.jks");
        when(apiWrapper.postTestRecipe(eq(testRecipe.getTestCase()), eq(true), any(HttpBasicAuth.class))).thenReturn(pendingReport);

        recipeExecutor.addExecutionListener(createExecutionListenerWithExpectedErrorMessage("Couldn't find test step client certificate file: clientCertificate.jks"));

        try {
            recipeExecutor.submitRecipe(testRecipe);
            assertTrue(false);
        } catch (com.smartbear.readyapi.client.execution.ApiException e) {
            e.printStackTrace();
        }
    }

    private ExecutionListener createExecutionListenerWithExpectedErrorMessage(final String expectedErrorMessage) {
        return new ExecutionListener() {
            @Override
            public void requestSent(ProjectResultReport projectResultReport) {
            }

            @Override
            public void executionFinished(ProjectResultReport projectResultReport) {
            }

            @Override
            public void errorOccurred(Exception exception) {
                assertThat(exception.getMessage(), is(expectedErrorMessage));
            }
        };
    }
}