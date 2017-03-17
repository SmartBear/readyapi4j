package com.smartbear.readyapi.readyapi4j.cucumber;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.smartbear.readyapi.client.model.RestParameter;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;
import io.swagger.models.HttpMethod;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Response;
import io.swagger.models.Swagger;
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

    @Given("^the Swagger definition at (.*)$")
    public void theSwaggerDefinitionAt(String swaggerUrl) {

        swagger = swaggerCache.getSwagger(swaggerUrl);

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

        for (String responseCode : swaggerOperation.getResponses().keySet()) {
            Response response = swaggerOperation.getResponses().get(responseCode);
            if (responseDescription.equalsIgnoreCase(response.getDescription())) {
                restStepDefs.aResponseIsReturned(Integer.parseInt(responseCode));
            }
        }
    }

    @Given("^([^ ]*) is (.*)$")
    public void parameterIs(String name, String value) {

        if (swaggerOperation != null) {
            for (io.swagger.models.parameters.Parameter parameter : swaggerOperation.getParameters()) {
                if (parameter.getName().equalsIgnoreCase(name)) {
                    String type = parameter.getIn();
                    if (PARAM_TYPES.contains(type)) {
                        restStepDefs.addParameter(
                            new RestParameter().type(RestParameter.TypeEnum.valueOf(type.toUpperCase())).name(name).value( value));
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
