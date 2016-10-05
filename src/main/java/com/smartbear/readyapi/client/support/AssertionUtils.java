package com.smartbear.readyapi.client.support;

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
            LOG.info("Response content for TestStep [" + result.getTestStepName() + "]: " +
                result.getResponseContent());
        }

        assertEquals(Arrays.toString(execution.getErrorMessages().toArray()),
            ProjectResultReport.StatusEnum.FINISHED, execution.getCurrentStatus());
    }
}
