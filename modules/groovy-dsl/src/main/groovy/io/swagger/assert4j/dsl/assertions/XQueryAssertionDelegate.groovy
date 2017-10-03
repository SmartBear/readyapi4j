package io.swagger.assert4j.dsl.assertions

import io.swagger.assert4j.assertions.AssertionBuilder

import static io.swagger.assert4j.assertions.Assertions.xQueryContains

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
