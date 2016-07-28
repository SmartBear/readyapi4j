package com.smartbear.readyapi.client.assertions;

import com.smartbear.readyapi.client.model.NotSoapFaultAssertion;

public class NotSoapFaultAssertionBuilder extends AbstractAssertionBuilder<NotSoapFaultAssertion> {

    private final NotSoapFaultAssertion notSoapFaultAssertion = new NotSoapFaultAssertion();

    public NotSoapFaultAssertionBuilder named(String name) {
        notSoapFaultAssertion.setName(name);
        return this;
    }

    @Override
    public NotSoapFaultAssertion build() {
        notSoapFaultAssertion.setType(Assertions.NOT_SOAP_FAULT_TYPE);
        return notSoapFaultAssertion;
    }

    public final static NotSoapFaultAssertion create() {
        return new NotSoapFaultAssertionBuilder().build();
    }
}
