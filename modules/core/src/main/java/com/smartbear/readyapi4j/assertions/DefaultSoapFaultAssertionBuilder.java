package com.smartbear.readyapi4j.assertions;

import com.smartbear.readyapi4j.client.model.SoapFaultAssertion;

public class DefaultSoapFaultAssertionBuilder implements SoapFaultAssertionBuilder {

    private final SoapFaultAssertion soapFaultAssertion = new SoapFaultAssertion();

    @Override
    public SoapFaultAssertionBuilder named(String name) {
        soapFaultAssertion.setName(name);
        return this;
    }

    @Override
    public SoapFaultAssertion build() {
        soapFaultAssertion.setType(AssertionNames.SOAP_FAULT);
        return soapFaultAssertion;
    }

    public final static SoapFaultAssertion create() {
        return new DefaultSoapFaultAssertionBuilder().build();
    }
}
