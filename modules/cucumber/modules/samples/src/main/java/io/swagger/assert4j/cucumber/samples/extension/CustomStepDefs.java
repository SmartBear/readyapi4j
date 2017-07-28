package io.swagger.assert4j.cucumber.samples.extension;

import io.swagger.assert4j.cucumber.CucumberRecipeBuilder;
import io.swagger.assert4j.cucumber.RestStepDefs;
import cucumber.api.java.en.Given;
import cucumber.runtime.java.guice.ScenarioScoped;

import javax.inject.Inject;

/**
 * Sample custom StepDef that adds an alternative way of specifying API endpoints
 */

@ScenarioScoped
public class CustomStepDefs {

    private final CucumberRecipeBuilder recipeBuilder;
    private final RestStepDefs restStepDefs;

    @Inject
    public CustomStepDefs(CucumberRecipeBuilder recipeBuilder, RestStepDefs restStepDefs ){
        this.recipeBuilder = recipeBuilder;
        this.restStepDefs = restStepDefs;
    }

    @Given("^an endpoint of (.*)$")
    public void anEndpointOf( String endpoint ) {
        restStepDefs.setEndpoint( endpoint );
    }
}
