package com.smartbear.readyapi4j.cucumber;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.smartbear.readyapi4j.client.model.RestParameter;
import cucumber.runtime.CucumberException;
import cucumber.runtime.java.guice.ScenarioScoped;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Additional StepDefs for simplifying testing of Swagger-defined REST APIs
 */

@ScenarioScoped
public class SwaggerStepDefs {

    private final static Logger LOG = LoggerFactory.getLogger(SwaggerStepDefs.class);

    private static final ArrayList<String> PARAM_TYPES = Lists.newArrayList("query", "path", "header");

    private final SwaggerCache swaggerCache;
    private final RestStepDefs restStepDefs;
    private Swagger swagger;
    private Operation swaggerOperation;

    @Inject
    public SwaggerStepDefs(SwaggerCache swaggerCache, RestStepDefs restStepDefs) {
        this.swaggerCache = swaggerCache;
        this.restStepDefs = restStepDefs;
    }

    @Given("^the OAS definition at (.*)$")
    public void theOASDefinitionAt(String swaggerUrl) {
        theSwaggerDefinitionAt(CucumberUtils.stripQuotes(swaggerUrl));
    }

    @Given("^the Swagger definition at (.*)$")
    public void theSwaggerDefinitionAt(String swaggerUrl) {

        swagger = swaggerCache.getSwagger(CucumberUtils.stripQuotes(swaggerUrl));
        if( swagger == null ){
            throw new CucumberException( "Failed to read Swagger definition at [" + swaggerUrl + "]");
        }

        if (swagger.getHost() != null) {
            restStepDefs.setEndpoint(swagger.getSchemes().get(0).name().toLowerCase() + "://" + swagger.getHost());
            if (swagger.getBasePath() != null) {
                restStepDefs.setEndpoint(restStepDefs.getEndpoint() + swagger.getBasePath());
            }
        }
    }

    @When("^a request to ([^ ]*) is made$")
    public void aRequestToOperationIsMade(String operationId) throws Throwable {
        if (swagger == null) {
            throw new CucumberExecutionException("Missing Swagger definition");
        }

        operationId = CucumberUtils.stripQuotes(operationId);

        if (!findSwaggerOperation(operationId)) {
            throw new CucumberExecutionException("Could not find operation [" + operationId + "] in Swagger definition");
        }
    }

    private boolean findSwaggerOperation(String operationId) {
        swaggerOperation = null;

        for (String resourcePath : swagger.getPaths().keySet()) {
            Path path = swagger.getPath(resourcePath);
            for (HttpMethod httpMethod : path.getOperationMap().keySet()) {
                Operation operation = path.getOperationMap().get(httpMethod);
                if (operationId.equalsIgnoreCase(operation.getOperationId())) {
                    restStepDefs.setMethod(httpMethod.name().toUpperCase());
                    restStepDefs.setPath(resourcePath);
                    swaggerOperation = operation;
                }
            }
        }

        return swaggerOperation != null;
    }

    @Then("^the response is (.*)$")
    public void theResponseIs(String responseDescription) {
        if (swaggerOperation == null) {
            throw new CucumberExecutionException("missing swagger operation for request");
        }

        responseDescription = CucumberUtils.stripQuotes(responseDescription);

        for (String responseCode : swaggerOperation.getResponses().keySet()) {
            Response response = swaggerOperation.getResponses().get(responseCode);
            if (responseDescription.equalsIgnoreCase(response.getDescription())) {
                restStepDefs.aResponseIsReturned(responseCode);
            }
        }
    }

    @Given("^([^ ]*) is (.*)$")
    public void parameterIs(String name, String value) {

        if (swaggerOperation != null) {

            name = CucumberUtils.stripQuotes(name);
            value = CucumberUtils.stripQuotes(value);

            for (io.swagger.models.parameters.Parameter parameter : swaggerOperation.getParameters()) {
                if (parameter.getName().equalsIgnoreCase(name)) {
                    String type = parameter.getIn();
                    if (PARAM_TYPES.contains(type)) {
                        restStepDefs.addParameter(
                                new RestParameter().type(RestParameter.TypeEnum.valueOf(type.toUpperCase())).name(name).value(value));
                    } else if (type.equals("body")) {
                        restStepDefs.setRequestBody(value);
                    }

                    return;
                }
            }
        }

        restStepDefs.addBodyValue(name, value);
    }

    @Given("^([^ ]*) is$")
    public void parameterIsBlob(String name, String value) throws Throwable {
        parameterIs(name, value);
    }
}
