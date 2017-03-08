package com.smartbear.readyapi.readyapi4j.cucumber.builders;

import com.smartbear.readyapi.client.model.GroovyScriptAssertion;
import com.smartbear.readyapi.client.model.JsonPathContentAssertion;
import com.smartbear.readyapi.client.model.RequestTestStepBase;
import com.smartbear.readyapi.client.model.ResponseSLAAssertion;
import com.smartbear.readyapi.client.model.SimpleContainsAssertion;
import com.smartbear.readyapi.client.model.ValidHttpStatusCodesAssertion;
import com.smartbear.readyapi.client.model.XPathContainsAssertion;

import java.util.List;

/**
 * Utility class with static method for building various types of assertions
 */

public class Assertions {

    public static <T extends RequestTestStepBase> T assertStatusCodeEquals(T testStep, List<String> statusCodes ){
        testStep.getAssertions().add( validStatusCodes( statusCodes ));
        return testStep;
    }

    public static ValidHttpStatusCodesAssertion validStatusCodes(List<String> statusCodes ){
        ValidHttpStatusCodesAssertion httpStatusCodesAssertion = new ValidHttpStatusCodesAssertion();
        httpStatusCodesAssertion.setValidStatusCodes(statusCodes);
        httpStatusCodesAssertion.setType("Valid HTTP Status Codes");
        return httpStatusCodesAssertion;
    }

    public static <T extends RequestTestStepBase> T assertResponseTime(T testStep, int timeout ){
        testStep.getAssertions().add( timeout( timeout ));
        return testStep;
    }

    public static ResponseSLAAssertion timeout(int timeout) {
        ResponseSLAAssertion slaAssertion = new ResponseSLAAssertion();
        slaAssertion.setMaxResponseTime(String.valueOf(timeout));
        slaAssertion.setType("Response SLA");
        return slaAssertion;
    }

    public static <T extends RequestTestStepBase> T assertContains(T testStep, String token ){
        testStep.getAssertions().add( bodyContains( token ));
        return testStep;
    }

    public static SimpleContainsAssertion bodyContains(String token) {
        SimpleContainsAssertion contentAssertion = new SimpleContainsAssertion();
        contentAssertion.setToken(token.trim());
        contentAssertion.setType("Contains");
        contentAssertion.setIgnoreCase(true);
        return contentAssertion;
    }

    public static <T extends RequestTestStepBase> T assertMatches(T testStep, String token ){
        testStep.getAssertions().add( bodyContains( token ));
        return testStep;
    }

    public static SimpleContainsAssertion bodyMatches(String responseBodyRegEx) {
        SimpleContainsAssertion contentAssertion = new SimpleContainsAssertion();
        contentAssertion.setToken(responseBodyRegEx.trim());
        contentAssertion.setType("Contains");
        contentAssertion.setUseRegexp(true);
        return contentAssertion;
    }

    public static <T extends RequestTestStepBase> T assertResponseType(T testStep, String contentType ){
        testStep.getAssertions().add( responseType( contentType ));
        return testStep;
    }

    public static GroovyScriptAssertion responseType(String contentType) {
        GroovyScriptAssertion scriptAssertion = new GroovyScriptAssertion();
        scriptAssertion.setType("Script Assertion");
        scriptAssertion.setScript(
            "assert messageExchange.responseHeaders[\"Content-Type\"].contains( \"" + Support.expandContentType(contentType) + "\")");
        return scriptAssertion;
    }

    public static <T extends RequestTestStepBase> T assertHeaderExists(T testStep, String header ){
        testStep.getAssertions().add( responseContainsHeader( header ));
        return testStep;
    }

    public static GroovyScriptAssertion responseContainsHeader(String header) {
        GroovyScriptAssertion scriptAssertion = new GroovyScriptAssertion();
        scriptAssertion.setType("Script Assertion");
        scriptAssertion.setScript(
            "assert messageExchange.responseHeaders.containsKey(\"" + header + "\")");

        return scriptAssertion;
    }

    public static <T extends RequestTestStepBase> T assertHeaderValue(T testStep, String header, String value ){
        testStep.getAssertions().add( responseHeader( header, value ));
        return testStep;
    }

    public static GroovyScriptAssertion responseHeader(String header, String value) {
        GroovyScriptAssertion scriptAssertion = new GroovyScriptAssertion();
        scriptAssertion.setType("Script Assertion");
        scriptAssertion.setScript(
            "assert messageExchange.responseHeaders[\"" + header + "\"].contains( \"" + value + "\")");

        return scriptAssertion;
    }

    public static <T extends RequestTestStepBase> T assertJsonValue(T testStep, String path, String value ){
        testStep.getAssertions().add( jsonPathContent( path, value ));
        return testStep;
    }

    public static JsonPathContentAssertion jsonPathContent( String path, String value ){
        JsonPathContentAssertion assertion = new JsonPathContentAssertion();
        assertion.setType( "JsonPath Match" );
        assertion.setExpectedContent( value );
        assertion.setJsonPath( path );
        return assertion;
    }

    public static <T extends RequestTestStepBase> T assertXmlValue(T testStep, String xpath, String value ){
        testStep.getAssertions().add( xpathContent( xpath, value ));
        return testStep;
    }

    public static XPathContainsAssertion xpathContent(String xpath, String value ){
        XPathContainsAssertion assertion = new XPathContainsAssertion();
        assertion.setType( "XPath Match" );
        assertion.setExpectedContent( value );
        assertion.setXpath( xpath );
        return assertion;
    }
}
