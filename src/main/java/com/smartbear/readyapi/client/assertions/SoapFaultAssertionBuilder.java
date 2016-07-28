package com.smartbear.readyapi.client.assertions;

import com.smartbear.readyapi.client.model.SoapFaultAssertion;

public class SoapFaultAssertionBuilder extends AbstractAssertionBuilder<SoapFaultAssertion> {

    private final SoapFaultAssertion soapFaultAssertion = new SoapFaultAssertion();

    public SoapFaultAssertionBuilder named(String name) {
        soapFaultAssertion.setName(name);
        return this;
    }

    @Override
    public SoapFaultAssertion build() {
        soapFaultAssertion.setType(Assertions.SOAP_FAULT_TYPE);
        return soapFaultAssertion;
    }

    public final static SoapFaultAssertion create() {
        return new SoapFaultAssertionBuilder().build();
    }
}
