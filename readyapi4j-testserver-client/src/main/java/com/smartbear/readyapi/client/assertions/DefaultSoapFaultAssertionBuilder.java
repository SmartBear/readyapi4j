package com.smartbear.readyapi.client.assertions;

import com.smartbear.readyapi.client.model.SoapFaultAssertion;

public class DefaultSoapFaultAssertionBuilder extends AbstractAssertionBuilder<SoapFaultAssertion>
implements SoapFaultAssertionBuilder {

    private final SoapFaultAssertion soapFaultAssertion = new SoapFaultAssertion();

    @Override
    public DefaultSoapFaultAssertionBuilder named(String name) {
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
