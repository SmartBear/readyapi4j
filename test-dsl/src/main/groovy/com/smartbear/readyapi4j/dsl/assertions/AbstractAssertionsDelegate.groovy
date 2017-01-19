package com.smartbear.readyapi4j.dsl.assertions

import com.smartbear.readyapi4j.assertions.AssertionBuilder
import com.smartbear.readyapi4j.assertions.Assertions
import com.smartbear.readyapi4j.assertions.ContainsAssertionBuilder
import com.smartbear.readyapi4j.assertions.InvalidHttpStatusCodesAssertionBuilder
import com.smartbear.readyapi4j.assertions.ValidHttpStatusCodesAssertionBuilder

import static com.smartbear.readyapi4j.assertions.Assertions.contains
import static com.smartbear.readyapi4j.assertions.Assertions.notContains
import static com.smartbear.readyapi4j.assertions.Assertions.responseSLA

/**
 * An abstract class which contains common assertions applicable to multiple requests: SOAP, REST etc.
 */
abstract class AbstractAssertionsDelegate {
    private List<AssertionBuilder> assertionBuilders = []

    List<AssertionBuilder> getAssertionBuilders() {
        return assertionBuilders
    }

    /**
     * Creates assertion to validate the status code in the response
     * @param httpStatuses status codes which should be present in the response
     */
    void status(int ... httpStatuses) {
        ValidHttpStatusCodesAssertionBuilder builder = new ValidHttpStatusCodesAssertionBuilder()
        for (int httpStatus : httpStatuses) {
            builder.addStatusCode(httpStatus)
        }
        assertionBuilders.add(builder)
    }

    /**
     * Creates 'Contains' assertion
     * @param options A map with assertion properties, e.g. useRegexp, ignoreCase etc.
     * @param token of which presence should be asserted in response
     */
    void responseContains(Map<String, Object> options = [:], String token) {
        configureContainsAssertion(contains(token), options)
    }

    /**
     * Creates 'Not Contains' assertion
     * @param options A map with assertion properties, e.g. useRegexp, ignoreCase etc.
     * @param token of which absence should be asserted in response
     */
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

    /**
     * Creates assertion to validate the absence of status codes in the response
     * @param invalidStatuses status codes which should not be present in the response
     */
    void statusNotIn(int ... invalidStatuses) {
        InvalidHttpStatusCodesAssertionBuilder builder = new InvalidHttpStatusCodesAssertionBuilder()
        for (int httpStatus : invalidStatuses) {
            builder.addStatusCode(httpStatus)
        }
        assertionBuilders.add(builder)
    }

    /**
     * Creates an assertion with script to validate the response as per the script
     * @param scriptText script to be executed on the response
     */
    void script(String scriptText) {
        assertionBuilders.add(Assertions.script(scriptText))
    }

    /**
     * Creates assertions to validate the value of "Content-Type" header in the response
     * @param contentType expected content type of the response
     */
    void contentType(String contentType) {
        assertionBuilders.add(Assertions.contentType(contentType))
    }

    /**
     * Creates XPath assertion delegate
     * @param xpath XPATH expression to be used on the response
     * @return XPathAssertionDelegate
     */
    XPathAssertionDelegate xpath(String xpath) {
        return new XPathAssertionDelegate(xpath, assertionBuilders)
    }

    /**
     * Creates XQuery assertion delegate
     * @param xQuery XQuery expression to be used on the response
     * @return XQueryAssertionDelegate
     */
    XQueryAssertionDelegate xQuery(String xQuery) {
        return new XQueryAssertionDelegate(xQuery, assertionBuilders)
    }

    /**
     * Creates assertion delegate to validate the max response time of the request
     * @param maxResponseTime expected maximum response time
     * @return TimeBasedAssertionDelegate
     */
    TimeBasedAssertionDelegate maxResponseTime(int maxResponseTime) {
        return new TimeBasedAssertionDelegate({ BigDecimal time -> responseSLA(time.intValue()) },
                new BigDecimal(maxResponseTime), assertionBuilders)
    }
}
