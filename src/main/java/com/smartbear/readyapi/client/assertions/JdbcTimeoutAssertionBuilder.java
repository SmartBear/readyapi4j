package com.smartbear.readyapi.client.assertions;

import com.smartbear.readyapi.client.model.JdbcTimeoutAssertion;

import java.util.Objects;

public class JdbcTimeoutAssertionBuilder extends AbstractAssertionBuilder<JdbcTimeoutAssertion> {

    private final Object timeout;

    public JdbcTimeoutAssertionBuilder(long timeout) {
        this.timeout = timeout;
    }

    public JdbcTimeoutAssertionBuilder(String timeout) {
        Objects.requireNonNull(timeout);
        this.timeout = timeout;
    }

    @Override
    public JdbcTimeoutAssertion build() {
        JdbcTimeoutAssertion timeoutAssertion = new JdbcTimeoutAssertion();
        timeoutAssertion.setTimeout(String.valueOf(timeout));
        timeoutAssertion.setType(Assertions.JDBC_TIMEOUT_TYPE);
        return timeoutAssertion;
    }
}
