package io.swagger.assert4j.assertions;

import io.swagger.assert4j.client.model.Assertion;

public interface JsonPathAssertionBuilder<T extends Assertion> extends AssertionBuilder<T> {
    JsonPathAssertionBuilder allowWildcards();

    JsonPathAssertionBuilder named(String assertionName);
}
