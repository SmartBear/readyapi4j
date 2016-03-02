package com.smartbear.readyapi.client.assertions;

import com.smartbear.readyapi.client.model.JdbcStatusAssertion;

public class JdbcStatusAssertionBuilder extends AbstractAssertionBuilder<JdbcStatusAssertion> {

    @Override
    public JdbcStatusAssertion build() {
        JdbcStatusAssertion statusAssertion = new JdbcStatusAssertion();
        statusAssertion.setType(Assertions.JDBC_STATUS_TYPE);
        return statusAssertion;
    }
}
