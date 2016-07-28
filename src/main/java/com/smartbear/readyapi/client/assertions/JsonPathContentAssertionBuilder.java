package com.smartbear.readyapi.client.assertions;

import com.smartbear.readyapi.client.model.JsonPathContentAssertion;

import static com.smartbear.readyapi.client.Validator.validateNotEmpty;

public class JsonPathContentAssertionBuilder extends AbstractAssertionBuilder<JsonPathContentAssertion> implements JsonPathAssertionBuilder<JsonPathContentAssertion> {
    private JsonPathContentAssertion jsonPathContentAssertion = new JsonPathContentAssertion();

    JsonPathContentAssertionBuilder(String jsonPath, String expectedContent) {
        jsonPathContentAssertion.setJsonPath(jsonPath);
        jsonPathContentAssertion.setExpectedContent(expectedContent);
    }

    @Override
    public JsonPathContentAssertionBuilder named(String name) {
        jsonPathContentAssertion.setName(name);
        return this;
    }

    @Override
    public JsonPathAssertionBuilder allowWildcards() {
        jsonPathContentAssertion.setAllowWildcards(true);
        return this;
    }

    @Override
    public JsonPathContentAssertion build() {
        validateNotEmpty(jsonPathContentAssertion.getJsonPath(), "Missing JSON path, it's a mandatory parameter for JsonPathContentAssertion");
        validateNotEmpty(jsonPathContentAssertion.getExpectedContent(), "Missing expected content, it's a mandatory parameter for JsonPathContentAssertion");
        jsonPathContentAssertion.setType(Assertions.JSON_PATH_MATCH_TYPE);
        return jsonPathContentAssertion;
    }

    public final static JsonPathContentAssertion create(){
        JsonPathContentAssertion assertion = new JsonPathContentAssertion();
        assertion.setType(Assertions.JSON_PATH_MATCH_TYPE);
        return assertion;
    }
}
