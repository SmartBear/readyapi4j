package com.smartbear.readyapi.client.assertions;

import com.smartbear.readyapi.client.model.JdbcStatusAssertion;

public class JdbcStatusAssertionBuilder extends AbstractAssertionBuilder<JdbcStatusAssertion> {
    private final JdbcStatusAssertion statusAssertion = new JdbcStatusAssertion();

    public JdbcStatusAssertionBuilder named(String name) {
        statusAssertion.setName(name);
        return this;
    }

    @Override
    public JdbcStatusAssertion build() {
        statusAssertion.setType(Assertions.JDBC_STATUS_TYPE);
        return statusAssertion;
    }

    public final static JdbcStatusAssertion create() {
        JdbcStatusAssertion assertion = new JdbcStatusAssertion();
        assertion.setType(Assertions.JDBC_STATUS_TYPE);
        return assertion;
    }
}
