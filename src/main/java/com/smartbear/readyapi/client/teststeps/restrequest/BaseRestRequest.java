package com.smartbear.readyapi.client.teststeps.restrequest;

import com.smartbear.readyapi.client.assertions.AbstractAssertionBuilder;
import com.smartbear.readyapi.client.assertions.AssertionBuilder;
import com.smartbear.readyapi.client.auth.AuthenticationBuilder;
import com.smartbear.readyapi.client.model.Assertion;
import com.smartbear.readyapi.client.model.Parameter;
import com.smartbear.readyapi.client.model.RestTestRequestStep;
import com.smartbear.readyapi.client.teststeps.TestStepBuilder;
import com.smartbear.readyapi.client.teststeps.TestStepTypes;
import com.smartbear.readyapi.client.teststeps.TestSteps;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.smartbear.readyapi.client.Validator.validateNotEmpty;
import static com.smartbear.readyapi.client.assertions.Assertions.contains;
import static com.smartbear.readyapi.client.assertions.Assertions.invalidStatusCodes;
import static com.smartbear.readyapi.client.assertions.Assertions.jsonPathContent;
import static com.smartbear.readyapi.client.assertions.Assertions.jsonPathCount;
import static com.smartbear.readyapi.client.assertions.Assertions.notContains;
import static com.smartbear.readyapi.client.assertions.Assertions.responseSLA;
import static com.smartbear.readyapi.client.assertions.Assertions.script;
import static com.smartbear.readyapi.client.assertions.Assertions.validStatusCodes;
import static com.smartbear.readyapi.client.assertions.Assertions.xPathContains;
import static com.smartbear.readyapi.client.assertions.Assertions.xQueryContains;

public class BaseRestRequest<RequestBuilderType extends RestRequestBuilder> implements TestStepBuilder<RestTestRequestStep>, RestRequestBuilder<RequestBuilderType> {
    protected RestTestRequestStep testStep = new RestTestRequestStep();
    private List<Parameter> parameters = new ArrayList<>();
    private List<AssertionBuilder> assertionBuilders = new ArrayList<>();
    private Map<String, Object> headers = new HashMap<>();

    public enum ParameterType {
        MATRIX, HEADER, QUERY, PATH
    }

    public BaseRestRequest(String uri, TestSteps.HttpMethod method) {
        testStep.setURI(uri);
        testStep.setMethod(method.toString());
    }

    @Override
    public RequestBuilderType named(String name) {
        testStep.setName(name);
        return (RequestBuilderType) this;
    }

    @Override
    public RequestBuilderType addQueryParameter(String parameterName, String value) {
        return addParameter(parameterName, value, ParameterType.QUERY);
    }

    @Override
    public RequestBuilderType addPathParameter(String parameterName, String value) {
        return addParameter(parameterName, value, ParameterType.PATH);
    }

    @Override
    public RequestBuilderType addMatrixParameter(String parameterName, String value) {
        return addParameter(parameterName, value, ParameterType.MATRIX);
    }

    @Override
    public RequestBuilderType addHeaderParameter(String parameterName, String value) {
        return addParameter(parameterName, value, ParameterType.HEADER);
    }

    @Override
    public RequestBuilderType addAssertion(AssertionBuilder assertionBuilder) {
        this.assertionBuilders.add(assertionBuilder);
        return (RequestBuilderType) this;
    }

    @Override
    public RequestBuilderType setAuthentication(AuthenticationBuilder authenticationBuilder) {
        testStep.setAuthentication(authenticationBuilder.build());
        return (RequestBuilderType) this;
    }

    @Override
    public RequestBuilderType addHeader(String name, List<String> values) {
        List<String> headerValues = (List<String>) this.headers.get(name);
        if (headerValues == null) {
            headerValues = new ArrayList<>();
            this.headers.put(name, headerValues);
        }
        headerValues.addAll(values);
        return (RequestBuilderType) this;
    }

    @Override
    public RequestBuilderType addHeader(String name, String value) {
        return addHeader(name, Collections.singletonList(value));
    }

    protected RestRequestBuilder withURI(String uri) {
        testStep.setURI(uri);
        return (RequestBuilderType) this;
    }

    /**
     * Set timeout using property expansion
     */
    @Override
    public RequestBuilderType setTimeout(String timeout) {
        testStep.setTimeout(timeout);
        return (RequestBuilderType) this;
    }

    /**
     * Sets timeout value
     */
    @Override
    public RequestBuilderType setTimeout(int timeout) {
        testStep.setTimeout(String.valueOf(timeout));
        return (RequestBuilderType) this;
    }

    @Override
    public RequestBuilderType followRedirects() {
        testStep.setFollowRedirects(true);
        return (RequestBuilderType) this;
    }

    @Override
    public RequestBuilderType entitizeParameters() {
        testStep.setEntitizeParameters(true);
        return (RequestBuilderType) this;
    }

    @Override
    public RequestBuilderType postQueryString() {
        testStep.setPostQueryString(true);
        return (RequestBuilderType) this;
    }

    @Override
    public RestTestRequestStep build() {
        validateNotEmpty(testStep.getURI(), "No URI set, it's a mandatory parameter for REST Request");
        validateNotEmpty(testStep.getMethod(), "No HTTP method set, it's a mandatory parameter for REST Request");
        testStep.setType(TestStepTypes.REST_REQUEST.getName());
        testStep.setHeaders(headers);
        setAssertions(testStep);
        testStep.setParameters(parameters);

        return testStep;
    }

    private RequestBuilderType addParameter(String parameterName, String value, ParameterType type) {
        Parameter parameter = new Parameter();
        parameter.setName(parameterName);
        parameter.setValue(value);
        parameter.setType(type == ParameterType.PATH ? "TEMPLATE" : type.name());
        parameters.add(parameter);
        return (RequestBuilderType) this;
    }

    private void setAssertions(RestTestRequestStep testStep) {
        List<Assertion> assertions = new ArrayList<>();
        for (AssertionBuilder assertionBuilder : assertionBuilders) {
            assertions.add(((AbstractAssertionBuilder) assertionBuilder).build());
        }
        testStep.setAssertions(assertions);
    }

    @Override
    public RequestBuilderType assertJsonContent(String jsonPath, String expectedContent) {
        return addAssertion(jsonPathContent(jsonPath, expectedContent).allowWildcards());
    }

    @Override
    public RequestBuilderType assertJsonCount(String jsonPath, int expectedCount) {
        return addAssertion(jsonPathCount(jsonPath, expectedCount).allowWildcards());
    }

    @Override
    public RequestBuilderType assertContains(String content) {
        return addAssertion(contains(content));
    }

    @Override
    public RequestBuilderType assertNotContains(String content) {
        return addAssertion(notContains(content));
    }

    @Override
    public RequestBuilderType assertScript(String script) {
        return addAssertion(script(script));
    }

    @Override
    public RequestBuilderType assertXPath(String xpath, String expectedContent) {
        return addAssertion(xPathContains(xpath, expectedContent));
    }

    @Override
    public RequestBuilderType assertInvalidStatusCodes(Integer... statusCodes) {
        return addAssertion(invalidStatusCodes(statusCodes));
    }

    @Override
    public RequestBuilderType assertValidStatusCodes(Integer... statusCodes) {
        return addAssertion(validStatusCodes(statusCodes));
    }

    @Override
    public RequestBuilderType assertXQuery(String xquery, String expectedContent) {
        return addAssertion(xQueryContains(xquery, expectedContent));
    }

    @Override
    public RequestBuilderType assertResponseTime(int timeInMillis) {
        return addAssertion(responseSLA(timeInMillis));
    }
}
