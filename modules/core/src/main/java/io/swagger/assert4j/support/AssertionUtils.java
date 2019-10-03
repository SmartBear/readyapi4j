package io.swagger.assert4j.support;

import io.swagger.assert4j.client.model.HarResponse;
import io.swagger.assert4j.client.model.TestJobReport;
import io.swagger.assert4j.execution.Execution;
import io.swagger.assert4j.result.RecipeExecutionResult;
import io.swagger.assert4j.result.TestStepResult;
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

        assertEquals("Execution failed: " + Arrays.toString(execution.getErrorMessages().toArray()),
                TestJobReport.StatusEnum.FINISHED, execution.getCurrentStatus());
    }

    public static void assertExecutionResult(RecipeExecutionResult execution) {
        assertNotNull(execution);

        if (LOG.isDebugEnabled()) {
            logExecution(execution);
        }

        assertEquals("Execution failed: " + Arrays.toString(execution.getErrorMessages().toArray()),
                TestJobReport.StatusEnum.FINISHED, execution.getStatus());
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
