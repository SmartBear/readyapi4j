package com.smartbear.readyapi.client.assertions;

import com.smartbear.readyapi.client.model.XPathContainsAssertion;

import static com.smartbear.readyapi.client.Validator.validateNotEmpty;

public class XPathContainsAssertionBuilder extends AbstractAssertionBuilder<XPathContainsAssertion> implements XPathAssertionBuilder {
    private XPathContainsAssertion xPathContainsAssertion = new XPathContainsAssertion();

    XPathContainsAssertionBuilder(String xPath, String expectedContent) {
        xPathContainsAssertion.setXpath(xPath);
        xPathContainsAssertion.setExpectedContent(expectedContent);
    }

    @Override
    public XPathContainsAssertionBuilder allowWildCards() {
        xPathContainsAssertion.setAllowWildcards(true);
        return this;
    }

    @Override
    public XPathContainsAssertionBuilder ignoreComments() {
        xPathContainsAssertion.setIgnoreComments(true);
        return this;
    }

    @Override
    public XPathContainsAssertionBuilder ignoreNamespaces() {
        xPathContainsAssertion.setIgnoreNamespaces(true);
        return this;
    }

    @Override
    public XPathContainsAssertion build() {
        validateNotEmpty(xPathContainsAssertion.getXpath(), "Missing Xpath, it's a mandatory parameter for XPathContainsAssertion");
        validateNotEmpty(xPathContainsAssertion.getExpectedContent(), "Missing expected content, it's a mandatory parameter for XPathContainsAssertion");
        xPathContainsAssertion.setType(Assertions.XPATH_MATCH_TYPE);
        return xPathContainsAssertion;
    }
}
