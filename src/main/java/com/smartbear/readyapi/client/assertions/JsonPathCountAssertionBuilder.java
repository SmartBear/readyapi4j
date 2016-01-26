package com.smartbear.readyapi.client.assertions;

import com.smartbear.readyapi.client.model.JsonPathCountAssertion;

import static com.smartbear.readyapi.client.Validator.validateNotEmpty;

public class JsonPathCountAssertionBuilder extends AbstractAssertionBuilder<JsonPathCountAssertion> implements JsonPathAssertionBuilder<JsonPathCountAssertion> {
    private JsonPathCountAssertion jsonPathCountAssertion = new JsonPathCountAssertion();

    JsonPathCountAssertionBuilder(String jsonPath, int expectedCount) {
        jsonPathCountAssertion.setJsonPath(jsonPath);
        jsonPathCountAssertion.setExpectedCount(expectedCount);
    }

    @Override
    public JsonPathCountAssertionBuilder allowWildcards() {
        jsonPathCountAssertion.setAllowWildcards(true);
        return this;
    }

    @Override
    public JsonPathCountAssertion build() {
        validateNotEmpty(jsonPathCountAssertion.getJsonPath(), "Missing JSON path, it's a mandatory parameter for JsonPathCountAssertion");
        validateNotEmpty(jsonPathCountAssertion.getExpectedCount(), "Missing expected count, it's a mandatory parameter for JsonPathCountAssertion");
        jsonPathCountAssertion.setType("JsonPath Count");
        return jsonPathCountAssertion;
    }
}
