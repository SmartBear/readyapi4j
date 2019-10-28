package com.smartbear.readyapi4j.assertions;

import com.smartbear.readyapi4j.client.model.JdbcStatusAssertion;

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
