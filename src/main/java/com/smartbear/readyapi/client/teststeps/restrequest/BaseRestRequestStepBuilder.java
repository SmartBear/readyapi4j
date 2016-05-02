package com.smartbear.readyapi.client.teststeps.restrequest;

import com.smartbear.readyapi.client.model.Parameter;
import com.smartbear.readyapi.client.model.RestTestRequestStep;
import com.smartbear.readyapi.client.teststeps.TestStepTypes;
import com.smartbear.readyapi.client.teststeps.TestSteps;
import com.smartbear.readyapi.client.teststeps.request.BaseRequestStepBuilder;

import static com.smartbear.readyapi.client.Validator.validateNotEmpty;
import static com.smartbear.readyapi.client.assertions.Assertions.jsonPathContent;
import static com.smartbear.readyapi.client.assertions.Assertions.jsonPathCount;

public class BaseRestRequestStepBuilder<RestRequestBuilderType extends RestRequestStepBuilder> extends BaseRequestStepBuilder<RestRequestBuilderType, RestTestRequestStep> implements RestRequestStepBuilder<RestRequestBuilderType> {

    public enum ParameterType {
        MATRIX, HEADER, QUERY, PATH
    }

    public BaseRestRequestStepBuilder(String uri, TestSteps.HttpMethod method) {
        super(new RestTestRequestStep(), TestStepTypes.REST_REQUEST.getName());
        testStep.setURI(uri);
        testStep.setMethod(method.toString());
    }

    public RestRequestBuilderType addQueryParameter(String parameterName, String value) {
        return addParameter(parameterName, value, ParameterType.QUERY);
    }

    public RestRequestBuilderType addPathParameter(String parameterName, String value) {
        return addParameter(parameterName, value, ParameterType.PATH);
    }

    public RestRequestBuilderType addMatrixParameter(String parameterName, String value) {
        return addParameter(parameterName, value, ParameterType.MATRIX);
    }

    public RestRequestBuilderType addHeaderParameter(String parameterName, String value) {
        return addParameter(parameterName, value, ParameterType.HEADER);
    }

    protected RestRequestBuilderType addParameter(String parameterName, String value, BaseRestRequestStepBuilder.ParameterType type) {
        Parameter parameter = new Parameter();
        parameter.setName(parameterName);
        parameter.setValue(value);
        parameter.setType(type.name());

        getParameters().add(parameter);
        return (RestRequestBuilderType) this;
    }

    public RestRequestBuilderType postQueryString() {
        testStep.setPostQueryString(true);
        return (RestRequestBuilderType) this;
    }

    public RestRequestBuilderType assertJsonContent(String jsonPath, String expectedContent) {
        return addAssertion(jsonPathContent(jsonPath, expectedContent).allowWildcards());
    }

    public RestRequestBuilderType assertJsonCount(String jsonPath, int expectedCount) {
        return addAssertion(jsonPathCount(jsonPath, expectedCount).allowWildcards());
    }

    @Override
    public RestRequestBuilderType get(String uri) {
        testStep.setMethod("GET");
        testStep.setURI(uri);

        return (RestRequestBuilderType) this;
    }

    @Override
    public RestRequestBuilderType post(String uri) {
        testStep.setMethod("POST");
        testStep.setURI(uri);

        return (RestRequestBuilderType) this;
    }

    @Override
    public RestRequestBuilderType put(String uri) {
        testStep.setMethod("PUT");
        testStep.setURI(uri);

        return (RestRequestBuilderType) this;
    }

    @Override
    public RestRequestBuilderType delete(String uri) {
        testStep.setMethod("DELETE");
        testStep.setURI(uri);

        return (RestRequestBuilderType) this;
    }

    public RestTestRequestStep build() {
        super.build();
        validateNotEmpty(testStep.getURI(), "No URI set, it's a mandatory parameter for REST Request");
        validateNotEmpty(testStep.getMethod(), "No HTTP method set, it's a mandatory parameter for REST Request");
        return testStep;
    }
}
