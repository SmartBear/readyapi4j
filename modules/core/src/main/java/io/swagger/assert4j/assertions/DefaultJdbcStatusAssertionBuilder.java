package io.swagger.assert4j.assertions;

import io.swagger.assert4j.client.model.JdbcStatusAssertion;
import io.swagger.assert4j.AssertionNames;

public class DefaultJdbcStatusAssertionBuilder implements JdbcStatusAssertionBuilder {
    private final JdbcStatusAssertion statusAssertion = new JdbcStatusAssertion();

    @Override
    public DefaultJdbcStatusAssertionBuilder named(String name) {
        statusAssertion.setName(name);
        return this;
    }

    @Override
    public JdbcStatusAssertion build() {
        statusAssertion.setType(AssertionNames.JDBC_STATUS);
        return statusAssertion;
    }

    public final static JdbcStatusAssertion create() {
        JdbcStatusAssertion assertion = new JdbcStatusAssertion();
        assertion.setType(AssertionNames.JDBC_STATUS);
        return assertion;
    }
}
