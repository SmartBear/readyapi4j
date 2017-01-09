package com.smartbear.readyapi4j.dsl

import com.smartbear.readyapi4j.assertions.AssertionBuilder
import com.smartbear.readyapi4j.assertions.XPathContainsAssertionBuilder

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
        assertionBuilders.add(new XPathContainsAssertionBuilder(xPath, expectedContent))
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
            assertionBuilders.add(new XPathContainsAssertionBuilder("count($xPath)", "$times"))
            return times
        }

    }

}
