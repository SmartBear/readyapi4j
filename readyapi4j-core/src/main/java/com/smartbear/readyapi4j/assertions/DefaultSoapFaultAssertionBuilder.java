package com.smartbear.readyapi4j.assertions;

import com.smartbear.readyapi.client.model.SoapFaultAssertion;

public class DefaultSoapFaultAssertionBuilder implements SoapFaultAssertionBuilder {

    private final SoapFaultAssertion soapFaultAssertion = new SoapFaultAssertion();

    @Override
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
        return new DefaultSoapFaultAssertionBuilder().build();
    }
}
