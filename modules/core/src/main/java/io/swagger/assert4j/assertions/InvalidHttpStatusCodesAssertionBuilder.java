package io.swagger.assert4j.assertions;

import io.swagger.assert4j.client.model.InvalidHttpStatusCodesAssertion;
import io.swagger.assert4j.support.Validations;

import java.util.ArrayList;
import java.util.List;

public class InvalidHttpStatusCodesAssertionBuilder implements HttpStatusCodeAssertionBuilder {
    protected List<String> statusCodes = new ArrayList<>();
    protected String name;

    @Override
    public InvalidHttpStatusCodesAssertionBuilder addStatusCode(int statusCode) {
        statusCodes.add(String.valueOf(statusCode));
        return this;
    }

    @Override
    public InvalidHttpStatusCodesAssertionBuilder addStatusCodes(List<Integer> statusCodes) {
        this.statusCodes.addAll(covertListIntegersToString(statusCodes));
        return this;
    }

    @Override
    public InvalidHttpStatusCodesAssertionBuilder withStatusCode(int statusCode) {
        statusCodes.add(String.valueOf(statusCode));
        return this;
    }

    @Override
    public InvalidHttpStatusCodesAssertionBuilder withStatusCode(String statusCode) {
        statusCodes.add(statusCode);
        return this;
    }

    @Override
    public InvalidHttpStatusCodesAssertionBuilder withStatusCodes(List<String> statusCodes) {
        this.statusCodes.addAll(statusCodes);
        return this;
    }

    @Override
    public InvalidHttpStatusCodesAssertionBuilder withIntStatusCodes(List<Integer> statusCodes) {
        this.statusCodes.addAll(covertListIntegersToString(statusCodes));
        return this;
    }

    @Override
    public InvalidHttpStatusCodesAssertionBuilder named(String name) {
        this.name = name;
        return this;
    }

    private List<String> covertListIntegersToString(List<Integer> statusCodes) {
        List<String> result = new ArrayList<>();
        for (int code : statusCodes) {
            result.add(String.valueOf(code));
        }
        return result;
    }

    @Override
    public InvalidHttpStatusCodesAssertion build() {
        Validations.validateNotEmpty(statusCodes, "Missing status codes. Status codes are mandatory for InvalidHttpStatusCodesAssertion");
        InvalidHttpStatusCodesAssertion invalidHttpStatusCodesAssertion = new InvalidHttpStatusCodesAssertion();
        invalidHttpStatusCodesAssertion.setType(AssertionNames.INVALID_HTTP_STATUS_CODES);
        invalidHttpStatusCodesAssertion.setInvalidStatusCodes(statusCodes);
        if (name != null) {
            invalidHttpStatusCodesAssertion.setName(name);
        }
        return invalidHttpStatusCodesAssertion;
    }

    public static InvalidHttpStatusCodesAssertion create() {
        InvalidHttpStatusCodesAssertion assertion = new InvalidHttpStatusCodesAssertion();
        assertion.setType(AssertionNames.INVALID_HTTP_STATUS_CODES);
        return assertion;
    }
}
