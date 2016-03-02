package com.smartbear.readyapi.client.assertions;

import com.smartbear.readyapi.client.model.InvalidHttpStatusCodesAssertion;

import static com.smartbear.readyapi.client.Validator.validateNotEmpty;

public class InvalidHttpStatusCodesAssertionBuilder extends ValidHttpStatusCodesAssertionBuilder<InvalidHttpStatusCodesAssertion> {

    InvalidHttpStatusCodesAssertionBuilder() {
    }

    @Override
    public InvalidHttpStatusCodesAssertion build() {
        validateNotEmpty(statusCodes, "Missing status codes. Status codes are mandatory for InvalidHttpStatusCodesAssertion");
        InvalidHttpStatusCodesAssertion invalidHttpStatusCodesAssertion = new InvalidHttpStatusCodesAssertion();
        invalidHttpStatusCodesAssertion.setType(Assertions.INVALID_HTTP_STATUS_CODES_TYPE);
        invalidHttpStatusCodesAssertion.setInvalidStatusCodes(statusCodes);
        return invalidHttpStatusCodesAssertion;
    }

    public final static InvalidHttpStatusCodesAssertion create(){
        InvalidHttpStatusCodesAssertion assertion = new InvalidHttpStatusCodesAssertion();
        assertion.setType(Assertions.INVALID_HTTP_STATUS_CODES_TYPE);
        return assertion;
    }
}
