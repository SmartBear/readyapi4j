package io.swagger.assert4j.assertions;

import io.swagger.assert4j.client.model.XPathContainsAssertion;

public interface XPathAssertionBuilder extends AssertionBuilder<XPathContainsAssertion> {
    XPathAssertionBuilder allowWildCards();

    XPathAssertionBuilder ignoreComments();

    XPathContainsAssertionBuilder ignoreNamespaces();

    XPathAssertionBuilder named(String assertionName);
}
