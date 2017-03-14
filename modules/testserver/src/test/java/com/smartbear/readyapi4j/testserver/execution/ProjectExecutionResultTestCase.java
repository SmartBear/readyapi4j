package com.smartbear.readyapi4j.testserver.execution;

import com.smartbear.readyapi.client.model.HarLogRoot;
import com.smartbear.readyapi.client.model.ProjectResultReport;
import com.smartbear.readyapi4j.execution.Execution;
import com.smartbear.readyapi4j.result.RecipeExecutionResult;
import io.swagger.client.auth.HttpBasicAuth;
import io.swagger.util.Json;
import org.junit.Test;
import org.mockito.Matchers;

import java.io.FileInputStream;
import java.io.IOException;

import static com.smartbear.readyapi.client.model.TestStepResultReport.AssertionStatusEnum.FAILED;
import static com.smartbear.readyapi.client.model.TestStepResultReport.AssertionStatusEnum.OK;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProjectExecutionResultTestCase {

    @Test
    public void testExecutionResult() throws IOException {

        ProjectResultReport resultReport =
                Json.mapper().readValue(new FileInputStream("src/test/resources/project-result-report.json"),
                        ProjectResultReport.class);

        HarLogRoot harLogRoot =
                Json.mapper().readValue(new FileInputStream("src/test/resources/single-entry-har-log.json"),
                        HarLogRoot.class);

        TestServerApi apiMock = mock(TestServerApi.class);
        when(apiMock.getTransactionLog(Matchers.anyString(), Matchers.anyString(), (HttpBasicAuth) Matchers.any())).thenReturn(harLogRoot);

        Execution execution = new TestServerExecution(apiMock, new HttpBasicAuth(), resultReport);
        RecipeExecutionResult result = execution.getExecutionResult();

        assertEquals(ProjectResultReport.StatusEnum.FINISHED, result.getStatus());
        assertEquals(215, result.getTimeTaken());
        assertEquals("93ddece4-53b3-4f23-9bb0-f5db6ed6b9ef", result.getExecutionId());
        assertEquals(3, result.getResultCount());
        assertEquals(3, result.getTestStepResults().size());
        assertEquals(1, result.getFailedTestStepsResults().size());
        assertEquals(2, result.getTestStepResults("GET request 1").size());
        assertEquals(1, result.getErrorMessages().size());
        assertEquals(1, result.getTestStepResult(1).getMessages().size());
        assertThat(FAILED, is(result.getTestStepResult(1).getStatusForAssertion("Valid HTTP Status Codes")));
        assertThat(OK, is(result.getTestStepResult(1).getStatusForAssertion("Invalid HTTP Status Codes")));

        assertTrue(result.getFirstTestStepResult("get request 1").isPresent());
        assertTrue(result.getLastTestStepResult("get request 1").isPresent());
        assertEquals(1, result.getFailedTestStepsResults("GET request 1").size());

        assertNotNull(result.getTestStepResult(0).getHarEntry().getResponse());
        assertEquals("Test response", result.getTestStepResult(0).getResponseContent());
    }
}
