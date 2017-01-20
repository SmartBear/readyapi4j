package com.smartbear.readyapi4j.dsl.assertions

import com.smartbear.readyapi4j.assertions.AssertionBuilder

import static com.smartbear.readyapi4j.assertions.Assertions.json
import static com.smartbear.readyapi4j.assertions.Assertions.jsonCount

/**
 * Delegate for building Json assertions using more or less natural language.
 */
class JsonPathAssertionDelegate {

    private String jsonPath
    private List<AssertionBuilder> assertionBuilders

    JsonPathAssertionDelegate(String jsonPath, List<AssertionBuilder> assertionBuilders) {
        this.jsonPath = jsonPath
        this.assertionBuilders = assertionBuilders
    }

    void contains(String expectedContent) {
        assertionBuilders.add(json(jsonPath, expectedContent))
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
            assertionBuilders.add(jsonCount(jsonPath, times))
            return times
        }
    }
}
