package com.smartbear.readyapi.client.assertions;

import com.smartbear.readyapi.client.model.ResponseSLAAssertion;

import static com.smartbear.readyapi.client.Validator.validateNotEmpty;

public class DefaultResponseSLAAssertionBuilder extends AbstractAssertionBuilder<ResponseSLAAssertion> {
    private ResponseSLAAssertion responseSLAAssertion = new ResponseSLAAssertion();

    public DefaultResponseSLAAssertionBuilder(int maxResponseTime) {
        responseSLAAssertion.setMaxResponseTime(maxResponseTime);
    }

    @Override
    public ResponseSLAAssertion build() {
        validateNotEmpty(responseSLAAssertion.getMaxResponseTime(), "Missing max response time, it's a mandatory parameter for ResponseSLAAssertion");
        responseSLAAssertion.setType("Response SLA");
        return responseSLAAssertion;
    }
}
