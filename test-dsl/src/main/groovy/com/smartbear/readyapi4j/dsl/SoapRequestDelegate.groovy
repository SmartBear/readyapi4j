package com.smartbear.readyapi4j.dsl

import com.smartbear.readyapi4j.teststeps.soaprequest.SoapRequestStepBuilder

/**
 * Class created to support soapRequest closures within the DSL's recipe closures etc.
 */
class SoapRequestDelegate {

    private String wsdlString
    private String binding
    private String operation
    private Map<String,String> pathParameters = [:]
    private Map<String,String> namedParameters = [:]



    void setWsdl(String wsdlUrl) {
        this.wsdlString = wsdlUrl
    }

    void setBinding(String binding) {
        this.binding = binding
    }

    void setOperation(String operation) {
        this.operation = operation
    }

    void pathParam(String path, Object value) {
        pathParameters[path] = value as String
    }

    void namedParam(String name, Object value) {
        namedParameters[name] = value as String
    }

    SoapRequestStepBuilder buildSoapRequestStep() {
        SoapRequestStepBuilder builder = new SoapRequestStepBuilder()
                .withWsdlAt(new URL(wsdlString))
                .forBinding(binding)
                .forOperation(operation)
        pathParameters.each { path, value ->
            builder.withPathParameter(path, value)
        }
        namedParameters.each { name, value ->
            builder.withParameter(name, value)
        }
        return builder
    }
}
