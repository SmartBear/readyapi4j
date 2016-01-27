package com.smartbear.readyapi.client.assertions;

import com.smartbear.readyapi.client.model.JdbcStatusAssertionStruct;

public class JdbcStatusAssertionBuilder extends AbstractAssertionBuilder<JdbcStatusAssertionStruct> {

    @Override
    public JdbcStatusAssertionStruct build() {
        JdbcStatusAssertionStruct statusAssertion = new JdbcStatusAssertionStruct();
        statusAssertion.setType("JDBC Status");
        return statusAssertion;
    }
}
