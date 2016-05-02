package com.smartbear.readyapi.client.teststeps.restrequest;

import com.smartbear.readyapi.client.teststeps.request.RequestBuilder;

public interface RestRequestBuilder<RestRequestBuilderType extends RestRequestBuilder> extends RequestBuilder<RestRequestBuilderType> {

    RestRequestBuilderType addQueryParameter(String parameterName, String value);

    RestRequestBuilderType addPathParameter(String parameterName, String value);

    RestRequestBuilderType addMatrixParameter(String parameterName, String value);

    RestRequestBuilderType addHeaderParameter(String parameterName, String value);

    RestRequestBuilderType postQueryString();

    /**
     * Assertion shortcuts
     */

    RestRequestBuilderType assertJsonContent(String jsonPath, String expectedContent);

    RestRequestBuilderType assertJsonCount(String jsonPath, int expectedCount);

    RestRequestBuilderType assertValidStatusCodes(Integer... statusCodes);

    RestRequestBuilderType assertInvalidStatusCodes(Integer... statusCodes);
}
