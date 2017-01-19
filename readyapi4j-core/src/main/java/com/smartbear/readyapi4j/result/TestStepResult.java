package com.smartbear.readyapi4j.result;

import com.smartbear.readyapi.client.model.HarHeader;
import com.smartbear.readyapi.client.model.HarResponse;
import com.smartbear.readyapi.client.model.TestStepResultReport;

import java.util.List;

/**
 * Provides direct access to the result of a specific TestStep execution
 */

public interface TestStepResult {

    /**
     * @return the underlying transactionId for this TestStepResult
     */
    String getTransactionId();

    /**
     * @return the name of the TestStep that this result comes from
     */
    String getTestStepName();

    /**
     * @return the time it took to execute this TestStepResult in milliseconds
     */
    Long getTimeTaken();

    /**
     * @return the assertion status for this TestStep result
     */
    TestStepResultReport.AssertionStatusEnum getAssertionStatus();

    /**
     * @param assertionName
     * @return the assertion status of the specified assertion
     */
    TestStepResultReport.AssertionStatusEnum getStatusForAssertion(String assertionName);

    /**
     * @return a list of all assertion messages
     */
    List<String> getMessages();

    /**
     * @return the HAR response for the underlying HTTP transaction if this was an HTTP-based TestStep - null otherwise
     */
    HarResponse getHarResponse();

    /**
     * @return the response content for this TestStep if available
     */
    String getResponseContent();

    /**
     * @return a list of HAR response headers for the underlying  HTTP transaction if this was an HTTP-based TestStep - null otherwise
     */
    List<HarHeader> getResponseHeaders();
}
