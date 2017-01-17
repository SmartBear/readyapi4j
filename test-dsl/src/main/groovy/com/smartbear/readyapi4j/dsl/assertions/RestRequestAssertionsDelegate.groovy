package com.smartbear.readyapi4j.dsl.assertions

class RestRequestAssertionsDelegate extends AbstractAssertionsDelegate {
    JsonPathAssertionDelegate jsonPath(String jsonPath) {
        return new JsonPathAssertionDelegate(jsonPath, assertionBuilders)
    }
}
