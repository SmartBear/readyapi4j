package com.smartbear.readyapi.readyapi4j.cucumber;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.smartbear.readyapi.client.model.Assertion;
import com.smartbear.readyapi.client.model.Authentication;
import com.smartbear.readyapi.client.model.RestParameter;
import com.smartbear.readyapi.client.model.RestTestRequestStep;
import com.smartbear.readyapi.readyapi4j.cucumber.builders.Assertions;
import com.smartbear.readyapi.readyapi4j.cucumber.builders.Parameters;
import com.smartbear.readyapi.readyapi4j.cucumber.builders.Support;
import com.smartbear.readyapi4j.assertions.DefaultResponseSLAAssertionBuilder;
import com.smartbear.readyapi4j.assertions.ValidHttpStatusCodesAssertionBuilder;
import com.smartbear.readyapi4j.support.ContentUtils;
import com.smartbear.readyapi4j.teststeps.TestStepTypes;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;
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
    public void theOauth2Token(String token) throws Throwable {
        this.token = token;
    }

    @Given("^the API running at (.*)$")
    public void theAPIRunningAt(String endpoint) throws Throwable {
        this.endpoint = endpoint;
    }

    @When("^a (.*) request to ([^ ]*) is made$")
    public void aRequestToPathIsMade(String method, String path) throws Throwable {
        this.method = method;
        this.path = path;
    }

    @When("^a (.*) request is made$")
    public void aRequestIsMade(String method) throws Throwable {
        this.method = method;
    }

    @Given("^the request body is$")
    public void theRequestBodyIs(String requestBody) throws Throwable {
        this.requestBody = requestBody;
    }

    @Then("^a status code of (\\d+) is returned$")
    public void aStatusCodeIsReturned(int statusCode) throws Throwable {
        aResponseIsReturnedWithin(statusCode, 0);
    }

    @Then("^a (\\d+) response is returned$")
    public void aResponseIsReturned(int statusCode) throws Throwable {
        aResponseIsReturnedWithin(statusCode, 0);
    }

    @Then("^a (\\d+) response is returned within (\\d+)ms$")
    public void aResponseIsReturnedWithin(int statusCode, int timeout) throws Throwable {

        if( timeout > 0 ) {
            assertions.add(DefaultResponseSLAAssertionBuilder.create().maxResponseTime(String.valueOf(timeout)));
        }

        addAssertion(ValidHttpStatusCodesAssertionBuilder.create().validStatusCodes(Arrays.asList(String.valueOf(statusCode))));
        pushRestRequest();
    }

    @Then("^the response body contains$")
    public void theResponseBodyContains(String responseBody) throws Throwable {
        addAssertion( Assertions.bodyContains( responseBody ));
    }

    @Then("^the response body matches$")
    public void theResponseBodyMatches(String responseBodyRegEx) throws Throwable {
        addAssertion( Assertions.bodyMatches( responseBodyRegEx ));
    }

    @Given("^the (.*) parameter is (.*)$")
    public void theParameterIs(String name, String value) throws Throwable {

        RestParameter.TypeEnum type = (endpoint+path).contains("{" + name + "}") ?
            RestParameter.TypeEnum.PATH :
            RestParameter.TypeEnum.QUERY;
        parameters.add( Parameters.buildParameter(type, name, value));
    }

    @Given("^the (.*) header is (.*)$")
    public void theHeaderIs(String name, String value) throws Throwable {
        parameters.add( Parameters.headerParameter(name,value));
    }

    @Given("^the type is (.*)$")
    public void theTypeIs(String type) throws Throwable {
        this.mediaType = type;
    }

    @Given("^the request expects (.*)")
    public void theRequestExpects(String format) throws Throwable {
        theHeaderIs("Accept", Support.expandContentType( format ));
    }

    @Then("^the response type is (.*)$")
    public void theResponseTypeIs(String format) throws Throwable {
        addAssertion( Assertions.responseType( format ));
    }

    @Then("^the response contains a (.*) header$")
    public void theResponseContainsHeader(String header) throws Throwable {
        addAssertion( Assertions.responseContainsHeader( header ));
    }

    @Then("^the response (.*) header is (.*)$")
    public void theResponseHeaderIs(String header, String value) throws Throwable {
        addAssertion( Assertions.responseHeader( header, value ));
    }

    @Then("^the response body contains (.*)$")
    public void theResponseBodyContains2(String content) throws Throwable {
        theResponseBodyContains(content);
    }

    public RestTestRequestStep pushRestRequest() {

        testStep = new RestTestRequestStep();
        testStep.setURI(endpoint);
        testStep.setMethod(method);
        testStep.setType(TestStepTypes.REST_REQUEST.getName());

        if (requestBody != null) {
            testStep.setRequestBody(requestBody);
            testStep.setMediaType( mediaType == null ? "application/json" : mediaType);
        }

        if (token != null) {
            Authentication authentication = new Authentication();
            authentication.setAccessToken(token);
            authentication.setType("OAuth 2.0");
            testStep.setAuthentication(authentication);
        }

        if (!bodyValues.isEmpty()) {
            testStep.setMediaType("application/json");
            testStep.setRequestBody(ContentUtils.serializeContent(bodyValues,"application/json"));
        }

        if( !assertions.isEmpty() ) {
            testStep.getAssertions().addAll(assertions);
        }

        if( !parameters.isEmpty() ){
            testStep.getParameters().addAll(parameters);
        }

        builder.addTestStep( testStep );
        return testStep;
    }

    public void addBodyValue( String name, String value ){
        bodyValues.put( name, value );
    }

    public void addParameter( RestParameter parameter ){
        parameters.add( parameter );
    }

    public void addAssertion( Assertion assertion ){
        if( testStep == null ) {
            assertions.add(assertion);
        }
        else {
            testStep.getAssertions().add( assertion );
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
