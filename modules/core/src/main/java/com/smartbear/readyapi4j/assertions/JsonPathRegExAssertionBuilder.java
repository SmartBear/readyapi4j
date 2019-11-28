package com.smartbear.readyapi4j.assertions;

import com.smartbear.readyapi4j.client.model.JsonPathRegExAssertion;

import static com.smartbear.readyapi4j.support.Validations.validateNotEmpty;

public class JsonPathRegExAssertionBuilder implements JsonPathAssertionBuilder<JsonPathRegExAssertion> {
    private JsonPathRegExAssertion jsonPathRegExAssertion = new JsonPathRegExAssertion();

    public JsonPathRegExAssertionBuilder(String jsonPath, String regEx) {
        jsonPathRegExAssertion.setJsonPath(jsonPath);
        jsonPathRegExAssertion.setRegEx(regEx);
    }

    @Override
    public JsonPathRegExAssertionBuilder named(String name) {
        jsonPathRegExAssertion.setName(name);
        return this;
    }

    public JsonPathAssertionBuilder regEx( String regEx ) {
        jsonPathRegExAssertion.setRegEx(regEx);
        return this;
    }

    @Override
    public JsonPathRegExAssertion build() {
        validateNotEmpty(jsonPathRegExAssertion.getJsonPath(), "Missing JSON path, it's a mandatory parameter for JsonPathRegExAssertion");
        validateNotEmpty(jsonPathRegExAssertion.getRegEx(), "Missing regEx, it's a mandatory parameter for JsonPathRegExAssertion");
        jsonPathRegExAssertion.setType(AssertionNames.JSON_PATH_REGEX);
        return jsonPathRegExAssertion;
    }

    public final static JsonPathRegExAssertion create() {
        JsonPathRegExAssertion assertion = new JsonPathRegExAssertion();
        assertion.setType(AssertionNames.JSON_PATH_REGEX);
        return assertion;
    }
}
