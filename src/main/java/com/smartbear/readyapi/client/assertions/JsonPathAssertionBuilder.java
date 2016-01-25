package com.smartbear.readyapi.client.assertions;

import com.smartbear.readyapi.client.model.Assertion;

public interface JsonPathAssertionBuilder<T extends Assertion> extends AssertionBuilder<T> {
    JsonPathAssertionBuilder allowWildcards();
}
