package io.swagger.assert4j.assertions;

import io.swagger.assert4j.client.model.SoapFaultAssertion;
import io.swagger.assert4j.AssertionNames;

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
