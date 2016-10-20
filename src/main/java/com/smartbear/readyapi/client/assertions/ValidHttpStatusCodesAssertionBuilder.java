package com.smartbear.readyapi.client.assertions;

import com.smartbear.readyapi.client.model.Assertion;
import com.smartbear.readyapi.client.model.ValidHttpStatusCodesAssertion;

import java.util.ArrayList;
import java.util.List;

import static com.smartbear.readyapi.client.Validator.validateNotEmpty;

public class ValidHttpStatusCodesAssertionBuilder<T extends Assertion> extends AbstractAssertionBuilder<Assertion> implements HttpStatusCodeAssertionBuilder {
    protected List<String> statusCodes = new ArrayList<>();
    protected String name;

    @Override
    public ValidHttpStatusCodesAssertionBuilder withStatusCode(int statusCode) {
        statusCodes.add(String.valueOf(statusCode));
        return this;
    }

    @Override
    public ValidHttpStatusCodesAssertionBuilder withStatusCode(String statusCode){
        statusCodes.add(statusCode);
        return this;
    }

    @Override
    public ValidHttpStatusCodesAssertionBuilder withStatusCodes(List<String> statusCodes){
        this.statusCodes.addAll(statusCodes);
        return this;
    }

    @Override
    public ValidHttpStatusCodesAssertionBuilder withIntStatusCodes(List<Integer> statusCodes){
        this.statusCodes.addAll(covertListIntegersToString(statusCodes));
        return this;
    }

    private List<String> covertListIntegersToString(List<Integer> statusCodes) {
       List<String> result =  new ArrayList<>();
        for(int code: statusCodes){
            result.add(String.valueOf(code));
        }
        return result;

    }

    @Override
    public ValidHttpStatusCodesAssertionBuilder named(String name) {
        this.name = name;
        return this;
    }

    @Override
    public T build() {
        validateNotEmpty(statusCodes, "Missing status codes. Status codes are mandatory for ValidHttpStatusCodesAssertion");
        ValidHttpStatusCodesAssertion statusCodesAssertion = new ValidHttpStatusCodesAssertion();
        statusCodesAssertion.setType(Assertions.VALID_HTTP_STATUS_CODES_TYPE);
        statusCodesAssertion.setValidStatusCodes(statusCodes);
        if (name != null) {
            statusCodesAssertion.setName(name);
        }
        return (T) statusCodesAssertion;
    }

    public static <T extends Assertion> T create() {
        ValidHttpStatusCodesAssertion assertion = new ValidHttpStatusCodesAssertion();
        assertion.setType(Assertions.VALID_HTTP_STATUS_CODES_TYPE);
        return (T) assertion;
    }
}
