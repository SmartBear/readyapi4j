package io.swagger.assert4j.assertions;

import io.swagger.assert4j.client.model.SimpleContainsAssertion;

public interface ContainsAssertionBuilder extends AssertionBuilder<SimpleContainsAssertion> {
    ContainsAssertionBuilder useRegEx();

    ContainsAssertionBuilder ignoreCase();

    ContainsAssertionBuilder named(String assertionName);
}
