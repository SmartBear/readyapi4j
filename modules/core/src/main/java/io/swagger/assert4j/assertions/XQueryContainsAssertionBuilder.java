package io.swagger.assert4j.assertions;

import io.swagger.assert4j.client.model.XQueryContainsAssertion;

import static io.swagger.assert4j.support.Validations.validateNotEmpty;

public class XQueryContainsAssertionBuilder implements XQueryAssertionBuilder {
    private XQueryContainsAssertion xQueryContainsAssertion = new XQueryContainsAssertion();

    XQueryContainsAssertionBuilder(String xQuery, String expectedContent) {
        xQueryContainsAssertion.setXquery(xQuery);
        xQueryContainsAssertion.setExpectedContent(expectedContent);
    }

    @Override
    public XQueryContainsAssertionBuilder named(String name) {
        xQueryContainsAssertion.setName(name);
        return this;
    }

    @Override
    public XQueryContainsAssertionBuilder allowWildcards() {
        xQueryContainsAssertion.setAllowWildcards(true);
        return this;
    }

    @Override
    public XQueryContainsAssertion build() {
        validateNotEmpty(xQueryContainsAssertion.getXquery(), "Missing XQuery, it's a mandatory parameter for XQueryContainsAssertion");
        validateNotEmpty(xQueryContainsAssertion.getExpectedContent(), "Missing expected content, it's a mandatory parameter for XQueryContainsAssertion");
        xQueryContainsAssertion.setType(AssertionNames.XQUERY_MATCH);
        return xQueryContainsAssertion;
    }

    public final static XQueryContainsAssertion create() {
        XQueryContainsAssertion assertion = new XQueryContainsAssertion();
        assertion.setType(AssertionNames.XQUERY_MATCH);
        return assertion;
    }
}
