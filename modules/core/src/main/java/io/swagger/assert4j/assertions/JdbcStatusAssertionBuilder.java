package io.swagger.assert4j.assertions;

import io.swagger.assert4j.client.model.JdbcStatusAssertion;

public interface JdbcStatusAssertionBuilder extends AssertionBuilder<JdbcStatusAssertion> {
    JdbcStatusAssertionBuilder named(String assertionName);
}
