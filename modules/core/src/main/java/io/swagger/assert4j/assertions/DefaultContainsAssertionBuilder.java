package io.swagger.assert4j.assertions;

import io.swagger.assert4j.client.model.SimpleContainsAssertion;
import io.swagger.assert4j.AssertionNames;
import io.swagger.assert4j.Validator;

public class DefaultContainsAssertionBuilder implements ContainsAssertionBuilder {
    protected SimpleContainsAssertion containsAssertion = new SimpleContainsAssertion();

    public DefaultContainsAssertionBuilder(String token) {
        containsAssertion.setToken(token);
    }

    @Override
    public ContainsAssertionBuilder useRegEx() {
        containsAssertion.setUseRegexp(true);
        return this;
    }

    @Override
    public ContainsAssertionBuilder ignoreCase() {
        containsAssertion.setIgnoreCase(true);
        return this;
    }

    @Override
    public ContainsAssertionBuilder named(String name) {
        containsAssertion.setName(name);
        return this;
    }

    @Override
    public SimpleContainsAssertion build() {
        Validator.validateNotEmpty(containsAssertion.getToken(), "Missing token, it's a mandatory parameter for ContainsAssertion");
        containsAssertion.setType(AssertionNames.SIMPLE_CONTAINS);
        return containsAssertion;
    }

    public static SimpleContainsAssertion create() {
        SimpleContainsAssertion assertion = new SimpleContainsAssertion();
        assertion.setType(AssertionNames.SIMPLE_CONTAINS);
        return assertion;
    }
}
