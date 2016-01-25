package com.smartbear.readyapi.client.assertions;

import static java.util.Arrays.asList;

public class Assertions {
    public static JsonPathAssertionBuilder jsonPathContent(String jsonPath, String expectedContent) {
        return new JsonPathContentAssertionBuilder(jsonPath, expectedContent);
    }

    public static JsonPathAssertionBuilder jsonPathCount(String jsonPath, int expectedCount) {
        return new JsonPathCountAssertionBuilder(jsonPath, expectedCount);
    }

    public static ContainsAssertionBuilder contains(String token) {
        return new DefaultContainsAssertionBuilder(token);
    }

    public static ContainsAssertionBuilder notContains(String token) {
        return new NotContainsAssertionBuilder(token);
    }

    public static AssertionBuilder script(String script) {
        return new DefaultGroovyScriptAssertionBuilder(script);
    }

    public static HttpStatusCodeAssertionBuilder validStatusCodes(Integer... statusCodes) {
        ValidHttpStatusCodesAssertionBuilder validHttpStatusCodesAssertionBuilder = new ValidHttpStatusCodesAssertionBuilder();
        return validHttpStatusCodesAssertionBuilder.addStatusCodes(asList(statusCodes));
    }

    public static InvalidHttpStatusCodesAssertionBuilder invalidStatusCodes(Integer... statusCodes) {
        InvalidHttpStatusCodesAssertionBuilder invalidHttpStatusCodesAssertionBuilder = new InvalidHttpStatusCodesAssertionBuilder();
        invalidHttpStatusCodesAssertionBuilder.addStatusCodes(asList(statusCodes));
        return invalidHttpStatusCodesAssertionBuilder;
    }

    public static XPathAssertionBuilder xPathContains(String xPath, String expectedContent) {
        return new XPathContainsAssertionBuilder(xPath, expectedContent);
    }

    public static XQueryAssertionBuilder xQueryContains(String xQuery, String expectedContent) {
        return new XQueryContainsAssertionBuilder(xQuery, expectedContent);
    }

    public static AssertionBuilder responseSLA(int maxResponseTime) {
        return new DefaultResponseSLAAssertionBuilder(maxResponseTime);
    }

}
