package com.smartbear.readyapi.client.teststeps.restrequest;

import com.smartbear.readyapi.client.model.RestParameter;
import com.smartbear.readyapi.client.model.RestTestRequestStep;
import com.smartbear.readyapi.client.teststeps.TestStepTypes;
import com.smartbear.readyapi.client.teststeps.TestSteps;
import com.smartbear.readyapi.client.teststeps.request.HttpRequestStepBuilder;

import java.util.ArrayList;
import java.util.List;

import static com.smartbear.readyapi.client.Validator.validateNotEmpty;
import static com.smartbear.readyapi.client.assertions.Assertions.jsonPathContent;
import static com.smartbear.readyapi.client.assertions.Assertions.jsonPathCount;

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
        return addAssertion(jsonPathContent(jsonPath, expectedContent).allowWildcards());
    }

    public RestRequestBuilderType assertJsonCount(String jsonPath, int expectedCount) {
        return addAssertion(jsonPathCount(jsonPath, expectedCount).allowWildcards());
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

    public RestTestRequestStep build() {
        super.build();
        validateNotEmpty(getTestStep().getURI(), "No URI set, it's a mandatory parameter for REST Request");
        validateNotEmpty(getTestStep().getMethod(), "No HTTP method set, it's a mandatory parameter for REST Request");

        getTestStep().setParameters( parameters );

        return getTestStep();
    }
}
