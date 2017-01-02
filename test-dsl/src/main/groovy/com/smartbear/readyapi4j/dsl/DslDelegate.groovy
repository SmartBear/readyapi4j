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

    void GET(String URI) {
        GET([:], URI)
    }

    void GET(Map<String, Object> params, String URI) {
        createRestRequest(params, 'GET', URI)
    }

    void POST(String URI) {
        POST([:], URI)
    }

    void POST(Map<String, Object> params, String URI) {
        createRestRequest(params, 'POST', URI)
    }

    void PUT(String URI) {
        PUT([:], URI)
    }

    void PUT(Map<String, Object> params, String URI) {
        createRestRequest(params, 'PUT', URI)
    }

    void DELETE(String URI) {
        DELETE([:], URI)
    }

    void DELETE(Map<String, Object> params, String URI) {
        createRestRequest(params, 'DELETE', URI)
    }

    void propertyTransfer(PropertyTransferBuilder transferBuilder) {
        recipeBuilder.addStep(TestSteps.propertyTransfer(transferBuilder))
    }

    DeferredPropertyTransferBuilder transfer(String sourcePath) {
        return new DeferredPropertyTransferBuilder([property: 'Response', path: sourcePath], recipeBuilder)
    }

    DeferredPropertyTransferBuilder transfer(Map sourceProperties) {
        return new DeferredPropertyTransferBuilder(sourceProperties, recipeBuilder)
    }

    static final Map request = Collections.unmodifiableMap([property : 'Request'])

    static final Map response = Collections.unmodifiableMap([property : 'Response'])

    private void createRestRequest(Map<String, Object> params, String httpVerb, String URI) {
        RestRequestStepBuilder request = TestSteps."$httpVerb"(URI)
        request.named(params['name'] as String)
        Map<String, Object> headers = params['headers']
        if (headers) {
            headers.each { name, value ->
                request.addHeader(name, value)
            }
        }
        if (params['followRedirects'] as boolean) {
            request.followRedirects()
        }
        if (params['entitizeParameters'] as boolean) {
            request.entitizeParameters()
        }
        if (params['postQueryString'] as boolean) {
            request.postQueryString()
        }
        if (params['timeout']) {
            request.setTimeout(String.valueOf(params['timeout']))
        }
        recipeBuilder.addStep(request)
    }

    TestRecipe buildRecipe() {
        recipeBuilder.buildTestRecipe()
    }
}
