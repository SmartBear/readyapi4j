package com.smartbear.readyapi.client.assertions;

import com.smartbear.readyapi.client.model.JdbcTimeoutAssertion;

import java.util.Objects;

public class JdbcTimeoutAssertionBuilder extends AbstractAssertionBuilder<JdbcTimeoutAssertion> {

    private final JdbcTimeoutAssertion timeoutAssertion;

    public JdbcTimeoutAssertionBuilder(long timeout) {
        this(String.valueOf(timeout));
    }

    public JdbcTimeoutAssertionBuilder(String timeout) {
        Objects.requireNonNull(timeout);
        timeoutAssertion = new JdbcTimeoutAssertion();
        timeoutAssertion.setTimeout(timeout);
        timeoutAssertion.setType(Assertions.JDBC_TIMEOUT_TYPE);
    }

    public JdbcTimeoutAssertionBuilder named(String name) {
        timeoutAssertion.setName(name);
        return this;
    }

    @Override
    public JdbcTimeoutAssertion build() {
        return timeoutAssertion;
    }

    public final static JdbcTimeoutAssertion create() {
        JdbcTimeoutAssertion assertion = new JdbcTimeoutAssertion();
        assertion.setType(Assertions.JDBC_TIMEOUT_TYPE);
        return assertion;
    }
}
