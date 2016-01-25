package com.smartbear.readyapi.client.assertions;

import io.swagger.client.model.Assertion;

public abstract class AbstractAssertionBuilder<T extends Assertion> implements AssertionBuilder<T> {
    public abstract T build();
}

