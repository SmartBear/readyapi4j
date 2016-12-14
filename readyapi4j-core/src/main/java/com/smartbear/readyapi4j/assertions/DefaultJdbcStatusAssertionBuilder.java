package com.smartbear.readyapi4j.assertions;

import com.smartbear.readyapi.client.model.JdbcStatusAssertion;

public class DefaultJdbcStatusAssertionBuilder extends AbstractAssertionBuilder<JdbcStatusAssertion>
        implements JdbcStatusAssertionBuilder {
    private final JdbcStatusAssertion statusAssertion = new JdbcStatusAssertion();

    @Override
    public DefaultJdbcStatusAssertionBuilder named(String name) {
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
