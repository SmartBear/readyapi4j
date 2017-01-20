package com.smartbear.readyapi4j.dsl.assertions

import com.smartbear.readyapi4j.assertions.AssertionBuilder

import static com.smartbear.readyapi4j.assertions.Assertions.xQueryContains

class XQueryAssertionDelegate {
    private String xQuery
    private List<AssertionBuilder> assertionBuilders

    XQueryAssertionDelegate(String xQuery, List<AssertionBuilder> assertionBuilders) {
        this.assertionBuilders = assertionBuilders
        this.xQuery = xQuery
    }

    void contains(String expectedContent) {
        assertionBuilders.add(xQueryContains(xQuery, expectedContent))
    }
}
