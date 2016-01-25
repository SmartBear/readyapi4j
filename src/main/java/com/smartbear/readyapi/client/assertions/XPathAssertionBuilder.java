package com.smartbear.readyapi.client.assertions;

import io.swagger.client.model.XPathContainsAssertion;

public interface XPathAssertionBuilder extends AssertionBuilder<XPathContainsAssertion> {
    XPathAssertionBuilder allowWildCards();

    XPathAssertionBuilder ignoreComments();

    XPathContainsAssertionBuilder ignoreNamespaces();
}
