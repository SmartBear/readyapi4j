package com.smartbear.readyapi4j.assertions;

import com.smartbear.readyapi.client.model.SoapFaultAssertion;

public interface SoapFaultAssertionBuilder extends AssertionBuilder<SoapFaultAssertion> {
    SoapFaultAssertionBuilder named(String assertionName);
}
