package com.smartbear.readyapi4j.result;

import com.smartbear.readyapi.client.model.HarEntry;
import com.smartbear.readyapi.client.model.HarLogRoot;
import com.smartbear.readyapi.client.model.TestStepResultReport;
import com.smartbear.readyapi4j.execution.TestServerExecution;
import com.smartbear.readyapi4j.result.AbstractTestStepResult;

/**
 * Result wrapper for individual TestSteps
 */

public class TestServerTestStepResult extends AbstractTestStepResult {
    private final TestServerExecution execution;
    private boolean hasCheckedForHarEntry;
    private HarEntry harEntry;

    public TestServerTestStepResult(TestStepResultReport testStepResultReport, TestServerExecution execution) {
        super(testStepResultReport);

        this.execution = execution;
    }

    public TestStepResultReport setAssertionStatus(TestStepResultReport.AssertionStatusEnum assertionStatus) {
        return testStepResultReport.assertionStatus(assertionStatus);
    }

    public boolean hasTransactionData() {
        return getHarEntry() != null;
    }

    @Override
    public HarEntry getHarEntry() {
        if (harEntry == null && !hasCheckedForHarEntry) {
            HarLogRoot logRoot = execution.getTestServerApi().getTransactionLog(execution.getId(),
                    testStepResultReport.getTransactionId(), execution.getAuth());

            if (hasHarEntry(logRoot)) {
                harEntry = logRoot.getLog().getEntries().get(0);
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
