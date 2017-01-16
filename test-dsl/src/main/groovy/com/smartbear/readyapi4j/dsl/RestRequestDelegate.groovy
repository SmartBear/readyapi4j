package com.smartbear.readyapi4j.dsl

import com.smartbear.readyapi4j.assertions.AssertionBuilder
import com.smartbear.readyapi4j.dsl.assertions.AssertionsDelegate
import com.smartbear.readyapi4j.teststeps.restrequest.ParameterBuilder

/**
 * Class created to support restRequest closures within the DSL's recipe closures etc.
 */
class RestRequestDelegate {

    String stepName
    Map<String, Object> headers = [:]
    boolean followRedirects
    boolean entitizeParameters
    boolean postQueryString
    String timeout
    List<AssertionBuilder> assertions
    List<ParameterBuilder> parameters = []

    void name(String name) {
        this.stepName = name
    }

    void headers(Map<String, Object> headers) {
        this.headers = headers
    }

    void header(String name, String value) {
        headers[name] = value
    }

    void header(String name, List<String> values) {
        headers[name] = values
    }

    void followRedirects(boolean followRedirects) {
        this.followRedirects = followRedirects
    }

    void entitizeParameters(boolean entitize) {
        this.entitizeParameters = entitize
    }

    void postQueryString(boolean postQueryString) {
        this.postQueryString = postQueryString
    }

    void timeout(int timeout) {
        this.timeout = String.valueOf(timeout)
    }

    void asserting(@DelegatesTo(AssertionsDelegate) Closure assertionsConfig) {
        def delegate = new AssertionsDelegate()
        assertionsConfig.delegate = delegate
        assertionsConfig.call()
        this.assertions = delegate.assertionBuilders
    }

    void parameters(@DelegatesTo(ParametersDelegate) Closure parametersConfig) {
        ParametersDelegate delegate = new ParametersDelegate()
        parametersConfig.delegate = delegate
        parametersConfig.call()
    }

    class ParametersDelegate {

        void query(String name, String value) {
            parameters.add(ParameterBuilder.query(name, value))
        }

        void path(String name, String value) {
            parameters.add(ParameterBuilder.path(name, value))
        }

        void matrix(String name, String value) {
            parameters.add(ParameterBuilder.matrix(name, value))
        }

        void headerParam(String name, String value) {
            parameters.add(ParameterBuilder.header(name, value))
        }

    }
}
