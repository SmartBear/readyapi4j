package com.smartbear.readyapi.client.assertions;

import io.swagger.client.model.XQueryContainsAssertion;

public interface XQueryAssertionBuilder extends AssertionBuilder<XQueryContainsAssertion> {
    XQueryAssertionBuilder allowWildcards();
}
