package io.swagger.assert4j.assertions;

import io.swagger.assert4j.client.model.ResponseSLAAssertion;

import static io.swagger.assert4j.support.Validations.validateNotEmpty;

public class DefaultResponseSLAAssertionBuilder
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
        responseSLAAssertion.setType(AssertionNames.RESPONSE_SLA);
        return responseSLAAssertion;
    }

    public final static ResponseSLAAssertion create() {
        ResponseSLAAssertion assertion = new ResponseSLAAssertion();
        assertion.setType(AssertionNames.RESPONSE_SLA);
        return assertion;
    }
}
