package io.swagger.assert4j.assertions;

import io.swagger.assert4j.client.model.ResponseSLAAssertion;

public interface ResponseSLAAssertionBuilder extends AssertionBuilder<ResponseSLAAssertion> {
    ResponseSLAAssertionBuilder named(String assertionName);
}
