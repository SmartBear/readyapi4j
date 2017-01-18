package com.smartbear.readyapi4j.dsl

import com.smartbear.readyapi4j.assertions.AssertionBuilder
import com.smartbear.readyapi4j.dsl.assertions.SoapRequestAssertionsDelegate
import com.smartbear.readyapi4j.execution.RecipeExecutionException
import com.smartbear.readyapi4j.teststeps.soaprequest.SoapRequestStepBuilder

/**
 * Class created to support soapRequest closures within the DSL's recipe closures etc.
 */
class SoapRequestDelegate {

    private String wsdlString
    private String binding
    private String operation
    private Map<String, String> pathParameters = [:]
    private Map<String, String> namedParameters = [:]
    private List<AssertionBuilder> assertions

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

    void asserting(@DelegatesTo(SoapRequestAssertionsDelegate) Closure assertionsConfig) {
        def delegate = new SoapRequestAssertionsDelegate()
        assertionsConfig.delegate = delegate
        //Have to use DELEGATE_FIRST here, otherwise Groovy end up calling 'get' method on DslDelegate with assertion
        // name being converted into URI, e.g. "soapFault
        assertionsConfig.resolveStrategy = Closure.DELEGATE_FIRST
        assertionsConfig.call()
        this.assertions = delegate.assertionBuilders
    }

    SoapRequestStepBuilder buildSoapRequestStep() {
        SoapRequestStepBuilder builder = new SoapRequestStepBuilder()
                .forBinding(binding)
                .forOperation(operation)
        try {
            builder.withWsdlAt(new URL(wsdlString))
        } catch (MalformedURLException e) {
            throw new RecipeExecutionException("Not a valid WSDL location: $wsdlString", e)
        }

        pathParameters.each { path, value ->
            builder.withPathParameter(path, value)
        }
        namedParameters.each { name, value ->
            builder.withParameter(name, value)
        }
        assertions.each { assertion -> builder.addAssertion(assertion) }
        return builder
    }
}
