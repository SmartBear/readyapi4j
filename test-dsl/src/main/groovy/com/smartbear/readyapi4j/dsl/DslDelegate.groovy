package com.smartbear.readyapi4j.dsl

import com.smartbear.readyapi4j.TestRecipe
import com.smartbear.readyapi4j.TestRecipeBuilder
import com.smartbear.readyapi4j.teststeps.TestSteps
import com.smartbear.readyapi4j.teststeps.propertytransfer.PropertyTransferBuilder
import com.smartbear.readyapi4j.teststeps.restrequest.RestRequestStepBuilder

/**
 * The delegate responding to commands inside the "recipe" closure.
 */
class DslDelegate {

    private TestRecipeBuilder recipeBuilder = new TestRecipeBuilder()

    void groovyScriptStep(String scriptText) {
        recipeBuilder.addStep(TestSteps.groovyScriptStep(scriptText))
    }

    void GET(String URI, @DelegatesTo(RestRequestDelegate) Closure configuration = null) {
        createRestRequest('GET', URI, configuration)
    }

    void POST(String URI, @DelegatesTo(RestRequestDelegate) Closure configuration = null) {
        createRestRequest('POST', URI, configuration)
    }

    void PUT(String URI, @DelegatesTo(RestRequestDelegate) Closure configuration = null) {
        createRestRequest('PUT', URI, configuration)
    }

    void DELETE(String URI, @DelegatesTo(RestRequestDelegate) Closure configuration = null) {
        createRestRequest('DELETE', URI, configuration)
    }

    void transfer(PropertyTransferBuilder transferBuilder) {
        recipeBuilder.addStep(TestSteps.propertyTransfer(transferBuilder))
    }

    DeferredPropertyTransferBuilder transfer(String sourcePath) {
        return new DeferredPropertyTransferBuilder([property: 'Response', path: sourcePath], recipeBuilder)
    }

    DeferredPropertyTransferBuilder transfer(Map sourceProperties) {
        return new DeferredPropertyTransferBuilder(sourceProperties, recipeBuilder)
    }

    DeferredDelayStepBuilder pause(BigDecimal time) {
        return new DeferredDelayStepBuilder(time, recipeBuilder)
    }

    static final Map request = Collections.unmodifiableMap([property : 'Request'])

    static final Map response = Collections.unmodifiableMap([property : 'Response'])

    void soapRequest(@DelegatesTo(SoapRequestDelegate) Closure soapRequestDefinition) {
        SoapRequestDelegate delegate = new SoapRequestDelegate()
        soapRequestDefinition.delegate = delegate
        soapRequestDefinition.call()
        recipeBuilder.addStep(delegate.buildSoapRequestStep())
    }

    private void createRestRequest(String httpVerb, String URI, Closure configuration = null) {
        RestRequestStepBuilder request = TestSteps."$httpVerb"(URI)
        if (configuration) {
            RestRequestDelegate delegate = new RestRequestDelegate()
            configuration.delegate = delegate
            configuration.call()
            request.named(delegate.stepName)
            Map<String, Object> headers = delegate.headers
            if (headers) {
                headers.each { name, value ->
                    request.addHeader(name, value)
                }
            }
            if (delegate.followRedirects) {
                request.followRedirects()
            }
            if (delegate.entitizeParameters) {
                request.entitizeParameters()
            }
            if (delegate.postQueryString) {
                request.postQueryString()
            }
            if (delegate.timeout) {
                request.setTimeout(delegate.timeout)
            }
            delegate.assertions.each { assertion -> request.addAssertion(assertion)}
            delegate.parameters.each { param -> request.addParameter(param)}
        }
        recipeBuilder.addStep(request)
    }

    TestRecipe buildRecipe() {
        recipeBuilder.buildTestRecipe()
    }
}
