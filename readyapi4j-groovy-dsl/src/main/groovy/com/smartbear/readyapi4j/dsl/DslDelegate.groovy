package com.smartbear.readyapi4j.dsl

import com.smartbear.readyapi4j.TestRecipeBuilder
import com.smartbear.readyapi4j.execution.RecipeExecutionException
import com.smartbear.readyapi4j.teststeps.TestSteps
import com.smartbear.readyapi4j.teststeps.groovyscript.GroovyScriptTestStepBuilder
import com.smartbear.readyapi4j.teststeps.jdbcrequest.JdbcRequestTestStepBuilder
import com.smartbear.readyapi4j.teststeps.mockresponse.SoapMockResponseTestStepBuilder
import com.smartbear.readyapi4j.teststeps.plugin.PluginTestStepBuilder
import com.smartbear.readyapi4j.teststeps.properties.PropertiesTestStepBuilder
import com.smartbear.readyapi4j.teststeps.propertytransfer.PropertyTransferBuilder
import com.smartbear.readyapi4j.teststeps.propertytransfer.PropertyTransferTestStepBuilder
import com.smartbear.readyapi4j.teststeps.restrequest.RestRequestStepBuilder

/**
 * The delegate responding to commands inside the "recipe" closure.
 */
class DslDelegate {

    protected TestRecipeBuilder testRecipeBuilder

    DslDelegate(TestRecipeBuilder testRecipeBuilder) {
        this.testRecipeBuilder = testRecipeBuilder
    }

    void groovyScriptStep(String scriptText, String testStepName = null) {
        GroovyScriptTestStepBuilder scriptTestStepBuilder = TestSteps.groovyScriptStep(scriptText)
        scriptTestStepBuilder.named(testStepName)
        testRecipeBuilder.addStep(scriptTestStepBuilder)
    }

    void get(String URI, @DelegatesTo(RestRequestDelegate) Closure configuration = null) {
        createRestRequest('GET', URI, configuration)
    }

    void post(String URI, @DelegatesTo(RestRequestDelegate) Closure configuration = null) {
        createRestRequest('POST', URI, configuration)
    }

    void put(String URI, @DelegatesTo(RestRequestDelegate) Closure configuration = null) {
        createRestRequest('PUT', URI, configuration)
    }

    void delete(String URI, @DelegatesTo(RestRequestDelegate) Closure configuration = null) {
        createRestRequest('DELETE', URI, configuration)
    }

    void transfer(PropertyTransferBuilder transferBuilder, String testStepName = null) {
        PropertyTransferTestStepBuilder transferTestStepBuilder = TestSteps.propertyTransfer(transferBuilder)
        transferTestStepBuilder.named(testStepName)
        testRecipeBuilder.addStep(transferTestStepBuilder)
    }

    DeferredPropertyTransferBuilder transfer(String sourcePath) {
        return new DeferredPropertyTransferBuilder([property: 'Response', path: sourcePath], testRecipeBuilder)
    }

    DeferredPropertyTransferBuilder transfer(Map sourceProperties) {
        return new DeferredPropertyTransferBuilder(sourceProperties, testRecipeBuilder)
    }

    DeferredDelayStepBuilder pause(BigDecimal time) {
        return new DeferredDelayStepBuilder(time, testRecipeBuilder)
    }

    void properties(Map<String, String> properties = [:], String testStepName = null) {
        PropertiesTestStepBuilder propertiesTestStepBuilder = TestSteps.properties(properties)
        propertiesTestStepBuilder.named(testStepName)
        testRecipeBuilder.addStep(propertiesTestStepBuilder)
    }

    void pluginProvidedStep(@DelegatesTo(PluginTestStepDelegate) Closure pluginTestStepDefinition) {
        PluginTestStepDelegate delegate = new PluginTestStepDelegate()
        pluginTestStepDefinition.delegate = delegate
        pluginTestStepDefinition.call()

        PluginTestStepBuilder pluginTestStepBuilder = TestSteps.pluginTestStep(delegate.type)
                .withConfigProperties(delegate.configuration)
                .named(delegate.testStepName)

        testRecipeBuilder.addStep(pluginTestStepBuilder)
    }

    static final Map request = Collections.unmodifiableMap([property: 'Request'])

    static final Map response = Collections.unmodifiableMap([property: 'Response'])

    void soapRequest(@DelegatesTo(SoapRequestDelegate) Closure soapRequestDefinition) {
        SoapRequestDelegate delegate = new SoapRequestDelegate()
        soapRequestDefinition.delegate = delegate
        soapRequestDefinition.call()
        testRecipeBuilder.addStep(delegate.buildSoapRequestStep())
    }

    void soapMockResponse(@DelegatesTo(SoapMockResponseDelegate) Closure soapMockResponseDefinition) {
        SoapMockResponseDelegate delegate = new SoapMockResponseDelegate()
        soapMockResponseDefinition.delegate = delegate
        soapMockResponseDefinition.call()

        SoapMockResponseTestStepBuilder builder = new SoapMockResponseTestStepBuilder()
                .named(delegate.testStepName)
                .forBinding(delegate.binding)
                .forOperation(delegate.operation)
                .withPath(delegate.path)
                .withPort(delegate.port)
        try {
            builder.withWsdlAt(new URL(delegate.wsdlUrl))
        } catch (MalformedURLException e) {
            throw new RecipeExecutionException("Not a valid WSDL location: $delegate.wsdlUrl", e)
        }
        testRecipeBuilder.addStep(builder)
    }

    void jdbcRequest(@DelegatesTo(JdbcRequestDelegate) Closure jdbcRequestDefinition) {
        JdbcRequestDelegate delegate = new JdbcRequestDelegate()
        jdbcRequestDefinition.delegate = delegate
        jdbcRequestDefinition.call()

        JdbcRequestTestStepBuilder builder = new JdbcRequestTestStepBuilder(delegate.driver, delegate.connectionString,
                delegate.storedProcedure)
        builder.named(delegate.testStepName)
        if (delegate.testStepProperties) {
            builder.withProperties(delegate.testStepProperties)
        }
        delegate.assertions.each { assertionBuilder -> builder.addAssertion(assertionBuilder) }
        testRecipeBuilder.addStep(builder)
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
            delegate.assertions.each { assertion -> request.addAssertion(assertion) }
            delegate.parameters.each { param -> request.addParameter(param) }
        }
        testRecipeBuilder.addStep(request)
    }
}
