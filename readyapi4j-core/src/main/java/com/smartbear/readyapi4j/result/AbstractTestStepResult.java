package com.smartbear.readyapi4j.result;

import com.smartbear.readyapi.client.model.HarContent;
import com.smartbear.readyapi.client.model.HarEntry;
import com.smartbear.readyapi.client.model.HarHeader;
import com.smartbear.readyapi.client.model.HarResponse;
import com.smartbear.readyapi.client.model.TestStepResultReport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        return testStepResultReport.getTimeTaken();
    }

    @Override
    public TestStepResultReport.AssertionStatusEnum getAssertionStatus() {
        return testStepResultReport.getAssertionStatus();
    }

    @Override
    public TestStepResultReport.AssertionStatusEnum getStatusForAssertion(String assertionName) {
        for (String message : testStepResultReport.getMessages()) {
            if (message.startsWith("[" + assertionName + "]")) {
                return TestStepResultReport.AssertionStatusEnum.FAILED;
            }
        }
        return TestStepResultReport.AssertionStatusEnum.OK;
    }

    @Override
    public List<String> getMessages() {
        return testStepResultReport.getMessages();
    }

    @Override
    public HarResponse getHarResponse() {
        HarEntry harEntry = getHarEntry();
        return harEntry == null ? null : harEntry.getResponse();
    }

    public abstract HarEntry getHarEntry();

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
