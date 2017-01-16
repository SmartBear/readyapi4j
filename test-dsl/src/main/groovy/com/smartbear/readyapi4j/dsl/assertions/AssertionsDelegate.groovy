package com.smartbear.readyapi4j.dsl.assertions

import com.smartbear.readyapi4j.assertions.AssertionBuilder
import com.smartbear.readyapi4j.assertions.Assertions
import com.smartbear.readyapi4j.assertions.ContainsAssertionBuilder
import com.smartbear.readyapi4j.assertions.InvalidHttpStatusCodesAssertionBuilder
import com.smartbear.readyapi4j.assertions.ValidHttpStatusCodesAssertionBuilder

import static com.smartbear.readyapi4j.assertions.Assertions.contains
import static com.smartbear.readyapi4j.assertions.Assertions.notContains


class AssertionsDelegate {

    private List<AssertionBuilder> assertionBuilders = []

    List<AssertionBuilder> getAssertionBuilders() {
        return assertionBuilders
    }

    void status(int ... httpStatuses) {
        ValidHttpStatusCodesAssertionBuilder builder = new ValidHttpStatusCodesAssertionBuilder()
        for (int httpStatus : httpStatuses) {
            builder.addStatusCode(httpStatus)
        }
        assertionBuilders.add(builder)
    }

    void responseContains(Map<String, Object> options = [:], String token) {
        configureContainsAssertion(contains(token), options)
    }

    void responseDoesNotContain(Map<String, Object> options = [:], String token) {
        configureContainsAssertion(notContains(token), options)
    }

    private void configureContainsAssertion(ContainsAssertionBuilder builder, Map<String, Object> options) {
        if (options['useRegexp'] as boolean) {
            builder.useRegEx()
        }
        if (options['ignoreCase'] as boolean) {
            builder.ignoreCase()
        }
        assertionBuilders.add(builder)
    }

    void statusNotIn(int ... invalidStatuses) {
        InvalidHttpStatusCodesAssertionBuilder builder = new InvalidHttpStatusCodesAssertionBuilder()
        for (int httpStatus : invalidStatuses) {
            builder.addStatusCode(httpStatus)
        }
        assertionBuilders.add(builder)
    }

    void script(String scriptText) {
        assertionBuilders.add(Assertions.script(scriptText))
    }

    void contentType(String contentType) {
        assertionBuilders.add(Assertions.contentType(contentType))
    }

    JsonPathAssertionDelegate jsonPath(String jsonPath) {
        return new JsonPathAssertionDelegate(jsonPath, assertionBuilders)
    }

    XPathAssertionDelegate xpath(String xpath) {
        return new XPathAssertionDelegate(xpath, assertionBuilders)
    }

    XQueryAssertionDelegate xQuery(String xQuery) {
        return new XQueryAssertionDelegate(xQuery, assertionBuilders)
    }

    ResponseSlaAssertionDelegate maxResponseTime(int maxResponseTime) {
        return new ResponseSlaAssertionDelegate(maxResponseTime, assertionBuilders)
    }
}
