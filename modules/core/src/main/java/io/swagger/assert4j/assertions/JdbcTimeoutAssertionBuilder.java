package io.swagger.assert4j.assertions;

import io.swagger.assert4j.client.model.JdbcTimeoutAssertion;

public interface JdbcTimeoutAssertionBuilder extends AssertionBuilder<JdbcTimeoutAssertion> {
    JdbcTimeoutAssertionBuilder named(String assertionName);
}
