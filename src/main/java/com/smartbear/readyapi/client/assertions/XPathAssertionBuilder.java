package com.smartbear.readyapi.client.assertions;

import com.smartbear.readyapi.client.model.XPathContainsAssertion;

public interface XPathAssertionBuilder extends AssertionBuilder<XPathContainsAssertion> {
    XPathAssertionBuilder allowWildCards();

    XPathAssertionBuilder ignoreComments();

    XPathContainsAssertionBuilder ignoreNamespaces();
}
