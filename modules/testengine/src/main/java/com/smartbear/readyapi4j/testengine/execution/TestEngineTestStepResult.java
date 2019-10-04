package com.smartbear.readyapi4j.testengine.execution;

import com.smartbear.readyapi4j.client.model.HarEntry;
import com.smartbear.readyapi4j.client.model.HarLogRoot;
import com.smartbear.readyapi4j.client.model.TestStepResultReport;
import com.smartbear.readyapi4j.result.AbstractTestStepResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Result wrapper for individual TestSteps executed on a TestEngine
 */

public class TestEngineTestStepResult extends AbstractTestStepResult {
    private final TestEngineExecution execution;
    private boolean hasCheckedForHarEntry;
    private HarEntry harEntry;
    private final static Logger LOG = LoggerFactory.getLogger(TestEngineTestStepResult.class);

    TestEngineTestStepResult(TestStepResultReport testStepResultReport, TestEngineExecution execution) {
        super(testStepResultReport);
        this.execution = execution;
    }

    @Override
    public HarEntry getHarEntry() {
        if (harEntry == null && !hasCheckedForHarEntry) {
            HarLogRoot logRoot = null;
            try {
                logRoot = execution.getTestEngineApi().getTransactionLog(execution.getId(),
                        testStepResultReport.getTransactionId(), execution.getAuth());

                if (hasHarEntry(logRoot)) {
                    harEntry = logRoot.getLog().getEntries().get(0);
                }
            } catch (ApiException e) {
                if (e.getStatusCode() != 404) {
                    LOG.error("Error when trying to get transaction log for execution " + execution.getId(), e);
                } else {
                    LOG.info("No transaction log available for execution " + execution.getId());
                }
            }

            hasCheckedForHarEntry = true;
        }

        return harEntry;
    }

    private boolean hasHarEntry(HarLogRoot logRoot) {
        return logRoot != null && logRoot.getLog() != null && logRoot.getLog().getEntries() != null &&
                logRoot.getLog().getEntries().size() > 0;
    }
}
