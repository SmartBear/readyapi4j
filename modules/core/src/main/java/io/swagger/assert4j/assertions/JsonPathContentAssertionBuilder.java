package io.swagger.assert4j.assertions;

import io.swagger.assert4j.client.model.JsonPathContentAssertion;

import static io.swagger.assert4j.support.Validations.validateNotEmpty;

public class JsonPathContentAssertionBuilder implements JsonPathAssertionBuilder<JsonPathContentAssertion> {
    private JsonPathContentAssertion jsonPathContentAssertion = new JsonPathContentAssertion();

    public JsonPathContentAssertionBuilder(String jsonPath, String expectedContent) {
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
        jsonPathContentAssertion.setType(AssertionNames.JSON_PATH_MATCH);
        return jsonPathContentAssertion;
    }

    public final static JsonPathContentAssertion create(){
        JsonPathContentAssertion assertion = new JsonPathContentAssertion();
        assertion.setType(AssertionNames.JSON_PATH_MATCH);
        return assertion;
    }
}
