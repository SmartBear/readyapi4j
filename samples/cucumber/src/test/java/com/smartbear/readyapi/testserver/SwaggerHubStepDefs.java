package com.smartbear.readyapi.testserver;

import com.google.inject.Inject;
import com.smartbear.readyapi.client.model.Assertion;
import com.smartbear.readyapi.client.model.JsonPathContentAssertion;
import com.smartbear.readyapi.client.model.ResponseSLAAssertion;
import com.smartbear.readyapi.client.model.RestTestRequestStep;
import com.smartbear.readyapi.client.model.ValidHttpStatusCodesAssertion;
import com.smartbear.readyapi.client.model.XPathContainsAssertion;
import com.smartbear.readyapi.client.teststeps.TestStepTypes;
import com.smartbear.readyapi.client.teststeps.TestSteps;
import com.smartbear.readyapi.testserver.cucumber.CucumberRecipeBuilder;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;

import java.net.URLEncoder;
import java.util.Arrays;

@ScenarioScoped
public class SwaggerHubStepDefs {

    private CucumberRecipeBuilder cucumberRecipeBuilder;
    private RestTestRequestStep testStep;
    private String owner;
    private String api;
    private String version;

    @Inject
    public SwaggerHubStepDefs(CucumberRecipeBuilder recipeBuilder) {
        cucumberRecipeBuilder = recipeBuilder;
    }

    public void noArguments() throws Throwable {
    }

    @When("^a request to the API listing is made$")
    public void aRequestToTheAPIListing() throws Throwable {
        testStep = new RestTestRequestStep();
        testStep.setURI("https://api.swaggerhub.com/apis");
        testStep.setMethod(TestSteps.HttpMethod.GET.name());
        testStep.setType(TestStepTypes.REST_REQUEST.getName());

        cucumberRecipeBuilder.addTestStep(testStep);
    }

    @Then("^a list of APIs should be returned within (\\d+)ms$")
    public void aListOfAPIsShouldBeReturned(int timeout) throws Throwable {
        buildEndpointFromParameters();

        XPathContainsAssertion assertion = new XPathContainsAssertion();
        assertion.setXpath("//*[local-name()='totalCount'] > 0");
        assertion.setExpectedContent("true");
        assertion.setType("XPath Match");

        addDefaultAssertions(timeout, assertion);
    }

    private void addDefaultAssertions(int timeout, Assertion contentAssertion) {
        ResponseSLAAssertion slaAssertion = new ResponseSLAAssertion();
        slaAssertion.setMaxResponseTime(String.valueOf(timeout));
        slaAssertion.setType("Response SLA");

        ValidHttpStatusCodesAssertion httpStatusCodesAssertion = new ValidHttpStatusCodesAssertion();
        httpStatusCodesAssertion.setValidStatusCodes(Arrays.asList("200"));
        httpStatusCodesAssertion.setType("Valid HTTP Status Codes");

        testStep.assertions(
                Arrays.asList(contentAssertion, httpStatusCodesAssertion, slaAssertion));
    }

    @Then("^an API definition should be returned within (\\d+)ms$")
    public void anApiDefinitionShouldBeReturned(int timeout) throws Throwable {

        buildEndpointFromParameters();

        JsonPathContentAssertion assertion = new JsonPathContentAssertion();
        assertion.setJsonPath("$.swagger");
        assertion.setExpectedContent("2.0");
        assertion.setType("JsonPath Match");

        addDefaultAssertions(timeout, assertion);
    }

    private void buildEndpointFromParameters() {
        if (owner != null) {
            testStep.setURI(testStep.getURI() + "/" + URLEncoder.encode(owner));

            if (api != null) {
                testStep.setURI(testStep.getURI() + "/" + URLEncoder.encode(api));

                if (version != null) {
                    testStep.setURI(testStep.getURI() + "/" + URLEncoder.encode(version));
                }
            }
        }
    }

    @Given("^an owner named (.+)$")
    public void anOwnerNamed(String name) throws Throwable {
        owner = name;
    }

    @And("^an api named (.+)$")
    public void anApiNamed(String name) throws Throwable {
        api = name;
    }

    @And("^a version named (.+)$")
    public void aVersionNamed(String name) throws Throwable {
        version = name;
    }
}


