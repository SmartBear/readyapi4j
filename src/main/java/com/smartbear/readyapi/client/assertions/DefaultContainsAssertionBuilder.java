package com.smartbear.readyapi.client.assertions;

import com.smartbear.readyapi.client.model.SimpleContainsAssertion;

import static com.smartbear.readyapi.client.Validator.validateNotEmpty;

public class DefaultContainsAssertionBuilder extends AbstractAssertionBuilder<SimpleContainsAssertion> implements ContainsAssertionBuilder {
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
    public SimpleContainsAssertion build() {
        validateNotEmpty(containsAssertion.getToken(), "Missing token, it's a mandatory parameter for ContainsAssertion");
        containsAssertion.setType(Assertions.CONTAINS_ASSERTION_TYPE);
        return containsAssertion;
    }
}
