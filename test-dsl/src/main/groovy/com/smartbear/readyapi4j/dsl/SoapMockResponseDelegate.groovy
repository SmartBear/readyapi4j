package com.smartbear.readyapi4j.dsl

/**
 * Delegate for the 'soapMockResponse'  closure in 'recipe' closure
 */
class SoapMockResponseDelegate {

    String testStepName
    String wsdlUrl
    String binding
    String operation
    String path
    int port

    void setName(String testStepName) {
        this.testStepName = testStepName
    }

    void setWsdl(String wsdlUrl) {
        this.wsdlUrl = wsdlUrl
    }

    void setBinding(String binding) {
        this.binding = binding
    }

    void setOperation(String operation) {
        this.operation = operation
    }

    void setPath(String path) {
        this.path = path
    }

    void setPort(int port) {
        this.port = port
    }
}
