package com.smartbear.readyapi4j.liveserver;

import com.smartbear.readyapi.client.model.ProjectResultReport;
import com.smartbear.readyapi.client.model.TestCaseResultReport;
import com.smartbear.readyapi.client.model.TestStepResultReport;
import com.smartbear.readyapi.client.model.TestSuiteResultReport;
import com.smartbear.readyapi4j.ExecutionListener;
import com.smartbear.readyapi4j.TestRecipe;
import com.smartbear.readyapi4j.TestRecipeBuilder;
import com.smartbear.readyapi4j.execution.Execution;
import com.smartbear.readyapi4j.testserver.execution.TestServerClient;
import com.smartbear.readyapi4j.testserver.execution.TestServerRecipeExecutor;
import com.smartbear.readyapi4j.teststeps.TestStepBuilder;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.util.Collection;
import java.util.Optional;

import static com.smartbear.readyapi4j.extractor.Extractors.pathExtractor;
import static com.smartbear.readyapi4j.extractor.Extractors.propertyExtractor;
import static com.smartbear.readyapi4j.teststeps.TestSteps.getRequest;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class RestRequestLiveServerTest {
    private static Logger logger = LoggerFactory.getLogger(RestRequestLiveServerTest.class);
    private static final String ENDPOINT = "http://api.swaggerhub.com";
    private static final String ENDPOINT_WITH_PATH = ENDPOINT + "/apis";
    private static final String ENDPOINT_NAME ="Default listing";
    private static final String ENDPOINT_DESCRIPTION = "Default registry listing";
    private static final String TESTSERVER_URL = "http://testserver.readyapi.io:8080";
    private static final String TESTSERVER_USER = "demoUser";
    private static final String TESTSERVER_PASSWORD = "demoPassword";
    private static final String REST_REQUEST_NAME = "swaggerhubrequest";
    private static final String SECOND_REST_REQUEST_NAME = "secondswaggerhubrequest";

    private TestServerClient testServerClient;

    @Before
    public void setup() throws MalformedURLException {
        testServerClient = TestServerClient.fromUrl(TESTSERVER_URL)
                .withCredentials(TESTSERVER_USER, TESTSERVER_PASSWORD);
    }

    @Test
    public void sendRequestWithPathExtractor(){
        final String[] extractedProperty = {""};
        TestRecipe testRecipe = createTestRecipe(
                getRequest(ENDPOINT_WITH_PATH)
                        .named(REST_REQUEST_NAME)
                        .withExtractors(pathExtractor("$.name", property -> extractedProperty[0] = property)));
        Execution execution = testServerClient.createRecipeExecutor().executeRecipe(testRecipe);
        Optional<TestStepResultReport> report  = extractTestStepResultReport(execution.getCurrentReport());
        if(report.isPresent()){
            assertThat(report.get().getAssertionStatus(), is(TestStepResultReport.AssertionStatusEnum.UNKNOWN));
        }
        assertThat(extractedProperty[0], is(ENDPOINT_NAME));
    }

    @Test
    public void sendRequestWithPropertyExtractor(){
        final String[] extractedProperty = {""};
        TestRecipe testRecipe = createTestRecipe(
                getRequest(ENDPOINT_WITH_PATH)
                        .named(REST_REQUEST_NAME)
                        .withExtractors(propertyExtractor("Endpoint", property -> extractedProperty[0] = property)));
        Execution execution = testServerClient.createRecipeExecutor().executeRecipe(testRecipe);
        Optional<TestStepResultReport> report  = extractTestStepResultReport(execution.getCurrentReport());
        if(report.isPresent()){
            assertThat(report.get().getAssertionStatus(), is(TestStepResultReport.AssertionStatusEnum.UNKNOWN));
        }
        assertThat(extractedProperty[0], is(ENDPOINT));
    }

    @Test
    public void sendRequestWithSeveralPathExtractors(){
        final String[] extractedProperty = {"",""};
        TestRecipe testRecipe = createTestRecipe(
                getRequest(ENDPOINT_WITH_PATH)
                        .named(REST_REQUEST_NAME)
                        .withExtractors(pathExtractor("$.name", property -> extractedProperty[0] = property),
                                pathExtractor("$.description", property -> extractedProperty[1] = property)));
        Execution execution = testServerClient.createRecipeExecutor().executeRecipe(testRecipe);
        Optional<TestStepResultReport> report  = extractTestStepResultReport(execution.getCurrentReport());
        if(report.isPresent()){
            assertThat(report.get().getAssertionStatus(), is(TestStepResultReport.AssertionStatusEnum.UNKNOWN));
        }
        assertThat(extractedProperty[0], is(ENDPOINT_NAME));
        assertThat(extractedProperty[1], is(ENDPOINT_DESCRIPTION));
    }

    @Test
    public void sendRequestWithSeveralPropertyExtractors(){
        final String[] extractedProperty = {"",""};
        TestRecipe testRecipe = createTestRecipe(
                getRequest(ENDPOINT_WITH_PATH)
                        .named(REST_REQUEST_NAME)
                        .withExtractors(propertyExtractor("Endpoint", property -> extractedProperty[0] = property),
                                propertyExtractor("Response", property -> extractedProperty[1] = property)));
        Execution execution = testServerClient.createRecipeExecutor().executeRecipe(testRecipe);
        Optional<TestStepResultReport> report  = extractTestStepResultReport(execution.getCurrentReport());
        if(report.isPresent()){
            assertThat(report.get().getAssertionStatus(), is(TestStepResultReport.AssertionStatusEnum.UNKNOWN));
        }
        assertThat(extractedProperty[0], is(ENDPOINT));
        assertThat(extractedProperty[1], containsString(ENDPOINT_NAME));
        assertThat(extractedProperty[1], containsString(ENDPOINT_DESCRIPTION));
    }

    @Test
    public void sendRequestWithPathAndPropertyExtractors(){
        final String[] extractedProperty = {"",""};
        TestRecipe testRecipe = createTestRecipe(
                getRequest(ENDPOINT_WITH_PATH)
                        .named(REST_REQUEST_NAME)
                        .withExtractors(propertyExtractor("Endpoint", property -> extractedProperty[0] = property),
                                pathExtractor("$.name", property -> extractedProperty[1] = property)));
        Execution execution = testServerClient.createRecipeExecutor().executeRecipe(testRecipe);
        Optional<TestStepResultReport> report  = extractTestStepResultReport(execution.getCurrentReport());
        if(report.isPresent()){
            assertThat(report.get().getAssertionStatus(), is(TestStepResultReport.AssertionStatusEnum.UNKNOWN));
        }
        assertThat(extractedProperty[0], is(ENDPOINT));
        assertThat(extractedProperty[1], is(ENDPOINT_NAME));
    }

    @Test
    public void sendSeveralRequestsWithExtractors(){
        final String[] extractedProperty = {"",""};
        TestRecipe testRecipe = createTestRecipe(
                getRequest(ENDPOINT_WITH_PATH)
                        .named(REST_REQUEST_NAME)
                        .withExtractors(propertyExtractor("Endpoint", property -> extractedProperty[0] = property)),
                getRequest(ENDPOINT_WITH_PATH)
                        .named(SECOND_REST_REQUEST_NAME)
                        .withExtractors(pathExtractor("$.name", property -> extractedProperty[1] = property)));

        Execution execution = testServerClient.createRecipeExecutor().executeRecipe(testRecipe);
        Optional<TestStepResultReport> report  = extractTestStepResultReport(execution.getCurrentReport());
        if(report.isPresent()){
            assertThat(report.get().getAssertionStatus(), is(TestStepResultReport.AssertionStatusEnum.UNKNOWN));
        }
        assertThat(extractedProperty[0], is(ENDPOINT));
        assertThat(extractedProperty[1], is(ENDPOINT_NAME));
    }

    @Test
    public void sendSeveralRequestWithExtractorsAsync(){
        final String[] extractedProperty = {"",""};
        TestRecipe testRecipe1 = createTestRecipe(
                getRequest(ENDPOINT_WITH_PATH)
                        .named(REST_REQUEST_NAME)
                        .withExtractors(propertyExtractor("Endpoint", property -> extractedProperty[0] = property)));
        TestRecipe testRecipe2 = createTestRecipe(
                getRequest(ENDPOINT_WITH_PATH)
                        .named(SECOND_REST_REQUEST_NAME)
                        .withExtractors(pathExtractor("$.name", property -> extractedProperty[1] = property)));

        ExecutionListener listener = new ExecutionListener() {
            private String executionID;

            @Override
            public void executionStarted(ProjectResultReport projectResultReport) {
                executionID = projectResultReport.getExecutionID();
                logger.info("Started execution of " + executionID);
            }

            @Override
            public void executionFinished(ProjectResultReport projectResultReport) {
                Optional<TestStepResultReport> report  = extractTestStepResultReport(projectResultReport);
                if(report.isPresent()){
                    assertThat(report.get().getAssertionStatus(), is(TestStepResultReport.AssertionStatusEnum.UNKNOWN));
                }
                if(report.get().getTestStepName().equals(REST_REQUEST_NAME)) {
                    assertThat(extractedProperty[0], is(ENDPOINT));
                } else {
                    assertThat(extractedProperty[1], is(ENDPOINT_NAME));
                }
            }

            @Override
            public void errorOccurred(Exception exception) {
                logger.error("Exception occurred on " + executionID + "with message: " + exception.getMessage());
            }
        };

        TestServerRecipeExecutor executor = testServerClient.createRecipeExecutor();
        executor.submitRecipe(testRecipe1);
        executor.submitRecipe(testRecipe2);
        executor.addExecutionListener(listener);
    }

    private Optional<TestStepResultReport> extractTestStepResultReport(ProjectResultReport projectResultReport){
        Optional<TestStepResultReport> result = Optional.empty();
        TestCaseResultReport report = projectResultReport
                .getTestSuiteResultReports()
                .stream()
                .map(TestSuiteResultReport::getTestCaseResultReports)
                .flatMap(Collection::stream)
                .findAny()
                .orElse(null);
        if(report != null && !report.getTestStepResultReports().isEmpty()) {
            result = Optional.ofNullable(report.getTestStepResultReports().get(0));
        }
        return result;
    }

    private TestRecipe createTestRecipe(TestStepBuilder... testStepBuilders) {
        return TestRecipeBuilder.newTestRecipe(testStepBuilders).buildTestRecipe();
    }
}