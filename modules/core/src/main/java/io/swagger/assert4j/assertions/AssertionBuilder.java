package io.swagger.assert4j.assertions;

import io.swagger.assert4j.client.model.Assertion;

public interface AssertionBuilder<T extends Assertion> {

    T build();
}
