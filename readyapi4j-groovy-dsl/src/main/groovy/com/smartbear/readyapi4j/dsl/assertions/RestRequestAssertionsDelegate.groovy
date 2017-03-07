package com.smartbear.readyapi4j.dsl.assertions

import com.smartbear.readyapi4j.assertions.Assertions

/**
 * The delegate to respond to commands inside 'asserting' closure of REST request
 */
class RestRequestAssertionsDelegate extends AbstractAssertionsDelegate {

    /**
     * Creates delegate for JSON path assertion.
     * @param jsonPath jsonPath expression
     * @return Delegate for JSON path assertion
     */
    JsonPathAssertionDelegate jsonPath(String jsonPath) {
        return new JsonPathAssertionDelegate(jsonPath, assertionBuilders)
    }

    void jsonPathExists(String jsonPath) {
        assertionBuilders.add(Assertions.jsonPathExists(jsonPath))
    }

    void jsonPathDoesNotExist(String jsonPath) {
        assertionBuilders.add(Assertions.jsonPathDoesNotExist(jsonPath))
    }

    /**
     * Used when expected value is provided as property expansion syntax to be evaluated (to true/false) at runtime.
     * @param jsonPath
     * @param expectedValue
     */
    void jsonPathExistence(String jsonPath, String expectedValue) {
        assertionBuilders.add(Assertions.jsonPathExistence(jsonPath, expectedValue))
    }
}
