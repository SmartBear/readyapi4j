package io.swagger.assert4j.assertions;

import io.swagger.assert4j.client.model.Assertion;

import java.util.List;

public interface HttpStatusCodeAssertionBuilder extends AssertionBuilder<Assertion> {
    @Deprecated
    HttpStatusCodeAssertionBuilder addStatusCode(int statusCode);

    @Deprecated
    HttpStatusCodeAssertionBuilder addStatusCodes(List<Integer> statusCodes);

    HttpStatusCodeAssertionBuilder withStatusCode(int statusCode);

    HttpStatusCodeAssertionBuilder withStatusCode(String statusCode);

    HttpStatusCodeAssertionBuilder withStatusCodes(List<String> statusCodes);

    HttpStatusCodeAssertionBuilder withIntStatusCodes(List<Integer> statusCodes);

    HttpStatusCodeAssertionBuilder named(String assertionName);
}
