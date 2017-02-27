package com.smartbear.readyapi4j.dsl

import com.smartbear.readyapi4j.dsl.assertions.SoapRequestAssertionsDelegate
import com.smartbear.readyapi4j.execution.RecipeExecutionException
import com.smartbear.readyapi4j.teststeps.soaprequest.SoapRequestStepBuilder

/**
 * Class created to support soapRequest closures within the DSL's recipe closures etc.
 */
class SoapRequestDelegate {

    private SoapRequestStepBuilder soapRequestStepBuilder

    SoapRequestDelegate(SoapRequestStepBuilder soapRequestStepBuilder) {
        this.soapRequestStepBuilder = soapRequestStepBuilder
    }

    void setWsdl(String wsdlUrl) {
        try {
            soapRequestStepBuilder.withWsdlAt(new URL(wsdlUrl))
        } catch (MalformedURLException e) {
            throw new RecipeExecutionException("Not a valid WSDL location: $wsdlUrl", e)
        }
    }

    void setBinding(String binding) {
        soapRequestStepBuilder.forBinding(binding)
    }

    void setOperation(String operation) {
        soapRequestStepBuilder.forOperation(operation)
    }

    void pathParam(String path, Object value) {
        soapRequestStepBuilder.withPathParameter(path, value as String)
    }

    void namedParam(String name, Object value) {
        soapRequestStepBuilder.withParameter(name, value as String)
    }

    void asserting(@DelegatesTo(SoapRequestAssertionsDelegate) Closure assertionsConfig) {
        def delegate = new SoapRequestAssertionsDelegate()
        assertionsConfig.delegate = delegate
        //Have to use DELEGATE_FIRST here, otherwise Groovy end up calling 'get' method on DslDelegate with assertion
        // name being converted into URI, e.g. "soapFault
        assertionsConfig.resolveStrategy = Closure.DELEGATE_FIRST
        assertionsConfig.call()
        delegate.assertionBuilders.each { assertion -> soapRequestStepBuilder.addAssertion(assertion) }
    }
}
