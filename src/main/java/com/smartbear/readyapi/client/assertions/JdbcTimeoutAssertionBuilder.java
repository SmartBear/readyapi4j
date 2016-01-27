package com.smartbear.readyapi.client.assertions;

import com.smartbear.readyapi.client.model.JdbcTimeoutAssertionStruct;

public class JdbcTimeoutAssertionBuilder extends AbstractAssertionBuilder<JdbcTimeoutAssertionStruct> {

    @Override
    public JdbcTimeoutAssertionStruct build() {
        JdbcTimeoutAssertionStruct timeoutAssertion = new JdbcTimeoutAssertionStruct();
        timeoutAssertion.setType("JDBC Timeout");
        return timeoutAssertion;
    }
}
