package io.swagger.assert4j.dsl.assertions

import io.swagger.assert4j.assertions.AssertionBuilder

import static io.swagger.assert4j.assertions.Assertions.xPathContains

/**
 * Delegate for building XPath assertions using more or less natural language.
 */
class XPathAssertionDelegate {

    private String xPath
    private List<AssertionBuilder> assertionBuilders

    XPathAssertionDelegate(String xPath, List<AssertionBuilder> assertionBuilders) {
        this.xPath = xPath
        this.assertionBuilders = assertionBuilders
    }

    void contains(String expectedContent) {
        assertionBuilders.add(xPathContains(xPath, expectedContent))
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
            assertionBuilders.add(xPathContains("count($xPath)", "$times"))
            return times
        }

    }

}
