package io.swagger.assert4j.assertions;

import io.swagger.assert4j.client.model.JsonPathCountAssertion;
import io.swagger.assert4j.AssertionNames;

import static io.swagger.assert4j.Validator.validateNotEmpty;

public class JsonPathCountAssertionBuilder implements JsonPathAssertionBuilder<JsonPathCountAssertion> {
    private JsonPathCountAssertion jsonPathCountAssertion = new JsonPathCountAssertion();

    public JsonPathCountAssertionBuilder(String jsonPath, int expectedCount) {
        jsonPathCountAssertion.setJsonPath(jsonPath);
        jsonPathCountAssertion.setExpectedCount(String.valueOf(expectedCount));
    }

    public JsonPathCountAssertionBuilder(String jsonPath, String expectedCount) {
        jsonPathCountAssertion.setJsonPath(jsonPath);
        jsonPathCountAssertion.setExpectedCount(expectedCount);
    }

    @Override
    public JsonPathCountAssertionBuilder named(String name) {
        jsonPathCountAssertion.setName(name);
        return this;
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
        jsonPathCountAssertion.setType(AssertionNames.JSON_PATH_COUNT);
        return jsonPathCountAssertion;
    }

    public final static JsonPathCountAssertion create() {
        JsonPathCountAssertion assertion = new JsonPathCountAssertion();
        assertion.setType(AssertionNames.JSON_PATH_COUNT);
        return assertion;
    }
}
