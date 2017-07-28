package io.swagger.assert4j.dsl

import io.swagger.assert4j.execution.RecipeExecutionException
import io.swagger.assert4j.teststeps.mockresponse.SoapMockResponseTestStepBuilder

/**
 * Delegate for the 'soapMockResponse'  closure in 'recipe' closure
 */
class SoapMockResponseDelegate {
    private SoapMockResponseTestStepBuilder soapMockResponseTestStepBuilder

    SoapMockResponseDelegate(SoapMockResponseTestStepBuilder soapMockResponseTestStepBuilder) {
        this.soapMockResponseTestStepBuilder = soapMockResponseTestStepBuilder
    }

    void setName(String testStepName) {
        soapMockResponseTestStepBuilder.named(testStepName)
    }

    void setWsdl(String wsdlUrl) {
        try {
            soapMockResponseTestStepBuilder.withWsdlAt(new URL(wsdlUrl))
        } catch (MalformedURLException e) {
            throw new RecipeExecutionException("Not a valid WSDL location: $wsdlUrl", e)
        }
    }

    void setBinding(String binding) {
        soapMockResponseTestStepBuilder.forBinding(binding)
    }

    void setOperation(String operation) {
        soapMockResponseTestStepBuilder.forOperation(operation)
    }

    void setPath(String path) {
        soapMockResponseTestStepBuilder.withPath(path)
    }

    void setPort(int port) {
        soapMockResponseTestStepBuilder.withPort(port)
    }
}
