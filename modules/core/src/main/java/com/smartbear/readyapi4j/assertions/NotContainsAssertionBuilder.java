package com.smartbear.readyapi4j.assertions;

import com.smartbear.readyapi4j.client.model.SimpleContainsAssertion;

import static com.smartbear.readyapi4j.support.Validations.validateNotEmpty;

public class NotContainsAssertionBuilder extends DefaultContainsAssertionBuilder {

    public NotContainsAssertionBuilder(String token) {
        super(token);
    }

    @Override
    public SimpleContainsAssertion build() {
        validateNotEmpty(containsAssertion.getToken(), "Missing token, it's a mandatory parameter for NotContainsAssertion");
        containsAssertion.setType(AssertionNames.SIMPLE_NOT_CONTAINS);
        return containsAssertion;
    }

    public final static SimpleContainsAssertion create() {
        SimpleContainsAssertion assertion = new SimpleContainsAssertion();
        assertion.setType(AssertionNames.SIMPLE_NOT_CONTAINS);
        return assertion;
    }
}
