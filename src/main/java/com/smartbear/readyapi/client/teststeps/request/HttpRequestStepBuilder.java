package com.smartbear.readyapi.client.teststeps.request;

import com.smartbear.readyapi.client.assertions.AssertionBuilder;
import com.smartbear.readyapi.client.auth.AuthenticationBuilder;
import com.smartbear.readyapi.client.teststeps.TestStepBuilder;

import java.util.List;

public interface HttpRequestStepBuilder<RequestBuilderType extends HttpRequestStepBuilder> extends TestStepBuilder {
    RequestBuilderType named(String name);

    RequestBuilderType addAssertion(AssertionBuilder assertionBuilder);

    RequestBuilderType addHeader(String name, List<String> values);

    RequestBuilderType addHeader(String name, String value);

    RequestBuilderType setTimeout(String timeout);

    /**
     * Sets timeout value
     */

    RequestBuilderType setTimeout(int timeout);

    RequestBuilderType entitizeParameters();

    RequestBuilderType followRedirects();

    RequestBuilderType setAuthentication(AuthenticationBuilder authenticationBuilder);

    RequestBuilderType assertContains(String content);

    RequestBuilderType assertNotContains(String content);

    RequestBuilderType assertScript(String script);

    RequestBuilderType assertXPath(String xpath, String expectedContent);

    RequestBuilderType assertXQuery(String xquery, String expectedContent);

    RequestBuilderType assertResponseTime(int timeInMillis);
}
