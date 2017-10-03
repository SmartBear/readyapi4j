package io.swagger.assert4j.assertions;

import io.swagger.assert4j.client.model.NotSoapFaultAssertion;

public class DefaultNotSoapFaultAssertionBuilder implements NotSoapFaultAssertionBuilder {

    private final NotSoapFaultAssertion notSoapFaultAssertion = new NotSoapFaultAssertion();

    @Override
    public NotSoapFaultAssertionBuilder named(String name) {
        notSoapFaultAssertion.setName(name);
        return this;
    }

    @Override
    public NotSoapFaultAssertion build() {
        notSoapFaultAssertion.setType(AssertionNames.NOT_SOAP_FAULT);
        return notSoapFaultAssertion;
    }

    public final static NotSoapFaultAssertion create() {
        return new DefaultNotSoapFaultAssertionBuilder().build();
    }
}
