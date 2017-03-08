package com.smartbear.readyapi4j.assertions;

import com.smartbear.readyapi.client.model.SimpleContainsAssertion;
import com.smartbear.readyapi4j.AssertionNames;
import com.smartbear.readyapi4j.Validator;

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
