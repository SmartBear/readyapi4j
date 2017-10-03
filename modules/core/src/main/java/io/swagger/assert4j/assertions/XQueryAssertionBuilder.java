package io.swagger.assert4j.assertions;

import io.swagger.assert4j.client.model.XQueryContainsAssertion;

public interface XQueryAssertionBuilder extends AssertionBuilder<XQueryContainsAssertion> {
    XQueryAssertionBuilder allowWildcards();

    XQueryAssertionBuilder named(String assertionName);
}
