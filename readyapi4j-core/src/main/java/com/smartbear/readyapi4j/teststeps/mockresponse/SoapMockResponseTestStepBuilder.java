package com.smartbear.readyapi4j.teststeps.mockresponse;

import com.smartbear.readyapi.client.model.SoapMockResponseTestStep;
import com.smartbear.readyapi4j.teststeps.TestStepBuilder;
import com.smartbear.readyapi4j.teststeps.TestStepTypes;

import java.net.URL;

public class SoapMockResponseTestStepBuilder implements TestStepBuilder<SoapMockResponseTestStep> {
    private final SoapMockResponseTestStep mockResponseTestStep = new SoapMockResponseTestStep();

    public SoapMockResponseTestStepBuilder named(String name) {
        mockResponseTestStep.setName(name);
        return this;
    }

    public SoapMockResponseTestStepBuilder withWsdlAt(URL wsdlUrl) {
        mockResponseTestStep.setWsdl(wsdlUrl.toString());
        return this;
    }

    public SoapMockResponseTestStepBuilder forOperation(String operation) {
        mockResponseTestStep.setOperation(operation);
        return this;
    }

    public SoapMockResponseTestStepBuilder forBinding(String binding) {
        mockResponseTestStep.setBinding(binding);
        return this;
    }

    public SoapMockResponseTestStepBuilder withPath(String path) {
        mockResponseTestStep.setPath(path);
        return this;
    }

    public SoapMockResponseTestStepBuilder withPort(int port) {
        mockResponseTestStep.setPort(port);
        return this;
    }

    @Override
    public SoapMockResponseTestStep build() {
        mockResponseTestStep.setType(TestStepTypes.SOAP_MOCK_RESPONSE.getName());
        return mockResponseTestStep;
    }
}
