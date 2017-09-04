package io.swagger.assert4j.dsl

import io.swagger.assert4j.dsl.assertions.RestRequestAssertionsDelegate
import io.swagger.assert4j.extractor.Extractor
import io.swagger.assert4j.teststeps.restrequest.ParameterBuilder
import io.swagger.assert4j.teststeps.restrequest.RestRequestStepBuilder

/**
 * Class created to support restRequest closures within the DSL's recipe closures etc.
 */
class RestRequestDelegate {

    private RestRequestStepBuilder requestBuilder

    RestRequestDelegate(RestRequestStepBuilder requestBuilder) {
        this.requestBuilder = requestBuilder
    }

    void name(String name) {
        requestBuilder.named(name)
    }

    void headers(Map<String, Object> headers) {
        headers.each { name, value ->
            requestBuilder.addHeader(name, value)
        }
    }

    void header(String name, String value) {
        requestBuilder.addHeader(name, value)
    }

    void header(String name, List<String> values) {
        requestBuilder.addHeader(name, values)
    }

    void followRedirects(boolean followRedirects) {
        if (followRedirects) {
            requestBuilder.followRedirects()
        }
    }

    void entitizeParameters(boolean entitize) {
        if (entitize) {
            requestBuilder.entitizeParameters()
        }
    }

    void postQueryString(boolean postQueryString) {
        if (postQueryString) {
            requestBuilder.postQueryString()
        }
    }

    void timeout(int timeout) {
        requestBuilder.setTimeout(String.valueOf(timeout))
    }

    void body(String body) {
        requestBuilder.withRequestBody(body)
    }

    void asserting(@DelegatesTo(RestRequestAssertionsDelegate) Closure assertionsConfig) {
        def delegate = new RestRequestAssertionsDelegate()
        assertionsConfig.delegate = delegate
        assertionsConfig.call()
        delegate.assertionBuilders.each { assertion -> requestBuilder.addAssertion(assertion) }
    }

    void parameters(@DelegatesTo(ParametersDelegate) Closure parametersConfig) {
        ParametersDelegate delegate = new ParametersDelegate()
        parametersConfig.delegate = delegate
        parametersConfig.call()
    }

    void extractors(Extractor... extractors) {
        requestBuilder.withExtractors(extractors)
    }

    class ParametersDelegate {

        void query(String name, String value) {
            requestBuilder.addParameter(ParameterBuilder.query(name, value))
        }

        void path(String name, String value) {
            requestBuilder.addParameter(ParameterBuilder.path(name, value))
        }

        void matrix(String name, String value) {
            requestBuilder.addParameter(ParameterBuilder.matrix(name, value))
        }

        void headerParam(String name, String value) {
            requestBuilder.addParameter(ParameterBuilder.header(name, value))
        }
    }
}
