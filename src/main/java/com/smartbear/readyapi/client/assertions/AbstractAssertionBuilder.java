package com.smartbear.readyapi.client.assertions;

import com.smartbear.readyapi.client.model.Assertion;

public abstract class AbstractAssertionBuilder<T extends Assertion> implements AssertionBuilder<T> {
    public abstract T build();
}

