package com.smartbear.readyapi4j.cucumber;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.smartbear.readyapi4j.client.model.RestParameter;
import cucumber.runtime.CucumberException;
import cucumber.runtime.java.guice.ScenarioScoped;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

/**
 * Additional StepDefs for simplifying testing of OAS/Swagger-defined REST APIs
 */

@ScenarioScoped
public class OASStepDefs {

    private final static Logger LOG = LoggerFactory.getLogger(OASStepDefs.class);

    private static final ArrayList<String> PARAM_TYPES = Lists.newArrayList("query", "path", "header");

    private final OASCache oasCache;
    private final RestStepDefs restStepDefs;
    private OpenAPI oas;
    private Operation oasOperation;

    @Inject
    public OASStepDefs(OASCache oasCache, RestStepDefs restStepDefs) {
        this.oasCache = oasCache;
        this.restStepDefs = restStepDefs;
    }

    @Given("^the OAS definition at (.*)$")
    public void theOASDefinitionAt(String swaggerUrl) {
        theSwaggerDefinitionAt(CucumberUtils.stripQuotes(swaggerUrl));
    }

    @Given("^the Swagger definition at (.*)$")
    public void theSwaggerDefinitionAt(String swaggerUrl) {

        oas = oasCache.getOAS(CucumberUtils.stripQuotes(swaggerUrl));
        if( oas == null ){
            throw new CucumberException( "Failed to read OAS/Swagger definition at [" + swaggerUrl + "]");
        }

        if (oas.getServers() != null && !oas.getServers().isEmpty()) {
            String url = oas.getServers().get(0).getUrl();
            if( url.endsWith("/")){
                url = url.substring(0, url.length()-1);
            }
            restStepDefs.setEndpoint(url);
        }
    }

    @When("^a request to ([^ ]*) with parameters$")
    public void aRequestToOperationWithParametersIsMade(String operationId, String parameters) throws Throwable {
        if (oas == null) {
            throw new CucumberExecutionException("Missing OAS/Swagger definition");
        }

        operationId = CucumberUtils.stripQuotes(operationId);

        if (!findOASOperation(operationId)) {
            throw new CucumberExecutionException("Could not find operation [" + operationId + "] in OAS/Swagger definition");
        }

        Properties properties = new Properties();
        properties.load( new StringReader( parameters ));
        for( String name : properties.stringPropertyNames()){
            parameterIs( name, properties.getProperty( name ));
        }
    }

    @When("^a request to ([^ ]*) with content")
    public void aRequestToOperationWithContentIsMade(String operationId, String content) throws Throwable {
        if (oas == null) {
            throw new CucumberExecutionException("Missing OAS/Swagger definition");
        }

        operationId = CucumberUtils.stripQuotes(operationId);

        if (!findOASOperation(operationId)) {
            throw new CucumberExecutionException("Could not find operation [" + operationId + "] in OAS/Swagger definition");
        }

       restStepDefs.setRequestBody( content );
    }

    @When("^a request to ([^ ]*) is made$")
    public void aRequestToOperationIsMade(String operationId) throws Throwable {
        if (oas == null) {
            throw new CucumberExecutionException("Missing OAS/Swagger definition");
        }

        operationId = CucumberUtils.stripQuotes(operationId);

        if (!findOASOperation(operationId)) {
            throw new CucumberExecutionException("Could not find operation [" + operationId + "] in OAS/Swagger definition");
        }
    }

    private boolean findOASOperation(String operationId) {
        oasOperation = null;

        Paths paths = oas.getPaths();
        for (String path : paths.keySet()) {
            Map<PathItem.HttpMethod, Operation> operations = paths.get(path).readOperationsMap();
            for (PathItem.HttpMethod httpMethod : operations.keySet()) {
                Operation operation = operations.get(httpMethod);
                if (operationId.equalsIgnoreCase(operation.getOperationId())) {
                    restStepDefs.setMethod(httpMethod.name().toUpperCase());
                    restStepDefs.setPath(path);
                    oasOperation = operation;
                }
            }
        }

        return oasOperation != null;
    }

    @Then("^the response is (.*)$")
    public void theResponseIs(String responseDescription) {
        if (oasOperation == null) {
            throw new CucumberExecutionException("missing OAS/Swagger operation for request");
        }

        responseDescription = CucumberUtils.stripQuotes(responseDescription);

        for (String responseCode : oasOperation.getResponses().keySet()) {
            ApiResponse response = oasOperation.getResponses().get(responseCode);
            if (responseDescription.equalsIgnoreCase(response.getDescription())) {
                restStepDefs.aResponseIsReturned(responseCode);
                return;
            }
        }

        throw new CucumberExecutionException("missing response match for [" + responseDescription + "]");
    }

    @Given("^([^ ]*) is (.*)$")
    public void parameterIs(String name, String value) {

        if (oasOperation != null) {

            name = CucumberUtils.stripQuotes(name);
            value = CucumberUtils.stripQuotes(value);

            for ( Parameter parameter : oasOperation.getParameters()) {
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
