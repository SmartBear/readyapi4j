package com.smartbear.readyapi4j.result;

import com.smartbear.readyapi.client.model.HarHeader;
import com.smartbear.readyapi.client.model.HarResponse;
import com.smartbear.readyapi.client.model.TestStepResultReport;

import java.util.List;

public interface TestStepResult {
    String getTransactionId();

    String getTestStepName();

    Long getTimeTaken();

    TestStepResultReport.AssertionStatusEnum getAssertionStatus();

    TestStepResultReport.AssertionStatusEnum getStatusForAssertion(String assertionName);

    List<String> getMessages();

    HarResponse getHarResponse();

    String getResponseContent();

    List<HarHeader> getResponseHeaders();
}
