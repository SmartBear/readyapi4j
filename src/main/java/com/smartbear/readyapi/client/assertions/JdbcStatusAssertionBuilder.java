package com.smartbear.readyapi.client.assertions;

import com.smartbear.readyapi.client.model.JdbcStatusAssertion;

public interface JdbcStatusAssertionBuilder extends AssertionBuilder<JdbcStatusAssertion> {
    JdbcStatusAssertionBuilder named(String assertionName);
}
