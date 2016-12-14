package com.smartbear.readyapi4j.assertions;

import com.smartbear.readyapi.client.model.ResponseSLAAssertion;

import static com.smartbear.readyapi4j.Validator.validateNotEmpty;

public class DefaultResponseSLAAssertionBuilder extends AbstractAssertionBuilder<ResponseSLAAssertion>
        implements ResponseSLAAssertionBuilder {
    private ResponseSLAAssertion responseSLAAssertion = new ResponseSLAAssertion();

    public DefaultResponseSLAAssertionBuilder(int maxResponseTime) {
        responseSLAAssertion.setMaxResponseTime(String.valueOf(maxResponseTime));
    }

    public DefaultResponseSLAAssertionBuilder(String maxResponseTime) {
        responseSLAAssertion.setMaxResponseTime(maxResponseTime);
    }

    @Override
    public DefaultResponseSLAAssertionBuilder named(String name) {
        responseSLAAssertion.setName(name);
        return this;
    }

    @Override
    public ResponseSLAAssertion build() {
        validateNotEmpty(responseSLAAssertion.getMaxResponseTime(), "Missing max response time, it's a mandatory parameter for ResponseSLAAssertion");
        responseSLAAssertion.setType(Assertions.RESPONSE_SLA_TYPE);
        return responseSLAAssertion;
    }

    public final static ResponseSLAAssertion create() {
        ResponseSLAAssertion assertion = new ResponseSLAAssertion();
        assertion.setType(Assertions.RESPONSE_SLA_TYPE);
        return assertion;
    }
}
