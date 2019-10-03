package io.swagger.assert4j.testengine.execution;

import io.swagger.assert4j.HttpBasicAuth;
import io.swagger.assert4j.TestRecipe;
import io.swagger.assert4j.client.model.TestCase;
import io.swagger.assert4j.client.model.TestJobReport;
import io.swagger.assert4j.execution.Execution;
import io.swagger.assert4j.execution.ExecutionListener;
import io.swagger.assert4j.extractor.ExtractorData;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the TestEngineRecipeExecutor.
 */

@Ignore
public class TestEngineRequestExecutorTest extends ProjectExecutionTestBase {

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
        TestJobReport startReport = ExecutionTestHelper.makeRunningReport(executionID);
        TestJobReport endReport = ExecutionTestHelper.makeFinishedReport(executionID);
        when(apiWrapper.postTestRecipe(eq(recipeToSubmit), eq(true), any(HttpBasicAuth.class))).thenReturn(startReport);
        when(apiWrapper.getExecutionStatus(eq(executionID), any(HttpBasicAuth.class))).thenReturn(endReport);

        Execution execution = recipeExecutor.submitRecipe(recipeToSubmit);
        assertThat(execution.getCurrentStatus(), is(TestJobReport.StatusEnum.RUNNING));
        assertThat(execution.getCurrentReport(), is(startReport));
        Thread.sleep(1500);
        assertThat(execution.getCurrentStatus(), is(TestJobReport.StatusEnum.FINISHED));
        assertThat(execution.getCurrentReport(), is(endReport));
    }

    @Test
    public void sendsNotificationsOnAsynchronousRequests() throws Exception {
        String executionID = "the_id";
        TestJobReport startReport = ExecutionTestHelper.makeRunningReport(executionID);
        TestJobReport endReport = ExecutionTestHelper.makeFinishedReport(executionID);
        when(apiWrapper.postTestRecipe(eq(recipeToSubmit), eq(true), any(HttpBasicAuth.class))).thenReturn(startReport);
        when(apiWrapper.getExecutionStatus(eq(executionID), any(HttpBasicAuth.class))).thenReturn(endReport);
        ExecutionListener executionListener = mock(ExecutionListener.class);

        recipeExecutor.addExecutionListener(executionListener);
        recipeExecutor.submitRecipe(recipeToSubmit);
        verify(executionListener).executionStarted(argThat(new ArgumentMatcher<Execution>() {
            @Override
            public boolean matches(Object o) {
                return ((Execution) o).getCurrentReport().equals(startReport);
            }
        }));
        Thread.sleep(1500);
        verify(executionListener).executionFinished(argThat(new ArgumentMatcher<Execution>() {
            @Override
            public boolean matches(Object o) {
                return ((Execution) o).getCurrentReport().equals(endReport);
            }
        }));
    }

    @Test
    public void executesRecipeSynchronously() throws Exception {
        TestJobReport report = ExecutionTestHelper.makeFinishedReport("execution_ID");
        when(apiWrapper.postTestRecipe(eq(recipeToSubmit), eq(false), any(HttpBasicAuth.class))).thenReturn(report);

        Execution execution = recipeExecutor.executeRecipe(recipeToSubmit);
        assertThat(execution.getCurrentReport(), is(report));
    }

    @Test
    public void notifiesListenerSynchronousExecutionLifecycleEvents() throws Exception {
        TestJobReport report = ExecutionTestHelper.makeFinishedReport("execution_ID");
        when(apiWrapper.postTestRecipe(eq(recipeToSubmit), eq(false), any(HttpBasicAuth.class))).thenReturn(report);
        ExecutionListener executionListener = mock(ExecutionListener.class);

        recipeExecutor.addExecutionListener(executionListener);
        recipeExecutor.executeRecipe(recipeToSubmit);
        verify(executionListener).executionFinished(argThat(new ArgumentMatcher<Execution>() {
            @Override
            public boolean matches(Object o) {
                return ((Execution) o).getCurrentReport().equals(report);
            }
        }));

    }

    @Test
    public void cancelsExecutions() throws Exception {
        TestJobReport runningReport = ExecutionTestHelper.makeRunningReport("execution_ID");
        when(apiWrapper.postTestRecipe(eq(recipeToSubmit), eq(true), any(HttpBasicAuth.class))).thenReturn(runningReport);
        TestJobReport cancelledReport = ExecutionTestHelper.makeCancelledReport("execution_ID");
        when(apiWrapper.cancelExecution(eq(cancelledReport.getTestjobId()), any(HttpBasicAuth.class))).thenReturn(cancelledReport);
        when(apiWrapper.getExecutionStatus(eq(cancelledReport.getTestjobId()), any(HttpBasicAuth.class))).thenReturn(cancelledReport);
        TestEngineExecution execution = recipeExecutor.submitRecipe(recipeToSubmit);
        assertThat(execution.getCurrentStatus(), is(TestJobReport.StatusEnum.RUNNING));

        execution = testEngineClient.cancelExecution(execution);
        assertThat(execution.getCurrentStatus(), is(TestJobReport.StatusEnum.CANCELED));
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