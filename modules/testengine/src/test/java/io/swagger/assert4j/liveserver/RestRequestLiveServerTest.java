package io.swagger.assert4j.liveserver;

import com.google.code.tempusfugit.temporal.Duration;
import io.swagger.assert4j.TestRecipe;
import io.swagger.assert4j.TestRecipeBuilder;
import io.swagger.assert4j.client.model.TestCaseResultReport;
import io.swagger.assert4j.client.model.TestJobReport;
import io.swagger.assert4j.client.model.TestStepResultReport;
import io.swagger.assert4j.client.model.TestSuiteResultReport;
import io.swagger.assert4j.execution.Execution;
import io.swagger.assert4j.execution.ExecutionListener;
import io.swagger.assert4j.testserver.execution.TestServerClient;
import io.swagger.assert4j.testserver.execution.TestServerRecipeExecutor;
import io.swagger.assert4j.teststeps.TestStepBuilder;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeoutException;

import static com.google.code.tempusfugit.concurrency.CountDownLatchWithTimeout.await;
import static io.swagger.assert4j.client.model.TestStepResultReport.AssertionStatusEnum.UNKNOWN;
import static io.swagger.assert4j.extractor.Extractors.fromProperty;
import static io.swagger.assert4j.extractor.Extractors.fromResponse;
import static io.swagger.assert4j.teststeps.TestSteps.GET;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@Ignore
public class RestRequestLiveServerTest {
    private static Logger logger = LoggerFactory.getLogger(RestRequestLiveServerTest.class);
    private static final String ENDPOINT = "https://api.swaggerhub.com";
    private static final String ENDPOINT_WITH_PATH = ENDPOINT + "/specs?specType=API&limit=10";
    private static final String ENDPOINT_NAME = "Default listing";
    private static final String ENDPOINT_DESCRIPTION = "Default registry listing";
    private static final String TESTSERVER_URL = "http://testserver.readyapi.io:8080";
    private static final String TESTSERVER_USER = "demoUser";
    private static final String TESTSERVER_PASSWORD = "demoPassword";
    private static final String REST_REQUEST_NAME = "swaggerhubrequest";
    private static final String SECOND_REST_REQUEST_NAME = "secondswaggerhubrequest";

    private static final Duration LATCH_TIMEOUT = Duration.millis(5000);
    private final CountDownLatch waitForExecution = new CountDownLatch(2);


    private TestServerClient testServerClient;

    @Before
    public void setup() throws MalformedURLException {
        testServerClient = TestServerClient.fromUrl(TESTSERVER_URL)
                .withCredentials(TESTSERVER_USER, TESTSERVER_PASSWORD);
    }

    @Test
    public void sendRequestWithPathExtractor() {
        final String[] extractedProperty = {""};
        TestRecipe testRecipe = createTestRecipe(
                GET(ENDPOINT_WITH_PATH)
                        .named(REST_REQUEST_NAME)
                        .withExtractors(fromResponse("$.name", property -> extractedProperty[0] = property)));
        Execution execution = testServerClient.createRecipeExecutor().executeRecipe(testRecipe);
        Optional<TestStepResultReport> report = extractTestStepResultReport(execution.getCurrentReport());
        report.ifPresent(testStepResultReport -> assertThat(testStepResultReport.getAssertionStatus(), is(UNKNOWN)));
        assertThat(extractedProperty[0], is(ENDPOINT_NAME));
    }

    @Test
    public void sendRequestWithPropertyExtractor() {
        final String[] extractedProperty = {""};
        TestRecipe testRecipe = createTestRecipe(
                GET(ENDPOINT_WITH_PATH)
                        .named(REST_REQUEST_NAME)
                        .withExtractors(fromProperty("Endpoint", property -> extractedProperty[0] = property)));
        Execution execution = testServerClient.createRecipeExecutor().executeRecipe(testRecipe);
        Optional<TestStepResultReport> report = extractTestStepResultReport(execution.getCurrentReport());
        report.ifPresent(testStepResultReport -> assertThat(testStepResultReport.getAssertionStatus(), is(UNKNOWN)));
        assertThat(extractedProperty[0], is(ENDPOINT));
    }

    @Test
    public void sendRequestWithSeveralPathExtractors() {
        final String[] extractedProperty = {"", ""};
        TestRecipe testRecipe = createTestRecipe(
                GET(ENDPOINT_WITH_PATH)
                        .named(REST_REQUEST_NAME)
                        .withExtractors(fromResponse("$.name", property -> extractedProperty[0] = property),
                                fromResponse("$.description", property -> extractedProperty[1] = property)));
        Execution execution = testServerClient.createRecipeExecutor().executeRecipe(testRecipe);
        Optional<TestStepResultReport> report = extractTestStepResultReport(execution.getCurrentReport());
        report.ifPresent(testStepResultReport -> assertThat(testStepResultReport.getAssertionStatus(), is(UNKNOWN)));
        assertThat(extractedProperty[0], is(ENDPOINT_NAME));
        assertThat(extractedProperty[1], is(ENDPOINT_DESCRIPTION));
    }

    @Test
    public void sendRequestWithSeveralPropertyExtractors() {
        final String[] extractedProperty = {"", ""};
        TestRecipe testRecipe = createTestRecipe(
                GET(ENDPOINT_WITH_PATH)
                        .named(REST_REQUEST_NAME)
                        .withExtractors(fromProperty("Endpoint", property -> extractedProperty[0] = property),
                                fromProperty("Response", property -> extractedProperty[1] = property)));
        Execution execution = testServerClient.createRecipeExecutor().executeRecipe(testRecipe);
        Optional<TestStepResultReport> report = extractTestStepResultReport(execution.getCurrentReport());
        report.ifPresent(testStepResultReport -> assertThat(testStepResultReport.getAssertionStatus(), is(UNKNOWN)));
        assertThat(extractedProperty[0], is(ENDPOINT));
        assertThat(extractedProperty[1], containsString(ENDPOINT_NAME));
        assertThat(extractedProperty[1], containsString(ENDPOINT_DESCRIPTION));
    }

    @Test
    public void sendRequestWithPathAndPropertyExtractors() {
        final String[] extractedProperty = {"", ""};
        TestRecipe testRecipe = createTestRecipe(
                GET(ENDPOINT_WITH_PATH)
                        .named(REST_REQUEST_NAME)
                        .withExtractors(fromProperty("Endpoint", property -> extractedProperty[0] = property),
                                fromResponse("$.name", property -> extractedProperty[1] = property)));
        Execution execution = testServerClient.createRecipeExecutor().executeRecipe(testRecipe);
        Optional<TestStepResultReport> report = extractTestStepResultReport(execution.getCurrentReport());
        report.ifPresent(testStepResultReport -> assertThat(testStepResultReport.getAssertionStatus(), is(UNKNOWN)));
        assertThat(extractedProperty[0], is(ENDPOINT));
        assertThat(extractedProperty[1], is(ENDPOINT_NAME));
    }

    @Test
    public void sendSeveralRequestsWithExtractors() {
        final String[] extractedProperty = {"", ""};
        TestRecipe testRecipe = createTestRecipe(
                GET(ENDPOINT_WITH_PATH)
                        .named(REST_REQUEST_NAME)
                        .withExtractors(fromProperty("Endpoint", property -> extractedProperty[0] = property)),
                GET(ENDPOINT_WITH_PATH)
                        .named(SECOND_REST_REQUEST_NAME)
                        .withExtractors(fromResponse("$.name", property -> extractedProperty[1] = property)));

        Execution execution = testServerClient.createRecipeExecutor().executeRecipe(testRecipe);
        Optional<TestStepResultReport> report = extractTestStepResultReport(execution.getCurrentReport());
        report.ifPresent(testStepResultReport -> assertThat(testStepResultReport.getAssertionStatus(), is(UNKNOWN)));
        assertThat(extractedProperty[0], is(ENDPOINT));
        assertThat(extractedProperty[1], is(ENDPOINT_NAME));
    }

    @Test
    @Ignore
    public void sendSeveralRequestWithExtractorsAsync() throws TimeoutException, InterruptedException {
        final String[] extractedProperty = {"", ""};
        TestRecipe testRecipe1 = createTestRecipe(
                GET(ENDPOINT_WITH_PATH)
                        .named(REST_REQUEST_NAME)
                        .withExtractors(fromProperty("Endpoint", property -> extractedProperty[0] = property)));
        TestRecipe testRecipe2 = createTestRecipe(
                GET(ENDPOINT_WITH_PATH)
                        .named(SECOND_REST_REQUEST_NAME)
                        .withExtractors(fromResponse("$.name", property -> extractedProperty[1] = property)));

        ExecutionListener listener = new ExecutionListener() {
            private String executionID;

            @Override
            public void executionStarted(Execution execution) {
                executionID = execution.getId();
                logger.info("Started execution of " + executionID);
            }

            @Override
            public void executionFinished(Execution execution) {
                TestJobReport projectResultReport = execution.getCurrentReport();
                Optional<TestStepResultReport> report = extractTestStepResultReport(projectResultReport);
                report.ifPresent(testStepResultReport -> {
                    assertThat(testStepResultReport.getAssertionStatus(), is(UNKNOWN));
                    if (report.get().getTestStepName().equals(REST_REQUEST_NAME)) {
                        assertThat(extractedProperty[0], is(ENDPOINT));
                    } else {
                        assertThat(extractedProperty[1], is(ENDPOINT_NAME));
                    }
                });

                waitForExecution.countDown();
            }

            @Override
            public void errorOccurred(Exception exception) {
                logger.error("Exception occurred on " + executionID + "with message: " + exception.getMessage());
            }
        };

        TestServerRecipeExecutor executor = testServerClient.createRecipeExecutor();
        executor.addExecutionListener(listener);
        executor.submitRecipe(testRecipe1);
        executor.submitRecipe(testRecipe2);
        await(waitForExecution).with(LATCH_TIMEOUT);
    }

    private Optional<TestStepResultReport> extractTestStepResultReport(TestJobReport projectResultReport) {
        Optional<TestStepResultReport> result = Optional.empty();
        TestCaseResultReport report = projectResultReport
                .getTestSuiteResultReports()
                .stream()
                .map(TestSuiteResultReport::getTestCaseResultReports)
                .flatMap(Collection::stream)
                .findAny()
                .orElse(null);
        if (report != null && !report.getTestStepResultReports().isEmpty()) {
            result = Optional.ofNullable(report.getTestStepResultReports().get(0));
        }
        return result;
    }

    private TestRecipe createTestRecipe(TestStepBuilder... testStepBuilders) {
        return TestRecipeBuilder.newTestRecipe(testStepBuilders).buildTestRecipe();
    }
}