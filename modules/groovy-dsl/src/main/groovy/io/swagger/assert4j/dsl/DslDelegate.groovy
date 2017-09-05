package io.swagger.assert4j.dsl

import io.swagger.assert4j.TestRecipeBuilder
import io.swagger.assert4j.teststeps.TestSteps
import io.swagger.assert4j.teststeps.groovyscript.GroovyScriptTestStepBuilder
import io.swagger.assert4j.teststeps.jdbcrequest.JdbcRequestTestStepBuilder
import io.swagger.assert4j.teststeps.mockresponse.SoapMockResponseTestStepBuilder
import io.swagger.assert4j.teststeps.plugin.PluginTestStepBuilder
import io.swagger.assert4j.teststeps.properties.PropertiesTestStepBuilder
import io.swagger.assert4j.teststeps.propertytransfer.PropertyTransferBuilder
import io.swagger.assert4j.teststeps.propertytransfer.PropertyTransferTestStepBuilder
import io.swagger.assert4j.teststeps.restrequest.RestRequestStepBuilder
import io.swagger.assert4j.teststeps.soaprequest.SoapRequestStepBuilder

/**
 * The delegate responding to commands inside the "recipe" closure.
 */
class DslDelegate {

    protected TestRecipeBuilder testRecipeBuilder

    DslDelegate(TestRecipeBuilder testRecipeBuilder) {
        this.testRecipeBuilder = testRecipeBuilder
    }

    void name(String recipeName) {
        testRecipeBuilder.named(recipeName)
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

    void post(String URI, String bodyString) {
        post(URI, {
            contentType 'application/json'
            body bodyString
        })
    }

    void put(String URI, @DelegatesTo(RestRequestDelegate) Closure configuration = null) {
        createRestRequest('PUT', URI, configuration)
    }

    void put(String URI, String bodyString) {
        put(URI, {
            contentType 'application/json'
            body bodyString
        })
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
        pluginTestStepDefinition.resolveStrategy = Closure.DELEGATE_FIRST
        pluginTestStepDefinition.call()

        PluginTestStepBuilder pluginTestStepBuilder = TestSteps.pluginTestStep(delegate.type)
                .withConfigProperties(delegate.configuration)
                .named(delegate.testStepName)

        testRecipeBuilder.addStep(pluginTestStepBuilder)
    }

    static final Map request = Collections.unmodifiableMap([property: 'Request'])

    static final Map response = Collections.unmodifiableMap([property: 'Response'])

    void soapRequest(@DelegatesTo(SoapRequestDelegate) Closure soapRequestDefinition) {
        SoapRequestStepBuilder soapRequestStepBuilder = new SoapRequestStepBuilder()

        SoapRequestDelegate delegate = new SoapRequestDelegate(soapRequestStepBuilder)
        soapRequestDefinition.delegate = delegate
        soapRequestDefinition.resolveStrategy = Closure.DELEGATE_FIRST
        soapRequestDefinition.call()
        testRecipeBuilder.addStep(soapRequestStepBuilder)
    }

    void soapMockResponse(@DelegatesTo(SoapMockResponseDelegate) Closure soapMockResponseDefinition) {
        SoapMockResponseTestStepBuilder builder = new SoapMockResponseTestStepBuilder()

        SoapMockResponseDelegate delegate = new SoapMockResponseDelegate(builder)
        soapMockResponseDefinition.delegate = delegate
        soapMockResponseDefinition.resolveStrategy = Closure.DELEGATE_FIRST
        soapMockResponseDefinition.call()

        testRecipeBuilder.addStep(builder)
    }

    void jdbcRequest(@DelegatesTo(JdbcRequestDelegate) Closure jdbcRequestDefinition) {
        JdbcRequestDelegate delegate = new JdbcRequestDelegate()
        jdbcRequestDefinition.delegate = delegate
        jdbcRequestDefinition.resolveStrategy = Closure.DELEGATE_FIRST
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
        RestRequestStepBuilder requestBuilder = TestSteps."$httpVerb"(URI)
        if (configuration) {
            RestRequestDelegate delegate = new RestRequestDelegate(requestBuilder)
            configuration.delegate = delegate
            configuration.resolveStrategy = Closure.DELEGATE_FIRST
            configuration.call()
        }
        testRecipeBuilder.addStep(requestBuilder)
    }
}
