package com.smartbear.readyapi.client.assertions;

import io.swagger.client.model.Assertion;

public interface JsonPathAssertionBuilder<T extends Assertion> extends AssertionBuilder<T> {
    JsonPathAssertionBuilder allowWildcards();
}
