package com.smartbear.readyapi.client.teststeps.restrequest;

import com.smartbear.readyapi.client.assertions.AssertionBuilder;
import com.smartbear.readyapi.client.auth.AuthenticationBuilder;
import com.smartbear.readyapi.client.model.RestTestRequestStep;
import com.smartbear.readyapi.client.teststeps.TestStepBuilder;

import java.util.List;

public interface RestRequestBuilder<RestRequestBuilderType extends RestRequestBuilder> extends TestStepBuilder<RestTestRequestStep> {
    RestRequestBuilderType named(String name);

    RestRequestBuilderType addQueryParameter(String parameterName, String value);

    RestRequestBuilderType addPathParameter(String parameterName, String value);

    RestRequestBuilderType addMatrixParameter(String parameterName, String value);

    RestRequestBuilderType addHeaderParameter(String parameterName, String value);

    RestRequestBuilderType addAssertion(AssertionBuilder assertionBuilder);

    RestRequestBuilderType addHeader(String name, List<String> values);

    RestRequestBuilderType addHeader(String name, String value);

    RestRequestBuilderType setTimeout(String timeout);

    /**
     * Sets timeout value
     */
    RestRequestBuilderType setTimeout(int timeout);

    RestRequestBuilderType followRedirects();

    RestRequestBuilderType entitizeParameters();

    RestRequestBuilderType postQueryString();

    RestRequestBuilderType setAuthentication(AuthenticationBuilder authenticationBuilder);

    /**
     * Assertion shortcuts
     */

    RestRequestBuilderType assertJsonContent(String jsonPath, String expectedContent);

    RestRequestBuilderType assertJsonCount(String jsonPath, int expectedCount);

    RestRequestBuilderType assertContains(String content);

    RestRequestBuilderType assertNotContains(String content);

    RestRequestBuilderType assertScript(String script);

    RestRequestBuilderType assertXPath(String xpath, String expectedContent);

    RestRequestBuilderType assertXQuery(String xquery, String expectedContent);

    RestRequestBuilderType assertValidStatusCodes(Integer... statusCodes);

    RestRequestBuilderType assertInvalidStatusCodes(Integer... statusCodes);

    RestRequestBuilderType assertResponseTime(int timeInMillis);
}
