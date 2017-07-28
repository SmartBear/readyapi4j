package io.swagger.assert4j.teststeps.request;

import io.swagger.assert4j.client.model.RequestTestStepBase;
import io.swagger.assert4j.assertions.AssertionBuilder;
import io.swagger.assert4j.assertions.Assertions;
import io.swagger.assert4j.attachments.RequestAttachmentBuilder;
import io.swagger.assert4j.auth.AuthenticationBuilder;
import io.swagger.assert4j.extractor.Extractor;
import io.swagger.assert4j.teststeps.TestStepBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.swagger.assert4j.assertions.Assertions.contains;
import static io.swagger.assert4j.assertions.Assertions.invalidStatusCodes;
import static io.swagger.assert4j.assertions.Assertions.maxResponseTime;
import static io.swagger.assert4j.assertions.Assertions.notContains;
import static io.swagger.assert4j.assertions.Assertions.script;
import static io.swagger.assert4j.assertions.Assertions.validStatusCodes;
import static io.swagger.assert4j.assertions.Assertions.xPathContains;
import static io.swagger.assert4j.assertions.Assertions.xQueryContains;

abstract public class HttpRequestStepBuilder<RequestBuilderType extends HttpRequestStepBuilder, RequestTestStepType extends RequestTestStepBase> implements TestStepBuilder {
    private final RequestTestStepType testStep;
    private List<AssertionBuilder> assertionBuilders = new ArrayList<>();
    private Map<String, Object> headers = new HashMap<>();
    private List<Extractor> extractors = new LinkedList<>();
    private List<RequestAttachmentBuilder> attachmentsBuilders = new ArrayList<>();

    protected HttpRequestStepBuilder(RequestTestStepType testStep, String type) {
        this.testStep = testStep;
        this.testStep.setType(type);
    }

    final protected RequestTestStepType getTestStep() {
        return testStep;
    }

    protected Map<String, Object> getHeaders() {
        return headers;
    }

    /**
     * Test Step specific Certificate file can be added on the TestServer in allowedFilePath directory. Otherwise it should be provided by the client.
     * Client will throw an exception if file doesn't exist on client and on server.
     *
     * @param filePath Certificate file path
     */
    public RequestBuilderType withClientCertificate(String filePath) {
        testStep.setClientCertificateFileName(filePath);
        return (RequestBuilderType) this;
    }

    public RequestBuilderType withClientCertificatePassword(String password) {
        testStep.setClientCertificatePassword(password);
        return (RequestBuilderType) this;
    }

    public RequestBuilderType withAuthorization(String authorizationValue) {
        return withHeader("Authorization", authorizationValue);
    }

    public RequestBuilderType named(String name) {
        testStep.setName(name);
        return (RequestBuilderType) this;
    }

    public RequestBuilderType addAssertion(AssertionBuilder assertionBuilder) {
        this.assertionBuilders.add(assertionBuilder);
        return (RequestBuilderType) this;
    }

    public RequestBuilderType setAuthentication(AuthenticationBuilder authenticationBuilder) {
        testStep.setAuthentication(authenticationBuilder.build());
        return (RequestBuilderType) this;
    }

    public RequestBuilderType accepts(String acceptHeader) {
        return addHeader("Accept", acceptHeader);
    }

    public RequestBuilderType acceptsJson() {
        return accepts("application/json");
    }

    public RequestBuilderType acceptsXml() {
        return accepts("application/xml");
    }

    public RequestBuilderType withHeader(String name, List<String> values) {
        return addHeader(name, values);
    }

    public RequestBuilderType withHeader(String name, String value) {
        return addHeader(name, value);
    }

    public RequestBuilderType addHeader(String name, List<String> values) {
        List<String> headerValues = (List<String>) this.headers.get(name);
        if (headerValues == null) {
            headerValues = new ArrayList<>();
            this.headers.put(name, headerValues);
        }
        headerValues.addAll(values);
        return (RequestBuilderType) this;
    }

    public RequestBuilderType addHeader(String name, String value) {
        return addHeader(name, Collections.singletonList(value));
    }

    protected RequestBuilderType withURI(String uri) {
        testStep.setURI(uri);
        return (RequestBuilderType) this;
    }

    /**
     * Set timeout using property expansion
     */
    public RequestBuilderType setTimeout(String timeout) {
        testStep.setTimeout(timeout);
        return (RequestBuilderType) this;
    }

    /**
     * Sets timeout value
     */
    public RequestBuilderType setTimeout(int timeout) {
        testStep.setTimeout(String.valueOf(timeout));
        return (RequestBuilderType) this;
    }

    public RequestBuilderType followRedirects() {
        testStep.setFollowRedirects(true);
        return (RequestBuilderType) this;
    }

    public RequestBuilderType entitizeParameters() {
        testStep.setEntitizeParameters(true);
        return (RequestBuilderType) this;
    }

    public RequestTestStepType build() {
        testStep.setHeaders(headers);
        setAssertions(testStep);
        setAttachments(testStep);

        return testStep;
    }

    private void setAssertions(RequestTestStepType testStep) {
        testStep.setAssertions(assertionBuilders
                .stream()
                .map(assertionBuilder -> assertionBuilder.build())
                .collect(Collectors.toList()));
    }

    private void setAttachments(RequestTestStepType testStep) {
        testStep.setAttachments(attachmentsBuilders
                .stream()
                .map(RequestAttachmentBuilder::build)
                .collect(Collectors.toList()));
    }

    public RequestBuilderType assertContains(String content) {
        return addAssertion(contains(content));
    }

    public RequestBuilderType assertNotContains(String content) {
        return addAssertion(notContains(content));
    }

    public RequestBuilderType assertScript(String script) {
        return addAssertion(script(script));
    }

    public RequestBuilderType assertXPath(String xpath, String expectedContent) {
        return addAssertion(xPathContains(xpath, expectedContent));
    }

    public RequestBuilderType assertInvalidStatusCodes(String... statusCodes) {
        return addAssertion(invalidStatusCodes(statusCodes));
    }

    public RequestBuilderType assertInvalidStatusCodes(Integer... statusCodes) {
        return addAssertion(invalidStatusCodes(statusCodes));
    }

    public RequestBuilderType assertValidStatusCodes(String... statusCodes) {
        return addAssertion(validStatusCodes(statusCodes));
    }

    public RequestBuilderType assertValidStatusCodes(Integer... statusCodes) {
        return addAssertion(validStatusCodes(statusCodes));
    }

    public RequestBuilderType assertXQuery(String xquery, String expectedContent) {
        return addAssertion(xQueryContains(xquery, expectedContent));
    }

    public RequestBuilderType assertResponseTime(int timeInMillis) {
        return addAssertion(maxResponseTime(timeInMillis));
    }

    public RequestBuilderType assertContentType(String contentType) {
        addAssertion(Assertions.contentType(contentType));
        return (RequestBuilderType) this;
    }

    public RequestBuilderType assertHeaderExists(String header) {
        addAssertion(Assertions.headerExists(header));
        return (RequestBuilderType) this;
    }

    public RequestBuilderType assertHeader(String header, String value) {
        addAssertion(Assertions.headerValue(header, value));
        return (RequestBuilderType) this;
    }

    public RequestBuilderType withExtractors(Extractor... argExtractors) {
        if (testStep.getName() == null) {
            throw new IllegalStateException("Test step name is required when using data extractors.");
        }
        extractors.addAll(Arrays.stream(argExtractors)
                .filter(extractor -> extractor != null)
                .collect(Collectors.toList()));
        extractors.forEach(extractor -> extractor.setSource(testStep.getName()));
        return (RequestBuilderType) this;
    }

    public List<Extractor> getExtractors() {
        return extractors;
    }

    public RequestBuilderType withAttachments(RequestAttachmentBuilder... builders) {
        attachmentsBuilders.addAll(Arrays.stream(builders)
                .filter(builder -> builder != null)
                .collect(Collectors.toList()));

        return (RequestBuilderType) this;
    }
}
