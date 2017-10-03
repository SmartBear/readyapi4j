package io.swagger.assert4j.support;

import org.junit.Assert;
import org.junit.Test;

public class ExpandersTest {

    @Test
    public void testFromXmlResponse() throws Exception {
        String value = Expanders.fromXmlResponse("Test name", "/some/xpath");
        Assert.assertEquals("${Test name#ResponseAsXml#/some/xpath}", value);
    }
}
