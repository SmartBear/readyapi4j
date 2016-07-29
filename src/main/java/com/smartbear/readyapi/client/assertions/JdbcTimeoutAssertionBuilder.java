package com.smartbear.readyapi.client.assertions;

import com.smartbear.readyapi.client.model.JdbcTimeoutAssertion;

public interface JdbcTimeoutAssertionBuilder extends AssertionBuilder<JdbcTimeoutAssertion> {
    JdbcTimeoutAssertionBuilder named(String assertionName);
}
