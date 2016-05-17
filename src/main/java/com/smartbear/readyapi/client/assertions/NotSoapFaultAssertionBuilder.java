package com.smartbear.readyapi.client.assertions;

import com.smartbear.readyapi.client.model.NotSoapFaultAssertion;

public class NotSoapFaultAssertionBuilder extends AbstractAssertionBuilder<NotSoapFaultAssertion> {

    @Override
    public NotSoapFaultAssertion build() {
        NotSoapFaultAssertion assertion = new NotSoapFaultAssertion();
        assertion.setType(Assertions.NOT_SOAP_FAULT_TYPE);
        return assertion;
    }

    public final static NotSoapFaultAssertion create() {
        return new NotSoapFaultAssertionBuilder().build();
    }
}
