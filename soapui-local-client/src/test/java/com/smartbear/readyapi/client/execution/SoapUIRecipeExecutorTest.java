package com.smartbear.readyapi.client.execution;

import com.google.gson.Gson;
import com.smartbear.readyapi.client.TestRecipe;
import com.smartbear.readyapi.client.model.HarResponse;
import com.smartbear.readyapi.client.model.ProjectResultReport;
import com.smartbear.readyapi.client.teststeps.propertytransfer.PathLanguage;
import com.smartbear.readyapi.util.rest.JsonTestObject;
import com.smartbear.readyapi.util.rest.Pair;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static com.smartbear.readyapi.client.TestRecipeBuilder.newTestRecipe;
import static com.smartbear.readyapi.client.teststeps.TestSteps.getRequest;
import static com.smartbear.readyapi.client.teststeps.TestSteps.groovyScriptStep;
import static com.smartbear.readyapi.client.teststeps.TestSteps.postRequest;
import static com.smartbear.readyapi.client.teststeps.TestSteps.propertyTransfer;
import static com.smartbear.readyapi.client.teststeps.propertytransfer.PropertyTransferBuilder.from;
import static com.smartbear.readyapi.client.teststeps.propertytransfer.PropertyTransferSourceBuilder.aSource;
import static com.smartbear.readyapi.client.teststeps.propertytransfer.PropertyTransferTargetBuilder.aTarget;
import static com.smartbear.readyapi.util.rest.local.LocalServerUtil.addGetToLocalServer;
import static com.smartbear.readyapi.util.rest.local.LocalServerUtil.addPostToLocalServer;
import static com.smartbear.readyapi.util.rest.local.LocalServerUtil.getPostedJsonTestObject;
import static com.smartbear.readyapi.util.rest.local.LocalServerUtil.startLocalServer;
import static com.smartbear.readyapi.util.rest.local.LocalServerUtil.stopLocalServer;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

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
        Execution execution = executor.postTestCase(testRecipe.getTestCase(), false);
        assertThat(execution.getId(), is(not(nullValue())));
        assertThat(execution.getCurrentStatus(), is(ProjectResultReport.StatusEnum.FINISHED));
    }

    @Test
    public void runsRestGetRequestJson() throws Exception {
        TestRecipe testRecipe = newTestRecipe(
                getRequest(serverURL)
                        .acceptsJson()
                        .assertJsonContent(ASSERTION_KEY, ASSERTION_TEST_VALUE)
        ).buildTestRecipe();
        Execution execution = executor.postTestCase(testRecipe.getTestCase(), false);
        assertThat(execution.getId(), is(not(nullValue())));
        assertThat(execution.getCurrentStatus(), is(ProjectResultReport.StatusEnum.FINISHED));

        HarResponse harResponse = execution.getExecutionResult().getTestStepResult(0).getHarResponse();
        assertThat(harResponse, is(not(nullValue())));
        assertEquals("{\"message\":\"Hello World\"}", harResponse.getContent().getText());
    }

    @Test
    public void runsPropertyTransferRequest() {
        TestRecipe testRecipe = newTestRecipe(
                getRequest(serverURL)
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
                getRequest(BOGUS_URL)
                        .named(REST_TARGET)
                        .acceptsJson()
                        .assertJsonContent(ASSERTION_KEY, ASSERTION_ROOT_VALUE)
        ).buildTestRecipe();
        Execution execution = executor.postTestCase(testRecipe.getTestCase(), false);
        assertThat(execution.getId(), is(not(nullValue())));
        assertThat(execution.getCurrentStatus(), is(ProjectResultReport.StatusEnum.FINISHED));
    }

    @Test
    public void runsPropertyTransferRequestWithJsonPathExtraction() {
        TestRecipe testRecipe = newTestRecipe(
                getRequest(jsonURL)
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
                postRequest(BOGUS_URL)
                        .named(REST_TARGET)
                        .acceptsJson()
                        .withRequestBody(new Gson().toJson(new JsonTestObject(ASSERTION_TEST_VALUE, serverURL)))
        ).buildTestRecipe();
        Execution execution = executor.postTestCase(testRecipe.getTestCase(), false);
        assertThat(execution.getId(), is(not(nullValue())));
        assertThat(execution.getCurrentStatus(), is(ProjectResultReport.StatusEnum.FINISHED));
        assertThat(getPostedJsonTestObject(), is(testObject));
    }
}
