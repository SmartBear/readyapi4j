package com.smartbear.readyapi.client.assertions;

import com.smartbear.readyapi.client.model.XQueryContainsAssertion;

public interface XQueryAssertionBuilder extends AssertionBuilder<XQueryContainsAssertion> {
    XQueryAssertionBuilder allowWildcards();
}
