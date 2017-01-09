package com.smartbear.readyapi4j.dsl

import com.smartbear.readyapi4j.assertions.AssertionBuilder
import com.smartbear.readyapi4j.assertions.DefaultContainsAssertionBuilder
import com.smartbear.readyapi4j.assertions.DefaultGroovyScriptAssertionBuilder
import com.smartbear.readyapi4j.assertions.InvalidHttpStatusCodesAssertionBuilder
import com.smartbear.readyapi4j.assertions.NotContainsAssertionBuilder
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

    void withHeaders(Map<String,Object> headers) {
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

    void asserting(@DelegatesTo(AssertionsDelegate) Closure assertionsConfig) {
        def delegate = new AssertionsDelegate()
        assertionsConfig.delegate = delegate
        assertionsConfig.call()
        this.assertions = delegate.assertionBuilders
    }

    class AssertionsDelegate {

        private List<AssertionBuilder> assertionBuilders = []

        void status(int... httpStatuses) {
            ValidHttpStatusCodesAssertionBuilder builder = new ValidHttpStatusCodesAssertionBuilder()
            for (int httpStatus : httpStatuses) {
                builder.addStatusCode(httpStatus)
            }
            assertionBuilders.add(builder)
        }

        void responseContains( Map<String,Object> options = [:], String token) {
            configureContainsAssertion(new DefaultContainsAssertionBuilder(token), options)
        }

        void responseDoesNotContain( Map<String,Object> options = [:], String token) {
            configureContainsAssertion(new NotContainsAssertionBuilder(token), options)
        }

        private void configureContainsAssertion(DefaultContainsAssertionBuilder builder, Map<String, Object> options) {
            if (options['useRegexp'] as boolean) {
                builder.useRegEx()
            }
            if (options['ignoreCase'] as boolean) {
                builder.ignoreCase()
            }
            assertionBuilders.add(builder)
        }

        void statusNotIn(int... invalidStatuses) {
            InvalidHttpStatusCodesAssertionBuilder builder = new InvalidHttpStatusCodesAssertionBuilder()
            for (int httpStatus : invalidStatuses) {
                builder.addStatusCode(httpStatus)
            }
            assertionBuilders.add(builder)
        }

        void script(String scriptText) {
            assertionBuilders.add(new DefaultGroovyScriptAssertionBuilder(scriptText))
        }

        JsonPathAssertionDelegate jsonPath(String jsonPath) {
            return new JsonPathAssertionDelegate(jsonPath, assertionBuilders)
        }

        XPathAssertionDelegate xpath(String xpath) {
            return new XPathAssertionDelegate(xpath, assertionBuilders)
        }

    }

}
