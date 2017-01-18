package com.smartbear.readyapi4j.assertions;

import static java.util.Arrays.asList;

/**
 * Utility class for building various types of AssertionBuilders when using the fluent API
 */

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
    public static final String SCHEMA_COMPLIANCE_TYPE = "Schema Compliance";
    public static final String SOAP_FAULT_TYPE = "SOAP Fault";
    public static final String NOT_SOAP_FAULT_TYPE = "Not SOAP Fault";

    @Deprecated
    public static JsonPathAssertionBuilder jsonPathContent(String jsonPath, String expectedContent) {
        return new JsonPathContentAssertionBuilder(jsonPath, expectedContent);
    }

    @Deprecated
    public static JsonPathAssertionBuilder jsonPathCount(String jsonPath, int expectedCount) {
        return new JsonPathCountAssertionBuilder(jsonPath, expectedCount);
    }

    public static JsonPathAssertionBuilder json(String jsonPath, String expectedContent) {
        return new JsonPathContentAssertionBuilder(jsonPath, expectedContent);
    }

    @Deprecated
    public static JsonPathAssertionBuilder jsonContent(String jsonPath, String expectedContent) {
        return new JsonPathContentAssertionBuilder(jsonPath, expectedContent);
    }

    public static JsonPathAssertionBuilder jsonCount(String jsonPath, int expectedCount) {
        return new JsonPathCountAssertionBuilder(jsonPath, expectedCount);
    }

    public static ContainsAssertionBuilder contains(String token) {
        return new DefaultContainsAssertionBuilder(token);
    }

    public static ContainsAssertionBuilder notContains(String token) {
        return new NotContainsAssertionBuilder(token);
    }

    public static GroovyScriptAssertionBuilder contentType(String contentType) {
        return new DefaultGroovyScriptAssertionBuilder(
            "assert messageExchange.responseHeaders[\"Content-Type\"].contains( \"" + contentType + "\")");
    }

    public static GroovyScriptAssertionBuilder headerExists(String header) {
        return new DefaultGroovyScriptAssertionBuilder(
            "assert messageExchange.responseHeaders.containsKey(\"" + header + "\")");
    }

    public static GroovyScriptAssertionBuilder headerValue(String header, String value) {
        return new DefaultGroovyScriptAssertionBuilder(
            "assert messageExchange.responseHeaders[\"" + header + "\"].contains( \"" + value + "\")");
    }

    public static GroovyScriptAssertionBuilder script(String script) {
        return new DefaultGroovyScriptAssertionBuilder(script);
    }

    @Deprecated
    public static HttpStatusCodeAssertionBuilder validStatusCodes() {
        return new ValidHttpStatusCodesAssertionBuilder();
    }

    @Deprecated
    public static HttpStatusCodeAssertionBuilder validStatusCodes(String... statusCodes) {
        ValidHttpStatusCodesAssertionBuilder validHttpStatusCodesAssertionBuilder = new ValidHttpStatusCodesAssertionBuilder();
        return validHttpStatusCodesAssertionBuilder.withStatusCodes(asList(statusCodes));
    }

    @Deprecated
    public static HttpStatusCodeAssertionBuilder validStatusCodes(Integer... statusCodes) {
        ValidHttpStatusCodesAssertionBuilder validHttpStatusCodesAssertionBuilder = new ValidHttpStatusCodesAssertionBuilder();
        return validHttpStatusCodesAssertionBuilder.withIntStatusCodes(asList(statusCodes));
    }

    public static HttpStatusCodeAssertionBuilder statusCodes() {
        return new ValidHttpStatusCodesAssertionBuilder();
    }

    public static HttpStatusCodeAssertionBuilder statusCodes(String... statusCodes) {
        ValidHttpStatusCodesAssertionBuilder validHttpStatusCodesAssertionBuilder = new ValidHttpStatusCodesAssertionBuilder();
        return validHttpStatusCodesAssertionBuilder.withStatusCodes(asList(statusCodes));
    }

    public static HttpStatusCodeAssertionBuilder statusCodes(Integer... statusCodes) {
        ValidHttpStatusCodesAssertionBuilder validHttpStatusCodesAssertionBuilder = new ValidHttpStatusCodesAssertionBuilder();
        return validHttpStatusCodesAssertionBuilder.withIntStatusCodes(asList(statusCodes));
    }

    public static InvalidHttpStatusCodesAssertionBuilder invalidStatusCodes() {
        return new InvalidHttpStatusCodesAssertionBuilder();
    }

    public static InvalidHttpStatusCodesAssertionBuilder invalidStatusCodes(Integer... statusCodes) {
        InvalidHttpStatusCodesAssertionBuilder invalidHttpStatusCodesAssertionBuilder = new InvalidHttpStatusCodesAssertionBuilder();
        invalidHttpStatusCodesAssertionBuilder.withIntStatusCodes(asList(statusCodes));
        return invalidHttpStatusCodesAssertionBuilder;
    }

    public static InvalidHttpStatusCodesAssertionBuilder invalidStatusCodes(String... statusCodes) {
        InvalidHttpStatusCodesAssertionBuilder invalidHttpStatusCodesAssertionBuilder = new InvalidHttpStatusCodesAssertionBuilder();
        invalidHttpStatusCodesAssertionBuilder.withStatusCodes(asList(statusCodes));
        return invalidHttpStatusCodesAssertionBuilder;
    }

    public static XPathAssertionBuilder xPathContains(String xPath, String expectedContent) {
        return new XPathContainsAssertionBuilder(xPath, expectedContent);
    }

    public static XQueryAssertionBuilder xQueryContains(String xQuery, String expectedContent) {
        return new XQueryContainsAssertionBuilder(xQuery, expectedContent);
    }

    @Deprecated
    public static ResponseSLAAssertionBuilder responseSLA(int maxResponseTime) {
        return new DefaultResponseSLAAssertionBuilder(maxResponseTime);
    }

    public static ResponseSLAAssertionBuilder maxResponseTime(int maxResponseTime) {
        return new DefaultResponseSLAAssertionBuilder(maxResponseTime);
    }

    public static ResponseSLAAssertionBuilder maxResponseTime(String maxResponseTime) {
        return new DefaultResponseSLAAssertionBuilder(maxResponseTime);
    }

    public static JdbcTimeoutAssertionBuilder jdbcRequestTimeout(long timeout) {
        return new DefaultJdbcTimeoutAssertionBuilder(timeout);
    }

    /**
     * This makes it possible to provide a property expansion that gives the timeout
     *
     * @param timeout a String that should expand to a numeric
     * @return a builder that will construct the JDBC Timeout assertion
     */

    public static JdbcTimeoutAssertionBuilder jdbcRequestTimeout(String timeout) {
        return new DefaultJdbcTimeoutAssertionBuilder(timeout);
    }

    public static JdbcStatusAssertionBuilder jdbcRequestStatusOk() {
        return new DefaultJdbcStatusAssertionBuilder();
    }

    public static SoapFaultAssertionBuilder soapFault() {
        return new DefaultSoapFaultAssertionBuilder();
    }

    public static NotSoapFaultAssertionBuilder notSoapFault() {
        return new DefaultNotSoapFaultAssertionBuilder();
    }
}
