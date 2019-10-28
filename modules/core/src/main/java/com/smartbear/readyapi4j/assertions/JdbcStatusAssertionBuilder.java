package com.smartbear.readyapi4j.assertions;

import com.smartbear.readyapi4j.client.model.JdbcStatusAssertion;

public interface JdbcStatusAssertionBuilder extends AssertionBuilder<JdbcStatusAssertion> {
    JdbcStatusAssertionBuilder named(String assertionName);
}
