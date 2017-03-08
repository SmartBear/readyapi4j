package com.smartbear.readyapi4j.teststeps.soaprequest;

import com.smartbear.readyapi4j.assertions.AssertionBuilder;
import com.smartbear.readyapi4j.assertions.DefaultNotSoapFaultAssertionBuilder;
import com.smartbear.readyapi4j.assertions.DefaultSchemaComplianceAssertionBuilder;
import com.smartbear.readyapi4j.assertions.DefaultSoapFaultAssertionBuilder;
import com.smartbear.readyapi4j.auth.AuthenticationBuilder;
import com.smartbear.readyapi.client.model.SoapParameter;
import com.smartbear.readyapi.client.model.SoapRequestTestStep;
import com.smartbear.readyapi4j.teststeps.TestStepTypes;
import com.smartbear.readyapi4j.teststeps.request.HttpRequestStepBuilder;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.smartbear.readyapi4j.Validator.validateNotEmpty;

public class SoapRequestStepBuilder extends HttpRequestStepBuilder<SoapRequestStepBuilder, SoapRequestTestStep> {

    private List<SoapParameter> parameters = new ArrayList<>();

    public SoapRequestStepBuilder() {
        super(new SoapRequestTestStep(), TestStepTypes.SOAP_REQUEST.getName());
    }

    @Override
    public SoapRequestTestStep build() {
        super.build();

        validateNotEmpty(getTestStep().getWsdl(), "Missing WSDL location");
        validateNotEmpty(getTestStep().getBinding(), "Missing WSDL Binding");
        validateNotEmpty(getTestStep().getOperation(), "Missing WSDL Operation");

        getTestStep().setParameters( parameters );

        return getTestStep();
    }

    public SoapRequestStepBuilder withWsdlAt(URL wsdlUrl) {
        getTestStep().setWsdl(wsdlUrl.toString());
        return this;
    }

    public SoapRequestStepBuilder forOperation(String operation) {
        getTestStep().setOperation(operation);
        return this;
    }

    public SoapRequestStepBuilder forBinding(String binding) {
        getTestStep().setBinding(binding);
        return this;
    }

    public SoapRequestStepBuilder withEndpoint(String endpoint) {
        getTestStep().setURI(endpoint);
        return this;
    }

    public SoapRequestStepBuilder assertSchemaCompliance() {
        return addAssertion(new DefaultSchemaComplianceAssertionBuilder());
    }

    public SoapRequestStepBuilder assertSoapFaultResponse() {
        return addAssertion(new DefaultSoapFaultAssertionBuilder());
    }

    public SoapRequestStepBuilder assertSoapOkResponse() {
        return addAssertion(new DefaultNotSoapFaultAssertionBuilder());
    }

    public SoapRequestStepBuilder withParameter(String name, String value) {
        SoapParameter parameter = new SoapParameter();
        parameter.setName(name);
        parameter.setValue(value);

        parameters.add(parameter);
        return this;
    }

    public SoapRequestStepBuilder withPathParameter(String path, String value) {
        SoapParameter parameter = new SoapParameter();
        parameter.setPath(path);
        parameter.setValue(value);

        parameters.add(parameter);
        return this;
    }

    /**
     * Overrides required for correct return type
     */

    @Override
    public SoapRequestStepBuilder withClientCertificate(String filePath) {
        return super.withClientCertificate(filePath);
    }

    @Override
    public SoapRequestStepBuilder withClientCertificatePassword(String password) {
        return super.withClientCertificatePassword(password);
    }

    @Override
    public SoapRequestStepBuilder named(String name) {
        return super.named(name);
    }

    @Override
    public SoapRequestStepBuilder addAssertion(AssertionBuilder assertionBuilder) {
        return super.addAssertion(assertionBuilder);
    }

    @Override
    public SoapRequestStepBuilder setAuthentication(AuthenticationBuilder authenticationBuilder) {
        return super.setAuthentication(authenticationBuilder);
    }

    @Override
    public SoapRequestStepBuilder accepts(String acceptHeader) {
        return super.accepts(acceptHeader);
    }

    @Override
    public SoapRequestStepBuilder acceptsJson() {
        return super.acceptsJson();
    }

    @Override
    public SoapRequestStepBuilder acceptsXml() {
        return super.acceptsXml();
    }

    @Override
    public SoapRequestStepBuilder withHeader(String name, List<String> values) {
        return super.withHeader(name, values);
    }

    @Override
    public SoapRequestStepBuilder withHeader(String name, String value) {
        return super.withHeader(name, value);
    }

    @Override
    public SoapRequestStepBuilder addHeader(String name, List<String> values) {
        return super.addHeader(name, values);
    }

    @Override
    public SoapRequestStepBuilder addHeader(String name, String value) {
        return super.addHeader(name, value);
    }

    @Override
    protected SoapRequestStepBuilder withURI(String uri) {
        return super.withURI(uri);
    }

    @Override
    public SoapRequestStepBuilder setTimeout(String timeout) {
        return super.setTimeout(timeout);
    }

    @Override
    public SoapRequestStepBuilder setTimeout(int timeout) {
        return super.setTimeout(timeout);
    }

    @Override
    public SoapRequestStepBuilder followRedirects() {
        return super.followRedirects();
    }

    @Override
    public SoapRequestStepBuilder entitizeParameters() {
        return super.entitizeParameters();
    }

    @Override
    public SoapRequestStepBuilder assertContains(String content) {
        return super.assertContains(content);
    }

    @Override
    public SoapRequestStepBuilder assertNotContains(String content) {
        return super.assertNotContains(content);
    }

    @Override
    public SoapRequestStepBuilder assertScript(String script) {
        return super.assertScript(script);
    }

    @Override
    public SoapRequestStepBuilder assertXPath(String xpath, String expectedContent) {
        return super.assertXPath(xpath, expectedContent);
    }

    @Override
    public SoapRequestStepBuilder assertInvalidStatusCodes(String... statusCodes) {
        return super.assertInvalidStatusCodes(statusCodes);
    }

    @Override
    public SoapRequestStepBuilder assertInvalidStatusCodes(Integer... statusCodes) {
        return super.assertInvalidStatusCodes(statusCodes);
    }

    @Override
    public SoapRequestStepBuilder assertValidStatusCodes(String... statusCodes) {
        return super.assertValidStatusCodes(statusCodes);
    }

    @Override
    public SoapRequestStepBuilder assertValidStatusCodes(Integer... statusCodes) {
        return super.assertValidStatusCodes(statusCodes);
    }

    @Override
    public SoapRequestStepBuilder assertXQuery(String xquery, String expectedContent) {
        return super.assertXQuery(xquery, expectedContent);
    }

    @Override
    public SoapRequestStepBuilder assertResponseTime(int timeInMillis) {
        return super.assertResponseTime(timeInMillis);
    }

    @Override
    public SoapRequestStepBuilder assertContentType(String contentType) {
        return super.assertContentType(contentType);
    }

    @Override
    public SoapRequestStepBuilder assertHeaderExists(String header) {
        return super.assertHeaderExists(header);
    }

    @Override
    public SoapRequestStepBuilder assertHeader(String header, String value) {
        return super.assertHeader(header, value);
    }
}
