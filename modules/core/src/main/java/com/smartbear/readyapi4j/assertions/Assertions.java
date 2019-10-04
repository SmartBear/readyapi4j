package com.smartbear.readyapi4j.assertions;

import static java.util.Arrays.asList;

/**
 * Utility class for building various types of AssertionBuilders when using the fluent API
 */

public class Assertions {
    /**
     * Deprecated, use json() instead.
     */
    @Deprecated
    public static JsonPathAssertionBuilder jsonPathContent(String jsonPath, String expectedContent) {
        return new JsonPathContentAssertionBuilder(jsonPath, expectedContent);
    }

    /**
     * Deprecated, use jsonCount() instead.
     */
    @Deprecated
    public static JsonPathAssertionBuilder jsonPathCount(String jsonPath, int expectedCount) {
        return new JsonPathCountAssertionBuilder(jsonPath, expectedCount);
    }

    /**
     * Deprecated, use json() instead.
     */
    @Deprecated
    public static JsonPathAssertionBuilder jsonContent(String jsonPath, String expectedContent) {
        return new JsonPathContentAssertionBuilder(jsonPath, expectedContent);
    }

    public static JsonPathAssertionBuilder json(String jsonPath, String expectedContent) {
        return new JsonPathContentAssertionBuilder(jsonPath, expectedContent);
    }

    public static JsonPathAssertionBuilder jsonCount(String jsonPath, int expectedCount) {
        return new JsonPathCountAssertionBuilder(jsonPath, expectedCount);
    }

    /**
     * This makes it possible to provide a property expansion that gives the expected count
     */
    public static JsonPathAssertionBuilder jsonCount(String jsonPath, String expectedCount) {
        return new JsonPathCountAssertionBuilder(jsonPath, expectedCount);
    }

    public static JsonPathExistenceAssertionBuilder jsonExists(String jsonPath) {
        return new JsonPathExistenceAssertionBuilder(jsonPath, true);
    }

    public static JsonPathExistenceAssertionBuilder jsonNotExists(String jsonPath) {
        return new JsonPathExistenceAssertionBuilder(jsonPath, false);
    }

    /**
     * Used when expected value is provided as a property expansion expression.
     *
     * @param jsonPath      JSONPath to check existence of.
     * @param expectedValue Property expansion syntax for expected value, evaluated to true/false.
     * @return JsonPathExistenceAssertionBuilder
     */
    public static JsonPathExistenceAssertionBuilder jsonExistence(String jsonPath, String expectedValue) {
        return new JsonPathExistenceAssertionBuilder(jsonPath, expectedValue);
    }

    public static ContainsAssertionBuilder contains(String token) {
        return new DefaultContainsAssertionBuilder(token);
    }

    public static ContainsAssertionBuilder matches(String token) {
        return new DefaultContainsAssertionBuilder(token).useRegEx();
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

    /**
     * Deprecated, use statusCodes() instead.
     */
    @Deprecated
    public static HttpStatusCodeAssertionBuilder validStatusCodes() {
        return new ValidHttpStatusCodesAssertionBuilder();
    }

    /**
     * Deprecated, use statusCodes(String... statusCodes) instead.
     */
    @Deprecated
    public static HttpStatusCodeAssertionBuilder validStatusCodes(String... statusCodes) {
        ValidHttpStatusCodesAssertionBuilder validHttpStatusCodesAssertionBuilder = new ValidHttpStatusCodesAssertionBuilder();
        return validHttpStatusCodesAssertionBuilder.withStatusCodes(asList(statusCodes));
    }

    /**
     * Deprecated, use statusCodes(Integer... statusCodes) instead.
     */
    @Deprecated
    public static HttpStatusCodeAssertionBuilder validStatusCodes(Integer... statusCodes) {
        ValidHttpStatusCodesAssertionBuilder validHttpStatusCodesAssertionBuilder = new ValidHttpStatusCodesAssertionBuilder();
        return validHttpStatusCodesAssertionBuilder.withIntStatusCodes(asList(statusCodes));
    }

    public static HttpStatusCodeAssertionBuilder statusCodes() {
        return new ValidHttpStatusCodesAssertionBuilder();
    }

    /**
     * This makes it possible to provide property expansions for status codes
     */

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

    /**
     * This makes it possible to provide property expansions for status codes
     */

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

    /**
     * Deprecated, use maxResponseTime instead.
     */
    @Deprecated
    public static ResponseSLAAssertionBuilder responseSLA(int maxResponseTime) {
        return new DefaultResponseSLAAssertionBuilder(maxResponseTime);
    }

    public static ResponseSLAAssertionBuilder maxResponseTime(int maxResponseTime) {
        return new DefaultResponseSLAAssertionBuilder(maxResponseTime);
    }

    /**
     * This makes it possible to provide a property expansion that gives the timeout
     */
    public static ResponseSLAAssertionBuilder maxResponseTime(String maxResponseTime) {
        return new DefaultResponseSLAAssertionBuilder(maxResponseTime);
    }

    public static JdbcTimeoutAssertionBuilder jdbcRequestTimeout(long timeout) {
        return new DefaultJdbcTimeoutAssertionBuilder(timeout);
    }

    /**
     * This makes it possible to provide a property expansion that gives the timeout
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

    public static SchemaComplianceAssertionBuilder schemaCompliance() {
        return new DefaultSchemaComplianceAssertionBuilder();
    }
}
