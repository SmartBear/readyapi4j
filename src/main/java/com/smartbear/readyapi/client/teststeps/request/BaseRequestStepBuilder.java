package com.smartbear.readyapi.client.teststeps.request;

import com.smartbear.readyapi.client.assertions.AbstractAssertionBuilder;
import com.smartbear.readyapi.client.assertions.AssertionBuilder;
import com.smartbear.readyapi.client.auth.AuthenticationBuilder;
import com.smartbear.readyapi.client.model.Assertion;
import com.smartbear.readyapi.client.model.Parameter;
import com.smartbear.readyapi.client.model.RequestTestStepBase;

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

public class BaseRequestStepBuilder<RequestBuilderType extends HttpRequestStepBuilder, RequestTestStepType extends RequestTestStepBase> implements HttpRequestStepBuilder<RequestBuilderType> {
    protected final RequestTestStepType testStep;
    private List<Parameter> parameters = new ArrayList<>();
    private List<AssertionBuilder> assertionBuilders = new ArrayList<>();
    private Map<String, Object> headers = new HashMap<>();

    protected BaseRequestStepBuilder(RequestTestStepType testStep, String type) {
        this.testStep = testStep;
        this.testStep.setType(type);
    }

    protected List<Parameter> getParameters() {
        return parameters;
    }

    protected Map<String, Object> getHeaders() {
        return headers;
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
        testStep.setParameters(parameters);

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
}
