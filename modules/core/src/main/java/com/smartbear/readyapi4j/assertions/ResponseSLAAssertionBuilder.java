package com.smartbear.readyapi4j.assertions;

import com.smartbear.readyapi4j.client.model.ResponseSLAAssertion;

public interface ResponseSLAAssertionBuilder extends AssertionBuilder<ResponseSLAAssertion> {
    ResponseSLAAssertionBuilder named(String assertionName);
}
