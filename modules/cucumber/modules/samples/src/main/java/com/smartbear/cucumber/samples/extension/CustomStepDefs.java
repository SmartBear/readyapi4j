package com.smartbear.cucumber.samples.extension;

import com.smartbear.readyapi4j.cucumber.CucumberRecipeBuilder;
import com.smartbear.readyapi4j.cucumber.RestStepDefs;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.Given;

import javax.inject.Inject;

/**
 * Sample custom StepDef that adds an alternative way of specifying API endpoints
 */

@ScenarioScoped
public class CustomStepDefs {

    private final CucumberRecipeBuilder recipeBuilder;
    private final RestStepDefs restStepDefs;

    @Inject
    public CustomStepDefs(CucumberRecipeBuilder recipeBuilder, RestStepDefs restStepDefs) {
        this.recipeBuilder = recipeBuilder;
        this.restStepDefs = restStepDefs;
    }

    @Given("^an endpoint of (.*)$")
    public void anEndpointOf(String endpoint) {
        restStepDefs.setEndpoint(endpoint);
    }
}
