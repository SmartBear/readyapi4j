package com.smartbear.readyapi.client.assertions;

import io.swagger.client.model.Assertion;

import java.util.List;

public interface HttpStatusCodeAssertionBuilder extends AssertionBuilder<Assertion> {
    HttpStatusCodeAssertionBuilder addStatusCode(int statusCode);

    HttpStatusCodeAssertionBuilder addStatusCodes(List<Integer> statusCodes);
}
