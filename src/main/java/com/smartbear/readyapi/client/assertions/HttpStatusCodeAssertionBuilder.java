package com.smartbear.readyapi.client.assertions;

import com.smartbear.readyapi.client.model.Assertion;

import java.util.List;

public interface HttpStatusCodeAssertionBuilder extends AssertionBuilder<Assertion> {
    HttpStatusCodeAssertionBuilder withStatusCode(int statusCode);

    HttpStatusCodeAssertionBuilder withStatusCode(String statusCode);

    HttpStatusCodeAssertionBuilder withStatusCodes(List<String> statusCodes);

    HttpStatusCodeAssertionBuilder withIntStatusCodes(List<Integer> statusCodes);

    HttpStatusCodeAssertionBuilder named(String assertionName);
}
