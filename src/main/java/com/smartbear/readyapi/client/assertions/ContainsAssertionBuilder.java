package com.smartbear.readyapi.client.assertions;


import io.swagger.client.model.SimpleContainsAssertion;

public interface ContainsAssertionBuilder extends AssertionBuilder<SimpleContainsAssertion> {
    ContainsAssertionBuilder useRegEx();

    ContainsAssertionBuilder ignoreCase();
}
