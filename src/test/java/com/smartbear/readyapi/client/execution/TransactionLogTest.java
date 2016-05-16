package com.smartbear.readyapi.client.execution;

import com.smartbear.readyapi.client.TestRecipe;
import com.smartbear.readyapi.client.model.HarLogRoot;
import com.smartbear.readyapi.client.model.ProjectResultReport;
import com.smartbear.readyapi.client.model.TestCase;
import io.swagger.client.auth.HttpBasicAuth;
import io.swagger.util.Json;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;

import static com.smartbear.readyapi.client.execution.ExecutionTestHelper.makeFinishedReport;
import static com.smartbear.readyapi.client.execution.ExecutionTestHelper.makeRunningReport;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TransactionLogTest {

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
    public void getsExecutionLog() throws Exception {
        String executionID = "the_id";
        ProjectResultReport startReport = makeRunningReport(executionID);
        ProjectResultReport endReport = makeFinishedReport(executionID);
        when(apiWrapper.postTestRecipe(eq(recipeToSubmit.getTestCase()), eq(true), any(HttpBasicAuth.class))).thenReturn(startReport);
        when(apiWrapper.getExecutionStatus(eq(executionID), any(HttpBasicAuth.class))).thenReturn(endReport);

        HarLogRoot harLog = Json.mapper().readValue(new FileInputStream("src/test/resources/har-log.json"), HarLogRoot.class);
        when(apiWrapper.getTransactionLog(eq(executionID), eq("1463064414287"), any(HttpBasicAuth.class))).thenReturn(harLog);

        Execution execution = executor.submitRecipe(recipeToSubmit);
        HarLogRoot transactionLog = executor.getTransactionLog(execution, "1463064414287");
        assertThat(transactionLog, is(harLog));
        System.out.println(transactionLog);
    }
}
