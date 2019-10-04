package com.smartbear.readyapi4j.assertions;

import com.smartbear.readyapi4j.client.model.NotSoapFaultAssertion;

public interface NotSoapFaultAssertionBuilder extends AssertionBuilder<NotSoapFaultAssertion> {
    NotSoapFaultAssertionBuilder named(String assertionName);
}
