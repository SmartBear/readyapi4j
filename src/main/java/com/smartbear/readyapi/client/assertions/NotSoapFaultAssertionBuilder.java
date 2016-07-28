package com.smartbear.readyapi.client.assertions;

import com.smartbear.readyapi.client.model.NotSoapFaultAssertion;

public interface NotSoapFaultAssertionBuilder extends AssertionBuilder<NotSoapFaultAssertion> {
    NotSoapFaultAssertionBuilder named(String assertionName);
}
