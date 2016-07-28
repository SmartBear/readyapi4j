package com.smartbear.readyapi.client.assertions;

import com.smartbear.readyapi.client.model.ResponseSLAAssertion;

public interface ResponseSLAAssertionBuilder extends AssertionBuilder<ResponseSLAAssertion> {
    ResponseSLAAssertionBuilder named(String assertionName);
}
