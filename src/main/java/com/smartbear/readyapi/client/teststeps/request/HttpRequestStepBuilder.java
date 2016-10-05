package com.smartbear.readyapi.client.teststeps.request;

import com.smartbear.readyapi.client.assertions.AbstractAssertionBuilder;
import com.smartbear.readyapi.client.assertions.AssertionBuilder;
import com.smartbear.readyapi.client.auth.AuthenticationBuilder;
import com.smartbear.readyapi.client.model.Assertion;
import com.smartbear.readyapi.client.model.RequestTestStepBase;
import com.smartbear.readyapi.client.teststeps.TestStepBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.smartbear.readyapi.client.assertions.Assertions.contains;
import static com.smartbear.readyapi.client.assertions.Assertions.invalidStatusCodes;
import static com.smartbear.readyapi.client.assertions.Assertions.notContains;
import static com.smartbear.readyapi.client.assertions.Assertions.responseSLA;
import static com.smartbear.readyapi.client.assertions.Assertions.script;
import static com.smartbear.readyapi.client.assertions.Assertions.validStatusCodes;
import static com.smartbear.readyapi.client.assertions.Assertions.xPathContains;
import static com.smartbear.readyapi.client.assertions.Assertions.xQueryContains;

abstract public class HttpRequestStepBuilder<RequestBuilderType extends HttpRequestStepBuilder, RequestTestStepType extends RequestTestStepBase> implements TestStepBuilder {

    private final RequestTestStepType testStep;
    private List<AssertionBuilder> assertionBuilders = new ArrayList<>();
    private Map<String, Object> headers = new HashMap<>();

    protected HttpRequestStepBuilder(RequestTestStepType testStep, String type) {
        this.testStep = testStep;
        this.testStep.setType(type);
    }

    final protected RequestTestStepType getTestStep() {
        return testStep;
    }

    protected Map<String, Object> getHeaders() {
        return headers;
    }

    /**
     * Test Step specific Certificate file can be added on the TestServer in allowedFilePath directory. Otherwise it should be provided by the client.
     * Client will throw an exception if file doesn't exist on client and on server.
     *
     * @param filePath Certificate file path
     */
    public RequestBuilderType withClientCertificate(String filePath) {
        testStep.setClientCertificateFileName(filePath);
        return (RequestBuilderType) this;
    }

    public RequestBuilderType withClientCertificatePassword(String password) {
        testStep.setClientCertificatePassword(password);
        return (RequestBuilderType) this;
    }

    public RequestBuilderType named(String name) {
        testStep.setName(name);
        return (RequestBuilderType) this;
    }

    public RequestBuilderType addAssertion(AssertionBuilder assertionBuilder) {
        this.assertionBuilders.add(assertionBuilder);
        return (RequestBuilderType) this;
    }

    public RequestBuilderType setAuthentication(AuthenticationBuilder authenticationBuilder) {
        testStep.setAuthentication(authenticationBuilder.build());
        return (RequestBuilderType) this;
    }

    public RequestBuilderType accepts(String acceptHeader) {
        return addHeader("Accept", acceptHeader);
    }

    public RequestBuilderType acceptsJson() {
        return accepts("application/json");
    }

    public RequestBuilderType acceptsXml() {
        return accepts("application/xml");
    }

    public RequestBuilderType withHeader(String name, List<String> values) {
        return addHeader(name, values);
    }

    public RequestBuilderType withHeader(String name, String value) {
        return addHeader(name, value);
    }

    public RequestBuilderType addHeader(String name, List<String> values) {
        List<String> headerValues = (List<String>) this.headers.get(name);
        if (headerValues == null) {
            headerValues = new ArrayList<>();
            this.headers.put(name, headerValues);
        }
        headerValues.addAll(values);
        return (RequestBuilderType) this;
    }

    public RequestBuilderType addHeader(String name, String value) {
        return addHeader(name, Collections.singletonList(value));
    }

    protected RequestBuilderType withURI(String uri) {
        testStep.setURI(uri);
        return (RequestBuilderType) this;
    }

    /**
     * Set timeout using property expansion
     */
    public RequestBuilderType setTimeout(String timeout) {
        testStep.setTimeout(timeout);
        return (RequestBuilderType) this;
    }

    /**
     * Sets timeout value
     */
    public RequestBuilderType setTimeout(int timeout) {
        testStep.setTimeout(String.valueOf(timeout));
        return (RequestBuilderType) this;
    }

    public RequestBuilderType followRedirects() {
        testStep.setFollowRedirects(true);
        return (RequestBuilderType) this;
    }

    public RequestBuilderType entitizeParameters() {
        testStep.setEntitizeParameters(true);
        return (RequestBuilderType) this;
    }

    public RequestTestStepType build() {
        testStep.setHeaders(headers);
        setAssertions(testStep);

        return testStep;
    }

    private void setAssertions(RequestTestStepType testStep) {
        List<Assertion> assertions = new ArrayList<>();
        for (AssertionBuilder assertionBuilder : assertionBuilders) {
            assertions.add(((AbstractAssertionBuilder) assertionBuilder).build());
        }
        testStep.setAssertions(assertions);
    }

    public RequestBuilderType assertContains(String content) {
        return addAssertion(contains(content));
    }

    public RequestBuilderType assertNotContains(String content) {
        return addAssertion(notContains(content));
    }

    public RequestBuilderType assertScript(String script) {
        return addAssertion(script(script));
    }

    public RequestBuilderType assertXPath(String xpath, String expectedContent) {
        return addAssertion(xPathContains(xpath, expectedContent));
    }

    public RequestBuilderType assertInvalidStatusCodes(Integer... statusCodes) {
        return addAssertion(invalidStatusCodes(statusCodes));
    }

    public RequestBuilderType assertValidStatusCodes(Integer... statusCodes) {
        return addAssertion(validStatusCodes(statusCodes));
    }

    public RequestBuilderType assertXQuery(String xquery, String expectedContent) {
        return addAssertion(xQueryContains(xquery, expectedContent));
    }

    public RequestBuilderType assertResponseTime(int timeInMillis) {
        return addAssertion(responseSLA(timeInMillis));
    }

    public RequestBuilderType assertContentType(String contentType) {
        return assertScript(
            "assert messageExchange.responseHeaders[\"Content-Type\"].contains( \"" + contentType + "\")");
    }

    public RequestBuilderType assertHeaderExists(String header) {
        return assertScript(
            "assert messageExchange.responseHeaders.containsKey(\"" + header + "\")");
    }

    public RequestBuilderType assertHeader(String header, String value) {
        return assertScript(
            "assert messageExchange.responseHeaders[\"" + header + "\"].contains( \"" + value + "\")");
    }
}
