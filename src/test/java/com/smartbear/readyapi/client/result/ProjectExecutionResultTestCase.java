package com.smartbear.readyapi.client.result;

import com.smartbear.readyapi.client.execution.Execution;
import com.smartbear.readyapi.client.model.ProjectResultReport;
import io.swagger.util.Json;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ProjectExecutionResultTestCase {

    @Test
    public void testExecutionResult() throws IOException {
        ProjectResultReport resultReport =
            Json.mapper().readValue(new FileInputStream("src/test/resources/project-result-report.json"),
                ProjectResultReport.class);

        Execution.ProjectRecipeExecutionResult result = new Execution.ProjectRecipeExecutionResult(resultReport);

        assertEquals(ProjectResultReport.StatusEnum.FINISHED, result.getStatus());
        assertEquals(215, result.getTimeTaken());
        assertEquals("93ddece4-53b3-4f23-9bb0-f5db6ed6b9ef", result.getExecutionId());
        assertEquals(3, result.getResultCount());
        assertEquals(3, result.getTestStepResults().size());
        assertEquals(1, result.getFailedTestStepsResults().size());
        assertEquals(2, result.getTestStepResults("GET request 1").size());
        assertEquals(1, result.getErrorMessages().size());
        assertEquals(1, result.getTestStepResult(1).getMessages().size());

        assertNotNull(result.getFirstTestStepResult("get request 1"));
        assertNotNull(result.getLastTestStepResult("get request 1"));
        assertEquals(1, result.getFailedTestStepsResults("GET request 1").size());
    }
}
