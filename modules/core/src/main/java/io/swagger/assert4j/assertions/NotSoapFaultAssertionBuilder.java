package io.swagger.assert4j.assertions;

import io.swagger.assert4j.client.model.NotSoapFaultAssertion;

public interface NotSoapFaultAssertionBuilder extends AssertionBuilder<NotSoapFaultAssertion> {
    NotSoapFaultAssertionBuilder named(String assertionName);
}
