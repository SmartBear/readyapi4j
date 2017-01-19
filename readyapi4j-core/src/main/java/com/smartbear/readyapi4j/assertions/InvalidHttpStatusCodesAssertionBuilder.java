package com.smartbear.readyapi4j.assertions;

import com.smartbear.readyapi.client.model.InvalidHttpStatusCodesAssertion;
import com.smartbear.readyapi4j.AssertionNames;
import com.smartbear.readyapi4j.Validator;

public class InvalidHttpStatusCodesAssertionBuilder extends ValidHttpStatusCodesAssertionBuilder<InvalidHttpStatusCodesAssertion> {

    public InvalidHttpStatusCodesAssertionBuilder() {
    }

    @Override
    public InvalidHttpStatusCodesAssertion build() {
        Validator.validateNotEmpty(statusCodes, "Missing status codes. Status codes are mandatory for InvalidHttpStatusCodesAssertion");
        InvalidHttpStatusCodesAssertion invalidHttpStatusCodesAssertion = new InvalidHttpStatusCodesAssertion();
        invalidHttpStatusCodesAssertion.setType(AssertionNames.INVALID_HTTP_STATUS_CODES);
        invalidHttpStatusCodesAssertion.setInvalidStatusCodes(statusCodes);
        if (name != null) {
            invalidHttpStatusCodesAssertion.setName(name);
        }
        return invalidHttpStatusCodesAssertion;
    }

    public final static InvalidHttpStatusCodesAssertion create() {
        InvalidHttpStatusCodesAssertion assertion = new InvalidHttpStatusCodesAssertion();
        assertion.setType(AssertionNames.INVALID_HTTP_STATUS_CODES);
        return assertion;
    }
}
