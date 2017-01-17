package com.smartbear.readyapi4j.local.execution;

import com.google.gson.Gson;
import com.smartbear.readyapi.client.model.HarResponse;
import com.smartbear.readyapi.client.model.ProjectResultReport;
import com.smartbear.readyapi.util.rest.JsonTestObject;
import com.smartbear.readyapi.util.rest.Pair;
import com.smartbear.readyapi4j.ExecutionListener;
import com.smartbear.readyapi4j.TestRecipe;
import com.smartbear.readyapi4j.execution.Execution;
import com.smartbear.readyapi4j.teststeps.propertytransfer.PathLanguage;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static com.smartbear.readyapi.util.rest.local.LocalServerUtil.addGetToLocalServer;
import static com.smartbear.readyapi.util.rest.local.LocalServerUtil.addPostToLocalServer;
import static com.smartbear.readyapi.util.rest.local.LocalServerUtil.getPostedJsonTestObject;
import static com.smartbear.readyapi.util.rest.local.LocalServerUtil.startLocalServer;
import static com.smartbear.readyapi.util.rest.local.LocalServerUtil.stopLocalServer;
import static com.smartbear.readyapi4j.TestRecipeBuilder.newTestRecipe;
import static com.smartbear.readyapi4j.teststeps.TestSteps.GET;
import static com.smartbear.readyapi4j.teststeps.TestSteps.POST;
import static com.smartbear.readyapi4j.teststeps.TestSteps.groovyScriptStep;
import static com.smartbear.readyapi4j.teststeps.TestSteps.propertyTransfer;
import static com.smartbear.readyapi4j.teststeps.propertytransfer.PropertyTransferBuilder.from;
import static com.smartbear.readyapi4j.teststeps.propertytransfer.PropertyTransferSourceBuilder.aSource;
import static com.smartbear.readyapi4j.teststeps.propertytransfer.PropertyTransferTargetBuilder.aTarget;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

public class SoapUIRecipeExecutorTest {
    private static final String REST_SOURCE = "SourceStep";
    private static final String REST_TARGET = "TargetStep";
    private static final String PROPERTY_ENDPOINT = "endpoint";
    private static final String PROPERTY_RESPONSE = "response";
    private static final String PROPERTY_REQUEST = "request";
    private static final String BOGUS_URL = "http://bogus.doesnotexist";
    private static final String ASSERTION_KEY = "message";
    private static final String ASSERTION_TEST_VALUE = "Hello World";
    private static final String ASSERTION_ROOT_VALUE = "Root World";
    private static final String ASSERTION_JSON_VALUE = "Json World";
    private static final String JSON_PATH_ALTERNATE = "$.alternatePath";
    private static final String JSON_PATH_MESSAGE = "$.message";

    private SoapUIRecipeExecutor executor = new SoapUIRecipeExecutor();
    private static String serverURL;
    private static String jsonURL;
    private static JsonTestObject testObject;

    @BeforeClass
    public static void setup() {
        String testPath = "/test";
        String rootPath = "/";
        int port = 8080;
        port = startLocalServer(port,
                new Pair<>(testPath, ASSERTION_TEST_VALUE),
                new Pair<>(rootPath, ASSERTION_ROOT_VALUE));
        serverURL = "http://localhost:" + port + testPath;
        String jsonPath = "/json";
        testObject = new JsonTestObject(ASSERTION_JSON_VALUE, serverURL);
        addGetToLocalServer(jsonPath, testObject);
        jsonURL = "http://localhost:" + port + jsonPath;
        addPostToLocalServer(testPath);
    }

    @AfterClass
    public static void cleanUp() {
        stopLocalServer();
    }

    @Test
    public void runsMinimalProject() throws Exception {
        TestRecipe testRecipe = newTestRecipe(
                groovyScriptStep("println 'Hello Earth'")
        ).buildTestRecipe();
        Execution execution = executor.executeRecipe(testRecipe);
        assertThat(execution.getId(), is(not(nullValue())));
        assertThat(execution.getCurrentStatus(), is(ProjectResultReport.StatusEnum.FINISHED));
    }

    @Test
    public void runsRestGetRequestJson() throws Exception {
        TestRecipe testRecipe = newTestRecipe(
                GET(serverURL)
                        .acceptsJson()
                        .assertJsonContent(ASSERTION_KEY, ASSERTION_TEST_VALUE)
        ).buildTestRecipe();
        Execution execution = executor.executeRecipe(testRecipe);
        assertThat(execution.getId(), is(not(nullValue())));
        assertThat(execution.getCurrentStatus(), is(ProjectResultReport.StatusEnum.FINISHED));

        HarResponse harResponse = execution.getExecutionResult().getTestStepResult(0).getHarResponse();
        assertThat(harResponse, is(not(nullValue())));
        assertEquals("{\"message\":\"Hello World\"}", harResponse.getContent().getText());
    }

    @Test
    public void runsPropertyTransferRequest() {
        TestRecipe testRecipe = newTestRecipe(
                GET(serverURL)
                        .named(REST_SOURCE)
                        .acceptsJson()
                        .assertJsonContent(ASSERTION_KEY, ASSERTION_TEST_VALUE),
                propertyTransfer()
                        .addTransfer(from(aSource()
                                .withSourceStep(REST_SOURCE)
                                .withProperty(PROPERTY_ENDPOINT))
                                .to(aTarget()
                                        .withTargetStep(REST_TARGET)
                                        .withProperty(PROPERTY_ENDPOINT))),
                GET(BOGUS_URL)
                        .named(REST_TARGET)
                        .acceptsJson()
                        .assertJsonContent(ASSERTION_KEY, ASSERTION_ROOT_VALUE)
        ).buildTestRecipe();
        Execution execution = executor.executeRecipe(testRecipe);
        assertThat(execution.getId(), is(not(nullValue())));
        assertThat(execution.getCurrentStatus(), is(ProjectResultReport.StatusEnum.FINISHED));
    }

    @Test
    public void runsPropertyTransferRequestWithJsonPathExtraction() {
        TestRecipe testRecipe = buildPropertyTransferWithJsonPathExtractionTestRecipe();
        Execution execution = executor.executeRecipe(testRecipe);
        assertThat(execution.getId(), is(not(nullValue())));
        assertThat(execution.getCurrentStatus(), is(ProjectResultReport.StatusEnum.FINISHED));
        assertThat(getPostedJsonTestObject(), is(testObject));
    }

    @Test
    public void runsAsyncPropertyTransferRequestWithJsonPathExtraction() {
        TestRecipe testRecipe = buildPropertyTransferWithJsonPathExtractionTestRecipe();

        ExecutionListener listenerMock = mock(ExecutionListener.class);
        executor.addExecutionListener(listenerMock);
        Execution execution = executor.submitRecipe(testRecipe);
        assertThat(execution.getId(), is(not(nullValue())));
        assertThat(execution.getCurrentStatus(), is(ProjectResultReport.StatusEnum.RUNNING));

        verify(listenerMock, timeout(1000).times(1)).executionStarted(any());
        verify(listenerMock, timeout(20000).times(1)).executionFinished(any());
    }

    private TestRecipe buildPropertyTransferWithJsonPathExtractionTestRecipe() {
        return newTestRecipe(
                GET(jsonURL)
                        .named(REST_SOURCE)
                        .acceptsJson()
                        .assertJsonContent(ASSERTION_KEY, ASSERTION_JSON_VALUE),
                propertyTransfer()
                        .addTransfer(from(aSource()
                                .withSourceStep(REST_SOURCE)
                                .withProperty(PROPERTY_RESPONSE)
                                .withPathLanguage(PathLanguage.JSONPath)
                                .withPath(JSON_PATH_ALTERNATE))
                                .to(aTarget()
                                        .withTargetStep(REST_TARGET)
                                        .withProperty(PROPERTY_ENDPOINT)))
                        .addTransfer(from(aSource()
                                .withSourceStep(REST_SOURCE)
                                .withProperty(PROPERTY_RESPONSE)
                                .withPathLanguage(PathLanguage.JSONPath)
                                .withPath(JSON_PATH_MESSAGE))
                                .to(aTarget()
                                        .withTargetStep(REST_TARGET)
                                        .withPathLanguage(PathLanguage.JSONPath)
                                        .withPath(JSON_PATH_MESSAGE)
                                        .withProperty(PROPERTY_REQUEST))),
                POST(BOGUS_URL)
                        .named(REST_TARGET)
                        .acceptsJson()
                        .withRequestBody(new Gson().toJson(new JsonTestObject(ASSERTION_TEST_VALUE, serverURL)))
        ).buildTestRecipe();
    }
}
