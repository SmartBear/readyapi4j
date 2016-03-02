package com.smartbear.readyapi.client.assertions;

import static java.util.Arrays.asList;

public class Assertions {
    public static final String CONTAINS_ASSERTION_TYPE = "Contains";
    public static final String SCRIPT_ASSERTION_TYPE = "Script Assertion";
    public static final String RESPONSE_SLA_TYPE = "Response SLA";
    public static final String INVALID_HTTP_STATUS_CODES_TYPE = "Invalid HTTP Status Codes";
    public static final String JDBC_STATUS_TYPE = "JDBC Status";
    public static final String JDBC_TIMEOUT_TYPE = "JDBC Timeout";
    public static final String JSON_PATH_MATCH_TYPE = "JsonPath Match";
    public static final String JSON_PATH_COUNT_TYPE = "JsonPath Count";
    public static final String NOT_CONTAINS_TYPE = "Not Contains";
    public static final String VALID_HTTP_STATUS_CODES_TYPE = "Valid HTTP Status Codes";
    public static final String XPATH_MATCH_TYPE = "XPath Match";
    public static final String XQUERY_MATCH_TYPE = "XQuery Match";

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

    public static AssertionBuilder jdbcRequestTimeout(long timeout) {
        return new JdbcTimeoutAssertionBuilder(timeout);
    }

    /**
     * This makes it possible to provide a property expansion that gives the timeout
     * @param timeout a String that should expand to a numeric
     * @return a builder that will construct the JDBC Timeout assertion
     */

    public static AssertionBuilder jdbcRequestTimeout(String timeout) {
        return new JdbcTimeoutAssertionBuilder(timeout);
    }

    public static AssertionBuilder jdbcRequestStatusOk() {
        return new JdbcStatusAssertionBuilder();
    }
}
