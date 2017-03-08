package com.smartbear.readyapi4j.assertions;

import com.smartbear.readyapi.client.model.JdbcTimeoutAssertion;
import com.smartbear.readyapi4j.AssertionNames;

import java.util.Objects;

public class DefaultJdbcTimeoutAssertionBuilder
        implements JdbcTimeoutAssertionBuilder {

    private final JdbcTimeoutAssertion timeoutAssertion;

    public DefaultJdbcTimeoutAssertionBuilder(long timeout) {
        this(String.valueOf(timeout));
    }

    public DefaultJdbcTimeoutAssertionBuilder(String timeout) {
        Objects.requireNonNull(timeout);
        timeoutAssertion = new JdbcTimeoutAssertion();
        timeoutAssertion.setTimeout(timeout);
        timeoutAssertion.setType(AssertionNames.JDBC_TIMEOUT);
    }

    @Override
    public DefaultJdbcTimeoutAssertionBuilder named(String name) {
        timeoutAssertion.setName(name);
        return this;
    }

    @Override
    public JdbcTimeoutAssertion build() {
        return timeoutAssertion;
    }

    public final static JdbcTimeoutAssertion create() {
        JdbcTimeoutAssertion assertion = new JdbcTimeoutAssertion();
        assertion.setType(AssertionNames.JDBC_TIMEOUT);
        return assertion;
    }
}
