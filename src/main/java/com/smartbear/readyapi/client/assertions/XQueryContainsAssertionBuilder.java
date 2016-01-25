package com.smartbear.readyapi.client.assertions;

import io.swagger.client.model.XQueryContainsAssertion;

import static com.smartbear.readyapi.client.Validator.validateNotEmpty;

public class XQueryContainsAssertionBuilder extends AbstractAssertionBuilder<XQueryContainsAssertion> implements XQueryAssertionBuilder {
    private XQueryContainsAssertion xQueryContainsAssertion = new XQueryContainsAssertion();

    XQueryContainsAssertionBuilder(String xQuery, String expectedContent) {
        xQueryContainsAssertion.setXquery(xQuery);
        xQueryContainsAssertion.setExpectedContent(expectedContent);
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
        xQueryContainsAssertion.setType("XQuery Match");
        return xQueryContainsAssertion;
    }
}
