package com.smartbear.readyapi4j.assertions;

import com.smartbear.readyapi.client.model.XQueryContainsAssertion;

public interface XQueryAssertionBuilder extends AssertionBuilder<XQueryContainsAssertion> {
    XQueryAssertionBuilder allowWildcards();

    XQueryAssertionBuilder named(String assertionName);
}
