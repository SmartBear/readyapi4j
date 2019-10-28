package com.smartbear.readyapi4j.result;

import com.smartbear.readyapi4j.client.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Base class for TestStepResultReport based TestStepResult implementations
 */

public abstract class AbstractTestStepResult implements TestStepResult {
    private static final List<HarHeader> noHeadersList = Collections.unmodifiableList(new ArrayList());
    protected final TestStepResultReport testStepResultReport;

    public AbstractTestStepResult(TestStepResultReport testStepResultReport) {
        this.testStepResultReport = testStepResultReport;
    }

    @Override
    public String getTransactionId() {
        return testStepResultReport.getTransactionId();
    }

    @Override
    public String getTestStepName() {
        return testStepResultReport.getTestStepName();
    }

    @Override
    public Long getTimeTaken() {
        return testStepResultReport.getTotalTestStepTime();
    }

    @Override
    public TestStepResultReport.AssertionStatusEnum getAssertionStatus() {
        return testStepResultReport.getAssertionStatus();
    }

    @Override
    public TestStepResultReport.AssertionStatusEnum getStatusForAssertion(String assertionName) {
        for (String message : testStepResultReport.getMessages()) {
            if (message.startsWith("[" + assertionName + "]")) {
                return TestStepResultReport.AssertionStatusEnum.FAIL;
            }
        }
        return TestStepResultReport.AssertionStatusEnum.PASS;
    }

    @Override
    public List<String> getMessages() {
        return testStepResultReport.getMessages();
    }

    public HarResponse getHarResponse() {
        HarEntry harEntry = getHarEntry();
        return harEntry == null ? null : harEntry.getResponse();
    }

    @Override
    public String getResponseContent() {
        HarResponse harResponse = getHarResponse();
        if (harResponse != null) {
            HarContent content = harResponse.getContent();
            if (content != null) {
                return content.getText();
            }
        }
        return null;
    }

    @Override
    public List<HarHeader> getResponseHeaders() {
        HarResponse harResponse = getHarResponse();
        return harResponse == null ? noHeadersList : harResponse.getHeaders();
    }
}
