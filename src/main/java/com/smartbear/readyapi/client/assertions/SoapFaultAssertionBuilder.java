package com.smartbear.readyapi.client.assertions;

import com.smartbear.readyapi.client.model.SoapFaultAssertion;

public class SoapFaultAssertionBuilder extends AbstractAssertionBuilder<SoapFaultAssertion> {

    @Override
    public SoapFaultAssertion build() {
        SoapFaultAssertion assertion = new SoapFaultAssertion();
        assertion.setType(Assertions.SOAP_FAULT_TYPE);
        return assertion;
    }

    public final static SoapFaultAssertion create() {
        return new SoapFaultAssertionBuilder().build();
    }
}
