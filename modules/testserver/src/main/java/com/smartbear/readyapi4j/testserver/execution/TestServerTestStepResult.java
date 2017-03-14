package com.smartbear.readyapi4j.testserver.execution;

import com.smartbear.readyapi.client.model.HarEntry;
import com.smartbear.readyapi.client.model.HarLogRoot;
import com.smartbear.readyapi.client.model.TestStepResultReport;
import com.smartbear.readyapi4j.result.AbstractTestStepResult;

/**
 * Result wrapper for individual TestSteps executed on a TestServer
 */

public class TestServerTestStepResult extends AbstractTestStepResult {
    private final TestServerExecution execution;
    private boolean hasCheckedForHarEntry;
    private HarEntry harEntry;

    TestServerTestStepResult(TestStepResultReport testStepResultReport, TestServerExecution execution) {
        super(testStepResultReport);
        this.execution = execution;
    }

    @Override
    public HarEntry getHarEntry() {
        if (harEntry == null && !hasCheckedForHarEntry) {
            HarLogRoot logRoot = null;
            try {
                logRoot = execution.getTestServerApi().getTransactionLog(execution.getId(),
                        testStepResultReport.getTransactionId(), execution.getAuth());

                if (hasHarEntry(logRoot)) {
                    harEntry = logRoot.getLog().getEntries().get(0);
                }
            } catch (ApiException e) {
                if( e.getStatusCode() != 404 ){
                    e.printStackTrace();
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
