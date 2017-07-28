package io.swagger.assert4j.testserver.execution;

import io.swagger.assert4j.client.model.ProjectResultReport;
import io.swagger.assert4j.ExecutionListener;
import io.swagger.assert4j.execution.Execution;
import io.swagger.client.auth.HttpBasicAuth;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RepositoryProjectExecutionTest extends ProjectExecutionTestBase {

    private RepositoryProjectExecutionRequest executionRequest;

    @Before
    public void setUp() throws Exception {
        executionRequest = createRepositoryProjectExecutionRequest();
    }

    @Test
    public void executesProjectFromRepository() throws Exception {
        ProjectResultReport endReport = ExecutionTestHelper.makeFinishedReport("executionId");
        when(apiWrapper.postRepositoryProject(eq(executionRequest), eq(false), any(HttpBasicAuth.class))).thenReturn(endReport);

        Execution execution = projectExecutor.executeRepositoryProject(executionRequest);//recipeExecutor.submitRepositoryProject(executionRequest) for async
        assertThat(execution.getCurrentStatus(), is(ProjectResultReport.StatusEnum.FINISHED));
    }

    @Test
    public void sendsNotificationsOnAsynchronousExecutionRequests() throws Exception {
        String executionID = "the_id";
        ProjectResultReport startReport = ExecutionTestHelper.makeRunningReport(executionID);
        ProjectResultReport endReport = ExecutionTestHelper.makeFinishedReport(executionID);
        when(apiWrapper.postRepositoryProject(eq(executionRequest), eq(true), any(HttpBasicAuth.class))).thenReturn(startReport);
        when(apiWrapper.getExecutionStatus(eq(executionID), any(HttpBasicAuth.class))).thenReturn(endReport);
        ExecutionListener executionListener = mock(ExecutionListener.class);


        projectExecutor.addExecutionListener(executionListener);
        projectExecutor.submitRepositoryProject(executionRequest);

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

    private RepositoryProjectExecutionRequest createRepositoryProjectExecutionRequest() {
        return RepositoryProjectExecutionRequest.Builder.forProject("Environment-test.xml")
                .fromRepository("compositeprojects")
                .forEnvironment("staging")
                .forTestSuite("TestSuite-1")
                .forTestCase("TestCase-1")
                .build();
    }
}
