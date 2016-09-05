package com.smartbear.readyapi.client.execution;

import com.smartbear.readyapi.client.TestRecipe;
import com.smartbear.readyapi.client.model.HarLogRoot;
import com.smartbear.readyapi.client.model.ProjectResultReport;
import com.smartbear.readyapi.client.model.TestCase;
import io.swagger.client.auth.HttpBasicAuth;
import io.swagger.util.Json;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.FileInputStream;

import static com.smartbear.readyapi.client.TestRecipeBuilder.newTestRecipe;
import static com.smartbear.readyapi.client.execution.ExecutionTestHelper.makeFinishedReport;
import static com.smartbear.readyapi.client.execution.ExecutionTestHelper.makeRunningReport;
import static com.smartbear.readyapi.client.teststeps.TestSteps.getRequest;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

public class TransactionLogTest extends ProjectExecutionTestBase {

    private TestRecipe recipeToSubmit;

    @Before
    public void setUp() throws Exception {
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

        Execution execution = recipeExecutor.submitRecipe(recipeToSubmit);
        HarLogRoot transactionLog = testServerClient.getTransactionLog(execution, "1463064414287");
        assertThat(transactionLog, is(harLog));
    }

    @Ignore("Manual test to get the transaction log from real test server")
    @Test
    public void getsExecutionLogFromTestServer() throws Exception {
        TestServerClient testServerClient = new TestServerClient(Scheme.HTTP, "localhost", 8080);
        testServerClient.setCredentials("prakash", "password");

        TestRecipe recipe = newTestRecipe()
                .addStep(getRequest("http://maps.googleapis.com/maps/api/geocode/xml")
                        .named("Rest Request")
                        .assertJsonContent("$.results[0].address_components[1].long_name", "Amphitheatre Parkway")
                        .assertValidStatusCodes(200)
                        .withClientCertificate("C:\\cygwin64\\home\\Prakash\\ClientCertificateStore.jks")
                        .withClientCertificatePassword("password")
                )
                .buildTestRecipe();

        RecipeExecutor recipeExecutor = testServerClient.createRecipeExecutor();
        Execution execution = recipeExecutor.executeRecipe(recipe);
        String transactionId = execution.getCurrentReport().getTestSuiteResultReports().get(0).getTestCaseResultReports().get(0).getTestStepResultReports().get(0).getTransactionId();
        HarLogRoot transactionLog = testServerClient.getTransactionLog(execution, transactionId);
        assertThat(transactionLog.getLog().getEntries().size(), is(1));
    }
}
