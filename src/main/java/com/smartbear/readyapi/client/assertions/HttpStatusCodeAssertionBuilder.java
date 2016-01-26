package com.smartbear.readyapi.client.assertions;

import com.smartbear.readyapi.client.model.Assertion;

import java.util.List;

public interface HttpStatusCodeAssertionBuilder extends AssertionBuilder<Assertion> {
    HttpStatusCodeAssertionBuilder addStatusCode(int statusCode);

    HttpStatusCodeAssertionBuilder addStatusCodes(List<Integer> statusCodes);
}
