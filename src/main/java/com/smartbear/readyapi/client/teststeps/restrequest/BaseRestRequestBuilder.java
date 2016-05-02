package com.smartbear.readyapi.client.teststeps.restrequest;

import com.smartbear.readyapi.client.model.Parameter;
import com.smartbear.readyapi.client.model.RestTestRequestStep;
import com.smartbear.readyapi.client.teststeps.TestStepTypes;
import com.smartbear.readyapi.client.teststeps.TestSteps;
import com.smartbear.readyapi.client.teststeps.request.BaseRequestBuilder;

import static com.smartbear.readyapi.client.Validator.validateNotEmpty;
import static com.smartbear.readyapi.client.assertions.Assertions.jsonPathContent;
import static com.smartbear.readyapi.client.assertions.Assertions.jsonPathCount;

public class BaseRestRequestBuilder<RestRequestBuilderType extends RestRequestBuilder> extends BaseRequestBuilder<RestRequestBuilderType, RestTestRequestStep> implements RestRequestBuilder<RestRequestBuilderType> {

    public enum ParameterType {
        MATRIX, HEADER, QUERY, PATH
    }

    public BaseRestRequestBuilder(String uri, TestSteps.HttpMethod method) {
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

    protected RestRequestBuilderType addParameter(String parameterName, String value, BaseRestRequestBuilder.ParameterType type) {
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

    public RestTestRequestStep build() {
        super.build();
        validateNotEmpty(testStep.getURI(), "No URI set, it's a mandatory parameter for REST Request");
        validateNotEmpty(testStep.getMethod(), "No HTTP method set, it's a mandatory parameter for REST Request");
        return testStep;
    }
}
