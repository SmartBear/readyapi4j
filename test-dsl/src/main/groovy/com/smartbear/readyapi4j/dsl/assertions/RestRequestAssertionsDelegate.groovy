package com.smartbear.readyapi4j.dsl.assertions

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
}
