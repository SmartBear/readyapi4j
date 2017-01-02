package com.smartbear.readyapi4j.dsl

import com.smartbear.readyapi.client.model.Assertion
import com.smartbear.readyapi4j.assertions.AssertionBuilder
import com.smartbear.readyapi4j.assertions.DefaultContainsAssertionBuilder
import com.smartbear.readyapi4j.assertions.ValidHttpStatusCodesAssertionBuilder

/**
 * Class created to support soapRequest closures within the DSL's recipe closures etc.
 */
class RestRequestDelegate {

    String stepName
    Map<String,Object> headers
    boolean followRedirects
    boolean entitizeParameters
    boolean postQueryString
    String timeout
    List<AssertionBuilder> assertions

    void name(String name) {
        this.stepName = name
    }

    void headers(Map<String,Object> headers) {
        this.headers = headers
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

    void assertions(Closure assertionsConfig) {
        def delegate = new AssertionsDelegate()
        assertionsConfig.delegate = delegate
        assertionsConfig.call()
        this.assertions = delegate.assertionBuilders
    }

    class AssertionsDelegate {

        private List<AssertionBuilder> assertionBuilders = []

        void status(int httpStatus) {
            assertionBuilders.add(new ValidHttpStatusCodesAssertionBuilder().addStatusCode(httpStatus))
        }

        void responseContains( Map<String,Object> options = [:], String token) {
            DefaultContainsAssertionBuilder builder = new DefaultContainsAssertionBuilder(token)
            if(options['useRegexp'] as boolean) {
                builder.useRegEx()
            }
            assertionBuilders.add(builder)
        }
    }

}
