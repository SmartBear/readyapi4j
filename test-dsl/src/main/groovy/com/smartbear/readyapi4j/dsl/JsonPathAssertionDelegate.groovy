package com.smartbear.readyapi4j.dsl

import com.smartbear.readyapi4j.assertions.AssertionBuilder
import com.smartbear.readyapi4j.assertions.JsonPathContentAssertionBuilder
import com.smartbear.readyapi4j.assertions.JsonPathCountAssertionBuilder

/**
 * Delegate for building Json assertions using more or less natural language.
 */
class JsonPathAssertionDelegate {

    private String jsonPath
    private List<AssertionBuilder> assertionBuilders
    private String expectedContent

    JsonPathAssertionDelegate(String jsonPath, List<AssertionBuilder> assertionBuilders) {
        this.jsonPath = jsonPath
        this.assertionBuilders = assertionBuilders
    }

    void contains(String expectedContent) {
        assertionBuilders.add(new JsonPathContentAssertionBuilder(jsonPath, expectedContent))
    }

    OccursDelegate occurs(int times) {
        return new OccursDelegate(times)
    }

    class OccursDelegate {

        private int times

        OccursDelegate(int times) {
            this.times = times
        }

        int getTimes() {
            assertionBuilders.add(new JsonPathCountAssertionBuilder(jsonPath, times))
            return times
        }

    }

}
