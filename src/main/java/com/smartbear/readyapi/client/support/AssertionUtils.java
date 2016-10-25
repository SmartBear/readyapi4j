package com.smartbear.readyapi.client.support;

import com.smartbear.readyapi.client.execution.ApiException;
import com.smartbear.readyapi.client.execution.Execution;
import com.smartbear.readyapi.client.model.ProjectResultReport;
import com.smartbear.readyapi.client.result.TestStepResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AssertionUtils {

    public static final Logger LOG = LoggerFactory.getLogger(AssertionUtils.class);

    public static void assertExecution(Execution execution) {
        assertNotNull(execution);

        for (TestStepResult result : execution.getExecutionResult().getTestStepResults()) {

            String responseContent = null;
            try {
                responseContent = result.getResponseContent();
            } catch (ApiException e) {
                if (e.getStatusCode() == 404) {
                    LOG.info("Missing response content for TestStep [" + result.getTestStepName() + "]");
                } else {
                    LOG.error("Missing response content for TestStep [" + result.getTestStepName() + "]", e);
                }
            }

            if (responseContent != null) {
                LOG.info("Response content for TestStep [" + result.getTestStepName() + "]: " + responseContent);
            }
        }

        assertEquals(Arrays.toString(execution.getErrorMessages().toArray()),
            ProjectResultReport.StatusEnum.FINISHED, execution.getCurrentStatus());
    }
}
