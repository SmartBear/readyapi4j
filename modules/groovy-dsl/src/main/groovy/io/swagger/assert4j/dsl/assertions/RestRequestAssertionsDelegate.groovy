package io.swagger.assert4j.dsl.assertions

import io.swagger.assert4j.assertions.Assertions

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

    void jsonExists(String jsonPath) {
        assertionBuilders.add(Assertions.jsonExists(jsonPath))
    }

    void jsonNotExists(String jsonPath) {
        assertionBuilders.add(Assertions.jsonNotExists(jsonPath))
    }

    /**
     * Used when expected value is provided as property expansion syntax to be evaluated (to true/false) at runtime.
     * @param jsonPath
     * @param expectedValue
     */
    void jsonExistence(String jsonPath, String expectedValue) {
        assertionBuilders.add(Assertions.jsonExistence(jsonPath, expectedValue))
    }
}
