package com.smartbear.readyapi4j.assertions;

import com.smartbear.readyapi.client.model.SimpleContainsAssertion;

import static com.smartbear.readyapi4j.Validator.validateNotEmpty;

public class NotContainsAssertionBuilder extends DefaultContainsAssertionBuilder {

    public NotContainsAssertionBuilder(String token) {
        super(token);
    }

    @Override
    public SimpleContainsAssertion build() {
        validateNotEmpty(containsAssertion.getToken(), "Missing token, it's a mandatory parameter for NotContainsAssertion");
        containsAssertion.setType(Assertions.NOT_CONTAINS_TYPE);
        return containsAssertion;
    }

    public final static SimpleContainsAssertion create() {
        SimpleContainsAssertion assertion = new SimpleContainsAssertion();
        assertion.setType(Assertions.NOT_CONTAINS_TYPE);
        return assertion;
    }
}
