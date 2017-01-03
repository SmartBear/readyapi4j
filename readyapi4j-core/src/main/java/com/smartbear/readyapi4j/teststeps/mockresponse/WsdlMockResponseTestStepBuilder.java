package com.smartbear.readyapi4j.teststeps.mockresponse;

import com.smartbear.readyapi.client.model.WsdlMockResponseTestStep;
import com.smartbear.readyapi4j.teststeps.TestStepBuilder;
import com.smartbear.readyapi4j.teststeps.TestStepTypes;

import java.net.URL;

public class WsdlMockResponseTestStepBuilder implements TestStepBuilder<WsdlMockResponseTestStep> {
    private final WsdlMockResponseTestStep mockResponseTestStep = new WsdlMockResponseTestStep();

    public WsdlMockResponseTestStepBuilder named(String name) {
        mockResponseTestStep.setName(name);
        return this;
    }

    public WsdlMockResponseTestStepBuilder withWsdlAt(URL wsdlUrl) {
        mockResponseTestStep.setWsdl(wsdlUrl.toString());
        return this;
    }

    public WsdlMockResponseTestStepBuilder forOperation(String operation) {
        mockResponseTestStep.setOperation(operation);
        return this;
    }

    public WsdlMockResponseTestStepBuilder forBinding(String binding) {
        mockResponseTestStep.setBinding(binding);
        return this;
    }

    public WsdlMockResponseTestStepBuilder withPath(String path) {
        mockResponseTestStep.setPath(path);
        return this;
    }

    public WsdlMockResponseTestStepBuilder withPort(int port) {
        mockResponseTestStep.setPort(port);
        return this;
    }

    @Override
    public WsdlMockResponseTestStep build() {
        mockResponseTestStep.setType(TestStepTypes.WSDL_MOCK_RESPONSE.getName());
        return mockResponseTestStep;
    }
}
