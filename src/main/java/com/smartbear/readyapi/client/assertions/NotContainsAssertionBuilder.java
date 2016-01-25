package com.smartbear.readyapi.client.assertions;

import com.smartbear.readyapi.client.model.SimpleContainsAssertion;

import static com.smartbear.readyapi.client.Validator.validateNotEmpty;

public class NotContainsAssertionBuilder extends DefaultContainsAssertionBuilder {
    public NotContainsAssertionBuilder(String token) {
        super(token);
    }

    @Override
    public SimpleContainsAssertion build() {
        validateNotEmpty(containsAssertion.getToken(), "Missing token, it's a mandatory parameter for NotContainsAssertion");
        containsAssertion.setType("Not Contains");
        return containsAssertion;
    }
}
