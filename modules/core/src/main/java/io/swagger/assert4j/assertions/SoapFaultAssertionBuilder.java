package io.swagger.assert4j.assertions;

import io.swagger.assert4j.client.model.SoapFaultAssertion;

public interface SoapFaultAssertionBuilder extends AssertionBuilder<SoapFaultAssertion> {
    SoapFaultAssertionBuilder named(String assertionName);
}
