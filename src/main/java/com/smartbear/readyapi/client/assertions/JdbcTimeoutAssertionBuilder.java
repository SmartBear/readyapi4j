package com.smartbear.readyapi.client.assertions;

import com.smartbear.readyapi.client.model.JdbcTimeoutAssertionStruct;

public class JdbcTimeoutAssertionBuilder extends AbstractAssertionBuilder<JdbcTimeoutAssertionStruct> {

    @Override
    public JdbcTimeoutAssertionStruct build() {
        JdbcTimeoutAssertionStruct responseSLAAssertion = new JdbcTimeoutAssertionStruct();
        responseSLAAssertion.setType("JDBC Timeout");
        return responseSLAAssertion;
    }
}
