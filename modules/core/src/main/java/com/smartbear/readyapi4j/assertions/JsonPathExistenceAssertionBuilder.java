package com.smartbear.readyapi4j.assertions;

import com.smartbear.readyapi4j.client.model.JsonPathExistenceAssertion;

public class JsonPathExistenceAssertionBuilder implements JsonPathAssertionBuilder<JsonPathExistenceAssertion> {
    private final JsonPathExistenceAssertion jsonPathExistenceAssertion = new JsonPathExistenceAssertion();

    /**
     * @param jsonPath
     * @param shouldExist
     */
    public JsonPathExistenceAssertionBuilder(String jsonPath, boolean shouldExist) {
        jsonPathExistenceAssertion.setJsonPath(jsonPath);
        jsonPathExistenceAssertion.setExpectedContent(String.valueOf(shouldExist));
    }

    /**
     * Used when expected value is provided as a property expansion expression.
     *
     * @param jsonPath      jsonPath to evaluate the existence of.
     * @param expectedValue property expansion expression (evaluated to true/false) depending upon if the provided
     *                      JSONPath should exist in the response or not.
     */
    public JsonPathExistenceAssertionBuilder(String jsonPath, String expectedValue) {
        jsonPathExistenceAssertion.setJsonPath(jsonPath);
        jsonPathExistenceAssertion.setExpectedContent(expectedValue);
    }

    @Override
    public JsonPathExistenceAssertion build() {
        jsonPathExistenceAssertion.setType(AssertionNames.JSON_EXISTENCE);
        return jsonPathExistenceAssertion;
    }


    /**
     * Default value is false, which means it doesn't allow wildcards.
     *
     * @return JsonPathAssertionBuilder
     */
    @Override
    public JsonPathAssertionBuilder allowWildcards() {
        return this;
    }

    @Override
    public JsonPathAssertionBuilder named(String assertionName) {
        jsonPathExistenceAssertion.setName(assertionName);
        return this;
    }
}
