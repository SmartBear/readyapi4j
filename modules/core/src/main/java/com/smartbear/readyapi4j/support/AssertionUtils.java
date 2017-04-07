package com.smartbear.readyapi4j.support;

import com.smartbear.readyapi.client.model.HarResponse;
import com.smartbear.readyapi.client.model.ProjectResultReport;
import com.smartbear.readyapi4j.execution.Execution;
import com.smartbear.readyapi4j.result.RecipeExecutionResult;
import com.smartbear.readyapi4j.result.TestStepResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Utility class for asserting an Execution
 */

public class AssertionUtils {

    private static final Logger LOG = LoggerFactory.getLogger(AssertionUtils.class);

    public static void assertExecution(Execution execution) {
        assertNotNull(execution);

        if (LOG.isDebugEnabled()) {
            logExecution(execution.getExecutionResult());
        }

        assertEquals(Arrays.toString(execution.getErrorMessages().toArray()),
            ProjectResultReport.StatusEnum.FINISHED, execution.getCurrentStatus());
    }

    public static void assertExecutionResult(RecipeExecutionResult execution) {
        assertNotNull(execution);

        if (LOG.isDebugEnabled()) {
            logExecution(execution);
        }

        assertEquals(Arrays.toString(execution.getErrorMessages().toArray()),
            ProjectResultReport.StatusEnum.FINISHED, execution.getStatus());
    }

    public static void logExecution(RecipeExecutionResult execution) {
        for (TestStepResult result : execution.getTestStepResults()) {
            if (result.getHarEntry() != null && result.getHarEntry().getResponse() != null) {
                HarResponse response = result.getHarEntry().getResponse();
                LOG.info("TestStep [" + result.getTestStepName() + "] response: " + response.getStatus() +
                    " - " + response.getContent().getText());
            } else {
                LOG.debug("Missing HAR response for TestStep [" + result.getTestStepName() + "]");
            }
        }
    }
}
