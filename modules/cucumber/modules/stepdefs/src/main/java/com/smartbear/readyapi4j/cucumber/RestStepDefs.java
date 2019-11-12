package com.smartbear.readyapi4j.cucumber;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.smartbear.readyapi4j.assertions.Assertions;
import com.smartbear.readyapi4j.assertions.DefaultResponseSLAAssertionBuilder;
import com.smartbear.readyapi4j.assertions.ValidHttpStatusCodesAssertionBuilder;
import com.smartbear.readyapi4j.client.model.Assertion;
import com.smartbear.readyapi4j.client.model.Authentication;
import com.smartbear.readyapi4j.client.model.RestParameter;
import com.smartbear.readyapi4j.client.model.RestTestRequestStep;
import com.smartbear.readyapi4j.cucumber.hiptest.ActionWord;
import com.smartbear.readyapi4j.support.ContentUtils;
import com.smartbear.readyapi4j.teststeps.TestStepTypes;
import com.smartbear.readyapi4j.teststeps.restrequest.ParameterBuilder;
import cucumber.runtime.java.guice.ScenarioScoped;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Generic StepDefs for REST API Testing
 */

@ScenarioScoped
public class RestStepDefs {

    private final static Logger LOG = LoggerFactory.getLogger(RestStepDefs.class);

    private final CucumberRecipeBuilder builder;

    private String endpoint = "";
    private String method = "GET";
    private String path = "";
    private String requestBody;
    private String token;
    private String mediaType;

    private List<RestParameter> parameters = Lists.newArrayList();
    private Map<String, String> bodyValues = Maps.newHashMap();
    private List<Assertion> assertions = Lists.newArrayList();
    private RestTestRequestStep testStep;

    @Inject
    public RestStepDefs(CucumberRecipeBuilder recipeBuilder) {
        this.builder = recipeBuilder;
    }

    @Given("^the oAuth2 token (.*)$")
    @ActionWord( "the oAuth2 token \"oauth-token\"")
    public void theOauth2Token(String token) {
        this.token = CucumberUtils.stripQuotes(token);
    }

    @Given("^the API running at (.*)$")
    @ActionWord( "the API running at \"api-endpoint\"")
    public void theAPIRunningAt(String endpoint) {
        this.endpoint = CucumberUtils.stripQuotes(endpoint);
    }

    @When("^a (.*) request to ([^ ]*) is made$")
    @ActionWord( "a \"method\" request to \"path\" is made")
    public void aRequestToPathIsMade(String method, String path) {
        this.method = CucumberUtils.stripQuotes(method);
        this.path = CucumberUtils.stripQuotes(path);
    }

    @When("^a (.*) request is made$")
    @ActionWord( "a \"method\" request is made")
    public void aRequestIsMade(String method) {
        this.method = CucumberUtils.stripQuotes(method);
    }

    @Given("^the request body is$")
    @ActionWord( value = "the request body is", addFreetext = true)
    public void theRequestBodyIs(String requestBody) {
        this.requestBody = requestBody;
    }

    @Then("^a status code of (.*) is returned$")
    @ActionWord( "a status code of \"status-code\" is returned")
    public void aStatusCodeIsReturned(String statusCode) {
        aResponseIsReturnedWithin(statusCode,  "0");
    }

    @Then("^a (.*) response is returned$")
    @ActionWord( "a \"status-code\" response is returned")
    public void aResponseIsReturned(String statusCode) {
        aResponseIsReturnedWithin(statusCode, "0");
    }

    @Then("^a (.*) response is returned within (.*)ms$")
    @ActionWord( "a \"status-code\" response is returned withing \"milliseconds\"ms" )
    public void aResponseIsReturnedWithin(String statusCode, String timeoutString) {

        int timeout = Integer.parseInt(CucumberUtils.stripQuotes(timeoutString));

        if (timeout > 0) {
            assertions.add(DefaultResponseSLAAssertionBuilder.create().maxResponseTime(String.valueOf(timeout)));
        }

        addAssertion(ValidHttpStatusCodesAssertionBuilder.create().validStatusCodes(
                Arrays.asList(CucumberUtils.stripQuotes(statusCode))));
        pushRestRequest();
    }

    @Then("^the response body contains$")
    @ActionWord( value = "the response body contains", addFreetext = true)
    public void theResponseBodyContains(String responseBody) {
        addAssertion(Assertions.contains(responseBody).build());
    }

    @Then("^the response body matches$")
    @ActionWord( value = "the response body matches", addFreetext = true)
    public void theResponseBodyMatches(String responseBodyRegEx) {
        addAssertion(Assertions.matches(responseBodyRegEx).build());
    }

    @Given("^the (.*) parameter is (.*)$")
    @ActionWord(  "the \"name\" parameter is \"value\"")
    public void theParameterIs(String name, String value) {

        name = CucumberUtils.stripQuotes(name);
        value = CucumberUtils.stripQuotes(value);

        ParameterBuilder parameterBuilder = (endpoint + path).contains("{" + name + "}") ?
                ParameterBuilder.path(name, value) :
                ParameterBuilder.query(name, value);
        parameters.add(parameterBuilder.build());
    }

    @Given("^the (.*) header is (.*)$")
    @ActionWord(  "the \"name\" header is \"value\"")
    public void theHeaderIs(String name, String value) {
        parameters.add(ParameterBuilder.header(CucumberUtils.stripQuotes(name), CucumberUtils.stripQuotes(value)).build());
    }

    @Given("^the type is (.*)$")
    @ActionWord("the type is \"request-content-type\"")
    public void theTypeIs(String type) {
        this.mediaType = CucumberUtils.stripQuotes(type);
    }

    @Given("^the request expects (.*)")
    @ActionWord( "the request expects \"expected-content-type\"")
    public void theRequestExpects(String format) {
        theHeaderIs("Accept", ContentUtils.expandContentType(CucumberUtils.stripQuotes(format)));
    }

    @Then("^the response type is (.*)$")
    @ActionWord( "the response type is \"expected-content-type\"")
    public void theResponseTypeIs(String format) {
        addAssertion(Assertions.contentType(CucumberUtils.stripQuotes(format)).build());
    }

    @Then("^the response contains a (.*) header$")
    @ActionWord( "the response contains a \"header-name\" header")
    public void theResponseContainsHeader(String header) {
        addAssertion(Assertions.headerExists(CucumberUtils.stripQuotes(header)).build());
    }

    @Then("^the path (.*) equals (.*)$")
    @ActionWord( value = "the path \"json-path\" equals \"expected-value\"" )
    public void thePathEquals(String path, String content) {
        addAssertion(Assertions.json(CucumberUtils.stripQuotes(path), CucumberUtils.stripQuotes(content)).build());
    }

    @Then("^the path (.*) equals")
    @ActionWord( value = "the path \"json-path\" equals", addFreetext = true )
    public void thePathEqualsContent(String path, String content) {
        thePathMatches( path, content );
    }

    @Then("^the path (.*) matches (.*)$")
    @ActionWord( value = "the path \"json-path\" matches \"regex-expression\"")
    public void thePathMatches(String path, String content) {
        addAssertion(Assertions.jsonRegEx(CucumberUtils.stripQuotes(path), CucumberUtils.stripQuotes(content)).build());
    }

    @Then("^the path (.*) matches$")
    @ActionWord( value = "the path \"json-path\" matches", addFreetext = true)
    public void thePathMatchesContent(String path, String content) {
        thePathMatches( path, content );
    }

    @Then("^the path (.*) finds (.*) items?$")
    @ActionWord( value = "the path \"json-path\" finds \"number-of-items\"")
    public void thePathFinds(String path, String count) {
        addAssertion(Assertions.jsonCount(CucumberUtils.stripQuotes(path), CucumberUtils.stripQuotes(count)).build());
    }

    @Then("^the path (.*) exists$")
    @ActionWord( value = "the path \"json-path\" exists")
    public void thePathExists(String path) {
        addAssertion(Assertions.jsonExists(CucumberUtils.stripQuotes(path)).build());
    }

    @Then("^the xpath (.*) matches (.*)$")
    @ActionWord( value = "the xpath \"xpath\" equals \"expected-value\"" )
    public void theXPathMatches(String path, String content) {
        addAssertion(Assertions.xPathContains(CucumberUtils.stripQuotes(path), CucumberUtils.stripQuotes(content)).build());
    }

    @Then("^the xpath (.*) matches$")
    @ActionWord( value = "the xpath \"xpath\" equals", addFreetext = true )
    public void theXPathMatchesContent(String path, String content) {
        theXPathMatches( path, content );
    }

    @Then("^the response (.*) header is (.*)$")
    @ActionWord( value = "the response \"response-header\" header is \"expected-value\"" )
    public void theResponseHeaderIs(String header, String value) {
        addAssertion(Assertions.headerValue(CucumberUtils.stripQuotes(header), CucumberUtils.stripQuotes(value)).build());
    }

    @Then("^the response (.*) header matches (.*)$")
    @ActionWord( value = "the response \"response-header\" header matches \"regex-expression\"" )
    public void theResponseHeaderMatches(String header, String regex) {
        addAssertion(Assertions.headerMatches(CucumberUtils.stripQuotes(header), CucumberUtils.stripQuotes(regex)).build());
    }

    @Then("^the response body contains (.*)$")
    @ActionWord( value = "the response body contains \"value\"" )
    public void theResponseBodyContains2(String content) {
        theResponseBodyContains(CucumberUtils.stripQuotes(content));
    }

    public RestTestRequestStep pushRestRequest() {

        testStep = new RestTestRequestStep();
        testStep.setURI(endpoint + path);
        testStep.setMethod(method);
        testStep.setType(TestStepTypes.REST_REQUEST.getName());
        testStep.setAssertions(Lists.newArrayList());
        testStep.setParameters(Lists.newArrayList());

        if (requestBody != null) {
            testStep.setRequestBody(requestBody);
            testStep.setMediaType(mediaType == null ? "application/json" : mediaType);
        }

        if (token != null) {
            Authentication authentication = new Authentication();
            authentication.setAccessToken(token);
            authentication.setType("OAuth 2.0");
            testStep.setAuthentication(authentication);
        }

        if (!bodyValues.isEmpty()) {
            testStep.setMediaType("application/json");
            testStep.setRequestBody(ContentUtils.serializeContent(bodyValues, "application/json"));
        }

        if (!assertions.isEmpty()) {
            testStep.getAssertions().addAll(assertions);
        }

        if (!parameters.isEmpty()) {
            testStep.getParameters().addAll(parameters);
        }

        builder.addTestStep(testStep);
        return testStep;
    }

    public void addBodyValue(String name, String value) {
        bodyValues.put(name, value);
    }

    public void addParameter(RestParameter parameter) {
        parameters.add(parameter);
    }

    public void addAssertion(Assertion assertion) {
        if (testStep == null) {
            assertions.add(assertion);
        } else {
            testStep.getAssertions().add(assertion);
        }
    }

    public Map<String, String> getBodyValues() {
        return bodyValues;
    }

    public List<Assertion> getAssertions() {
        return assertions;
    }

    public List<RestParameter> getParameters() {
        return parameters;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
