package io.swagger.assert4j.testengine.execution;

import io.swagger.assert4j.HttpBasicAuth;
import io.swagger.assert4j.client.model.HarLogRoot;
import io.swagger.assert4j.client.model.TestJobReport;
import io.swagger.assert4j.execution.Execution;
import io.swagger.assert4j.result.RecipeExecutionResult;
import io.swagger.util.Json;
import org.junit.Test;
import org.mockito.Matchers;

import java.io.FileInputStream;
import java.io.IOException;

import static io.swagger.assert4j.client.model.TestStepResultReport.AssertionStatusEnum.FAIL;
import static io.swagger.assert4j.client.model.TestStepResultReport.AssertionStatusEnum.PASS;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class ProjectExecutionResultTestCase {

    @Test
    public void testExecutionResult() throws IOException {

        TestJobReport resultReport =
                Json.mapper().readValue(new FileInputStream("src/test/resources/testjob-report.json"),
                        TestJobReport.class);

        HarLogRoot harLogRoot =
                Json.mapper().readValue(new FileInputStream("src/test/resources/single-entry-har-log.json"),
                        HarLogRoot.class);

        TestEngineApi apiMock = mock(TestEngineApi.class);
        when(apiMock.getTransactionLog(Matchers.anyString(), Matchers.anyString(), (HttpBasicAuth) Matchers.any())).thenReturn(harLogRoot);

        Execution execution = new TestEngineExecution(apiMock, new HttpBasicAuth(), resultReport);
        RecipeExecutionResult result = execution.getExecutionResult();

        assertEquals(TestJobReport.StatusEnum.FINISHED, result.getStatus());
        assertEquals(215, result.getTimeTaken());
        assertEquals("93ddece4-53b3-4f23-9bb0-f5db6ed6b9ef", result.getExecutionId());
        assertEquals(3, result.getResultCount());
        assertEquals(3, result.getTestStepResults().size());
        assertEquals(1, result.getFailedTestStepsResults().size());
        assertEquals(2, result.getTestStepResults("GET request 1").size());
        assertEquals(1, result.getErrorMessages().size());
        assertEquals(1, result.getTestStepResult(1).getMessages().size());
        assertThat(FAIL, is(result.getTestStepResult(1).getStatusForAssertion("Valid HTTP Status Codes")));
        assertThat(PASS, is(result.getTestStepResult(1).getStatusForAssertion("Invalid HTTP Status Codes")));

        assertTrue(result.getFirstTestStepResult("get request 1").isPresent());
        assertTrue(result.getLastTestStepResult("get request 1").isPresent());
        assertEquals(1, result.getFailedTestStepsResults("GET request 1").size());

        assertNotNull(result.getTestStepResult(0).getHarEntry().getResponse());
        assertEquals("Test response", result.getTestStepResult(0).getResponseContent());
    }
}
