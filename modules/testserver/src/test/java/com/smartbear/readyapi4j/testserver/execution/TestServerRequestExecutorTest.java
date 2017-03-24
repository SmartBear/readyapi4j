package com.smartbear.readyapi4j.testserver.execution;

import com.smartbear.readyapi.client.model.ProjectResultReport;
import com.smartbear.readyapi.client.model.ProjectResultReports;
import com.smartbear.readyapi.client.model.TestCase;
import com.smartbear.readyapi4j.ExecutionListener;
import com.smartbear.readyapi4j.TestRecipe;
import com.smartbear.readyapi4j.TestRecipeBuilder;
import com.smartbear.readyapi4j.execution.Execution;
import com.smartbear.readyapi4j.extractor.ExtractorData;
import com.smartbear.readyapi4j.teststeps.TestSteps;
import io.swagger.client.auth.HttpBasicAuth;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the TestServerRecipeExecutor.
 */
public class TestServerRequestExecutorTest extends ProjectExecutionTestBase {

    private static final String CLIENT_CERTIFICATE_FILE_NAME = "ClientCertificate.cert";

    private TestRecipe recipeToSubmit;

    @Before
    public void setUp() throws Exception {
        recipeToSubmit = new TestRecipe(new TestCase(), new ExtractorData());
    }

    @Test
    public void setsBasePathCorrectly() throws Exception {
        verify(apiWrapper).setBasePath("https://" + HOST + ":" + PORT + BASE_PATH);
    }

    @Test
    public void submitsRecipeToApi() throws Exception {
        String executionID = "the_id";
        ProjectResultReport startReport = ExecutionTestHelper.makeRunningReport(executionID);
        ProjectResultReport endReport = ExecutionTestHelper.makeFinishedReport(executionID);
        when(apiWrapper.postTestRecipe(eq(recipeToSubmit), eq(true), any(HttpBasicAuth.class))).thenReturn(startReport);
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
        ProjectResultReport startReport = ExecutionTestHelper.makeRunningReport(executionID);
        ProjectResultReport endReport = ExecutionTestHelper.makeFinishedReport(executionID);
        when(apiWrapper.postTestRecipe(eq(recipeToSubmit), eq(true), any(HttpBasicAuth.class))).thenReturn(startReport);
        when(apiWrapper.getExecutionStatus(eq(executionID), any(HttpBasicAuth.class))).thenReturn(endReport);
        ExecutionListener executionListener = mock(ExecutionListener.class);

        recipeExecutor.addExecutionListener(executionListener);
        recipeExecutor.submitRecipe(recipeToSubmit);
        verify(executionListener).executionStarted(argThat(new ArgumentMatcher<Execution>(){
            @Override
            public boolean matches(Object o) {
                return ((Execution)o).getCurrentReport().equals( startReport );
            }
        }));
        Thread.sleep(1500);
        verify(executionListener).executionFinished(argThat(new ArgumentMatcher<Execution>(){
            @Override
            public boolean matches(Object o) {
                return ((Execution)o).getCurrentReport().equals( endReport );
            }
        }));
    }

    @Test
    public void executesRecipeSynchronously() throws Exception {
        ProjectResultReport report = ExecutionTestHelper.makeFinishedReport("execution_ID");
        when(apiWrapper.postTestRecipe(eq(recipeToSubmit), eq(false), any(HttpBasicAuth.class))).thenReturn(report);

        Execution execution = recipeExecutor.executeRecipe(recipeToSubmit);
        assertThat(execution.getCurrentReport(), is(report));
    }

    @Test
    public void notifiesListenerSynchronousExecutionLifecycleEvents() throws Exception {
        ProjectResultReport report = ExecutionTestHelper.makeFinishedReport("execution_ID");
        when(apiWrapper.postTestRecipe(eq(recipeToSubmit), eq(false), any(HttpBasicAuth.class))).thenReturn(report);
        ExecutionListener executionListener = mock(ExecutionListener.class);

        recipeExecutor.addExecutionListener(executionListener);
        recipeExecutor.executeRecipe(recipeToSubmit);
        verify(executionListener).executionFinished(argThat(new ArgumentMatcher<Execution>(){
            @Override
            public boolean matches(Object o) {
                return ((Execution)o).getCurrentReport().equals( report );
            }
        }));

    }

    @Test
    public void getsExecutions() throws Exception {
        ProjectResultReports projectStatusReports = ExecutionTestHelper.makeProjectResultReports();
        when(apiWrapper.getExecutions(any(HttpBasicAuth.class))).thenReturn(projectStatusReports);
        List<Execution> executions = testServerClient.getExecutions();
        assertThat(executions.size(), is(2));
    }

    @Test
    public void cancelsExecutions() throws Exception {
        ProjectResultReport runningReport = ExecutionTestHelper.makeRunningReport("execution_ID");
        when(apiWrapper.postTestRecipe(eq(recipeToSubmit), eq(true), any(HttpBasicAuth.class))).thenReturn(runningReport);
        ProjectResultReport cancelledReport = ExecutionTestHelper.makeCancelledReport("execution_ID");
        when(apiWrapper.cancelExecution(eq(cancelledReport.getExecutionID()), any(HttpBasicAuth.class))).thenReturn(cancelledReport);
        when(apiWrapper.getExecutionStatus(eq(cancelledReport.getExecutionID()), any(HttpBasicAuth.class))).thenReturn(cancelledReport);
        TestServerExecution execution = recipeExecutor.submitRecipe(recipeToSubmit);
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

        ProjectResultReport pendingReport = ExecutionTestHelper.makePendingReportWithUnresolvedFiles("executionId", CLIENT_CERTIFICATE_FILE_NAME);
        when(apiWrapper.postTestRecipe(eq(testRecipe), eq(true), any(HttpBasicAuth.class))).thenReturn(pendingReport);

        recipeExecutor.addExecutionListener(createExecutionListenerWithExpectedErrorMessage("Couldn't find client certificate file: " + CLIENT_CERTIFICATE_FILE_NAME));
        try {
            recipeExecutor.submitRecipe(testRecipe);
            assertTrue(false);
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void throwsExceptionIfTestStepClientCertificateNotProvidedAndNotFoundOnServer() throws Exception {
        TestRecipe testRecipe = new TestRecipeBuilder()
                .addStep(TestSteps.GET("http://localhost:8080")
                        .withClientCertificate("clientCertificate.jks")
                )
                .buildTestRecipe();

        ProjectResultReport pendingReport = ExecutionTestHelper.makePendingReportWithUnresolvedFiles("executionId", "clientCertificate.jks");
        when(apiWrapper.postTestRecipe(eq(testRecipe), eq(true), any(HttpBasicAuth.class))).thenReturn(pendingReport);

        recipeExecutor.addExecutionListener(createExecutionListenerWithExpectedErrorMessage("Couldn't find test step client certificate file: clientCertificate.jks"));

        try {
            recipeExecutor.submitRecipe(testRecipe);
            assertTrue(false);
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    private ExecutionListener createExecutionListenerWithExpectedErrorMessage(final String expectedErrorMessage) {
        return new ExecutionListener() {
            @Override
            public void errorOccurred(Exception exception) {
                assertThat(exception.getMessage(), is(expectedErrorMessage));
            }
        };
    }
}