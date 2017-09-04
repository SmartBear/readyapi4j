package io.swagger.assert4j.teststeps.restrequest;

import io.swagger.assert4j.client.model.RestParameter;
import io.swagger.assert4j.client.model.RestTestRequestStep;
import io.swagger.assert4j.assertions.AssertionBuilder;
import io.swagger.assert4j.auth.AuthenticationBuilder;
import io.swagger.assert4j.support.HttpHeaders;
import io.swagger.assert4j.teststeps.TestStepTypes;
import io.swagger.assert4j.teststeps.TestSteps;
import io.swagger.assert4j.teststeps.request.HttpRequestStepBuilder;

import java.util.ArrayList;
import java.util.List;

import static io.swagger.assert4j.Validator.validateNotEmpty;
import static io.swagger.assert4j.assertions.Assertions.jsonContent;
import static io.swagger.assert4j.assertions.Assertions.jsonCount;
import static io.swagger.assert4j.assertions.Assertions.jsonExists;
import static io.swagger.assert4j.assertions.Assertions.jsonNotExists;

public class RestRequestStepBuilder<RestRequestBuilderType extends RestRequestStepBuilder> extends HttpRequestStepBuilder<RestRequestBuilderType, RestTestRequestStep> {

    private List<RestParameter> parameters = new ArrayList<>();

    public RestRequestStepBuilder(String uri, TestSteps.HttpMethod method) {
        super(new RestTestRequestStep(), TestStepTypes.REST_REQUEST.getName());
        getTestStep().setURI(uri);
        getTestStep().setMethod(method.toString());
    }

    public RestRequestBuilderType addQueryParameter(String parameterName, String value) {
        return addParameter(parameterName, value, RestParameter.TypeEnum.QUERY);
    }

    public RestRequestBuilderType addPathParameter(String parameterName, String value) {
        return addParameter(parameterName, value, RestParameter.TypeEnum.PATH);
    }

    public RestRequestBuilderType addMatrixParameter(String parameterName, String value) {
        return addParameter(parameterName, value, RestParameter.TypeEnum.MATRIX);
    }

    public RestRequestBuilderType addHeaderParameter(String parameterName, String value) {
        return addParameter(parameterName, value, RestParameter.TypeEnum.HEADER);
    }

    public RestRequestBuilderType withParameters(ParameterBuilder... parameterBuilders) {
        for (ParameterBuilder parameterBuilder : parameterBuilders) {
            parameterBuilder.addParameter(this);
        }

        return (RestRequestBuilderType) this;
    }

    public void addParameter(ParameterBuilder parameterBuilder) {
        parameterBuilder.addParameter(this);
    }

    public RestRequestBuilderType withParameter(String parameterName, String value) {
        if (getTestStep().getURI().toLowerCase().contains("{" + parameterName.toLowerCase() + "}")) {
            return withPathParameter(parameterName, value);
        } else if (HttpHeaders.isKnownHeader(parameterName)) {
            return withHeaderParameter(parameterName, value);
        } else {
            return withQueryParameter(parameterName, value);
        }
    }

    public RestRequestBuilderType withQueryParameter(String parameterName, String value) {
        return addQueryParameter(parameterName, value);
    }

    public RestRequestBuilderType withPathParameter(String parameterName, String value) {
        return addPathParameter(parameterName, value);
    }

    public RestRequestBuilderType withMatrixParameter(String parameterName, String value) {
        return addMatrixParameter(parameterName, value);
    }

    public RestRequestBuilderType withHeaderParameter(String parameterName, String value) {
        return addHeaderParameter(parameterName, value);
    }

    @Override
    public RestRequestBuilderType withAuthorization(String authorizationValue) {
        return super.withAuthorization(authorizationValue);
    }

    protected RestRequestBuilderType addParameter(String parameterName, String value, RestParameter.TypeEnum type) {
        RestParameter parameter = new RestParameter();
        parameter.setName(parameterName);
        parameter.setValue(value);
        parameter.setType(type);

        parameters.add(parameter);
        return (RestRequestBuilderType) this;
    }

    public RestRequestBuilderType postQueryString() {
        getTestStep().setPostQueryString(true);
        return (RestRequestBuilderType) this;
    }

    public RestRequestBuilderType assertJsonContent(String jsonPath, String expectedContent) {
        return addAssertion(jsonContent(jsonPath, expectedContent).allowWildcards());
    }

    public RestRequestBuilderType assertJsonCount(String jsonPath, int expectedCount) {
        return addAssertion(jsonCount(jsonPath, expectedCount).allowWildcards());
    }

    public RestRequestBuilderType assertJsonPathExists(String jsonPath) {
        return addAssertion(jsonExists(jsonPath));
    }

    public RestRequestBuilderType assertJsonPathDoesNotExist(String jsonPath) {
        return addAssertion(jsonNotExists(jsonPath));
    }

    public RestRequestBuilderType get(String uri) {
        getTestStep().setMethod("GET");
        getTestStep().setURI(uri);

        return (RestRequestBuilderType) this;
    }

    public RestRequestBuilderType post(String uri) {
        getTestStep().setMethod("POST");
        getTestStep().setURI(uri);

        return (RestRequestBuilderType) this;
    }

    public RestRequestBuilderType put(String uri) {
        getTestStep().setMethod("PUT");
        getTestStep().setURI(uri);

        return (RestRequestBuilderType) this;
    }

    public RestRequestBuilderType delete(String uri) {
        getTestStep().setMethod("DELETE");
        getTestStep().setURI(uri);

        return (RestRequestBuilderType) this;
    }

    @Override
    public RestTestRequestStep build() {
        super.build();
        validateNotEmpty(getTestStep().getURI(), "No URI set, it's a mandatory parameter for REST Request");
        validateNotEmpty(getTestStep().getMethod(), "No HTTP method set, it's a mandatory parameter for REST Request");

        getTestStep().setParameters(parameters);

        return getTestStep();
    }

    /**
     * Overrides required for correct return type
     */

    @Override
    public RestRequestBuilderType assertHeader(String header, String value) {
        return super.assertHeader(header, value);
    }

    @Override
    public RestRequestBuilderType withClientCertificate(String filePath) {
        return super.withClientCertificate(filePath);
    }

    @Override
    public RestRequestBuilderType withClientCertificatePassword(String password) {
        return super.withClientCertificatePassword(password);
    }

    @Override
    public RestRequestBuilderType named(String name) {
        return super.named(name);
    }

    @Override
    public RestRequestBuilderType addAssertion(AssertionBuilder assertionBuilder) {
        return super.addAssertion(assertionBuilder);
    }

    @Override
    public RestRequestBuilderType setAuthentication(AuthenticationBuilder authenticationBuilder) {
        return super.setAuthentication(authenticationBuilder);
    }

    @Override
    public RestRequestBuilderType accepts(String acceptHeader) {
        return super.accepts(acceptHeader);
    }

    @Override
    public RestRequestBuilderType acceptsJson() {
        return super.acceptsJson();
    }

    @Override
    public RestRequestBuilderType acceptsXml() {
        return super.acceptsXml();
    }

    @Override
    public RestRequestBuilderType withHeader(String name, List<String> values) {
        return super.withHeader(name, values);
    }

    @Override
    public RestRequestBuilderType withHeader(String name, String value) {
        return super.withHeader(name, value);
    }

    @Override
    public RestRequestBuilderType addHeader(String name, List<String> values) {
        return super.addHeader(name, values);
    }

    @Override
    public RestRequestBuilderType addHeader(String name, String value) {
        return super.addHeader(name, value);
    }

    @Override
    protected RestRequestBuilderType withURI(String uri) {
        return super.withURI(uri);
    }

    public RestRequestBuilderType withRequestBody(String body) {
        getTestStep().setRequestBody(body);
        return (RestRequestBuilderType) this;
    }

    @Override
    public RestRequestBuilderType setTimeout(String timeout) {
        return super.setTimeout(timeout);
    }

    @Override
    public RestRequestBuilderType setTimeout(int timeout) {
        return super.setTimeout(timeout);
    }

    @Override
    public RestRequestBuilderType followRedirects() {
        return super.followRedirects();
    }

    @Override
    public RestRequestBuilderType entitizeParameters() {
        return super.entitizeParameters();
    }

    @Override
    public RestRequestBuilderType assertContains(String content) {
        return super.assertContains(content);
    }

    @Override
    public RestRequestBuilderType assertNotContains(String content) {
        return super.assertNotContains(content);
    }

    @Override
    public RestRequestBuilderType assertScript(String script) {
        return super.assertScript(script);
    }

    @Override
    public RestRequestBuilderType assertXPath(String xpath, String expectedContent) {
        return super.assertXPath(xpath, expectedContent);
    }

    @Override
    public RestRequestBuilderType assertInvalidStatusCodes(String... statusCodes) {
        return super.assertInvalidStatusCodes(statusCodes);
    }

    @Override
    public RestRequestBuilderType assertInvalidStatusCodes(Integer... statusCodes) {
        return super.assertInvalidStatusCodes(statusCodes);
    }

    @Override
    public RestRequestBuilderType assertValidStatusCodes(String... statusCodes) {
        return super.assertValidStatusCodes(statusCodes);
    }

    @Override
    public RestRequestBuilderType assertValidStatusCodes(Integer... statusCodes) {
        return super.assertValidStatusCodes(statusCodes);
    }

    @Override
    public RestRequestBuilderType assertXQuery(String xquery, String expectedContent) {
        return super.assertXQuery(xquery, expectedContent);
    }

    @Override
    public RestRequestBuilderType assertResponseTime(int timeInMillis) {
        return super.assertResponseTime(timeInMillis);
    }

    @Override
    public RestRequestBuilderType assertContentType(String contentType) {
        return super.assertContentType(contentType);
    }

    @Override
    public RestRequestBuilderType assertHeaderExists(String header) {
        return super.assertHeaderExists(header);
    }

    public RestRequestBuilderType withAssertions(AssertionBuilder... assertionBuilders) {
        for (AssertionBuilder assertionBuilder : assertionBuilders) {
            addAssertion(assertionBuilder);
        }

        return (RestRequestBuilderType) this;
    }
}
