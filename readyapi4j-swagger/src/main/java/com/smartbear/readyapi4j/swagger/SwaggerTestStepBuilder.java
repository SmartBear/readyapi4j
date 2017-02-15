package com.smartbear.readyapi4j.swagger;

import com.smartbear.readyapi4j.teststeps.TestSteps;
import com.smartbear.readyapi4j.teststeps.restrequest.RestRequestStepBuilder;
import io.swagger.models.HttpMethod;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import io.swagger.parser.util.SwaggerDeserializationResult;

import java.util.Arrays;

/**
 * Utility class for building RestRequestStepBuilders for operations in a Swagger 2.0 definition
 */

public class SwaggerTestStepBuilder {

    private final Swagger swagger;
    private String targetEndpoint;

    /**
     * Creates a SwaggerTestStepBuilder for the specified Swagger definition and target targetEndpoint
     *
     * @param swaggerUrl endpoint to the Swagger definition to use
     * @param targetEndpoint where the target API under test running
     * @throws IllegalArgumentException if the specified Swagger definition can not be parsed
     */

    public SwaggerTestStepBuilder(String swaggerUrl, String targetEndpoint) throws IllegalArgumentException {
        swagger = parseSwagger(swaggerUrl);
        this.targetEndpoint = targetEndpoint;
    }

    /**
     * Creates a SwaggerTestStepBuilder for the specified Swagger definition, uses the host and first scheme in the
     * definition for the target endpoint.
     *
     * @param swaggerUrl endpoint to the Swagger definition to use
     * @throws IllegalArgumentException if the specified Swagger definition can not be parsed
     */

    public SwaggerTestStepBuilder(String swaggerUrl) throws IllegalArgumentException {
        swagger = parseSwagger(swaggerUrl);
        this.targetEndpoint = swagger.getSchemes().get(0).toValue().toLowerCase() + "://" + swagger.getHost();
    }

    private Swagger parseSwagger(String swaggerUrl) throws IllegalArgumentException {

        Swagger swagger;SwaggerDeserializationResult result = new SwaggerParser().readWithInfo(swaggerUrl, null, true);
        swagger = result.getSwagger();
        if( swagger == null )
        {
            throw new IllegalArgumentException( "Failed to parse Swagger definition at [" + swaggerUrl + "]; " +
                Arrays.toString(result.getMessages().toArray()));
        }
        return swagger;
    }

    /**
     * Creates a RestRequestStepBuilder for the specified operationId
     *
     * @param operationId the operationId of an operation in the Swagger definition
     * @return a RestRequestStepBuilder with the correct path and method
     * @throws IllegalArgumentException if the operationId is not found in the Swagger definition
     */

    public RestRequestStepBuilder<RestRequestStepBuilder> operation(String operationId) {

        for( String path : swagger.getPaths().keySet()){
            Path p = swagger.getPaths().get( path );

            for(HttpMethod method: p.getOperationMap().keySet()) {

                Operation op = p.getOperationMap().get(method);
                if (operationId.equalsIgnoreCase(op.getOperationId())) {
                    return new RestRequestStepBuilder<>(
                        targetEndpoint + swagger.getBasePath() + path,
                        TestSteps.HttpMethod.valueOf(method.name().toUpperCase()));
                }
            }
        }

        throw new IllegalArgumentException("operationId [" + operationId + "] not found in Swagger definition" );
    }

    /**
     * Creates a RestRequestStepBuilder for an arbitrary path and method - the targetEndpoint and basePath will be prefixed
     * to the specified path.
     *
     * @param path the path to append to the targetEndpoint and basePath
     * @param method the HTTP method to use
     */

    public RestRequestStepBuilder<RestRequestStepBuilder> request(String path, TestSteps.HttpMethod method){
        return new RestRequestStepBuilder<>(targetEndpoint + swagger.getBasePath() + path, method);
    }

    /**
     * @return the parsed Swagger definition object
     */

    public Swagger getSwagger() {
        return swagger;
    }

    /**
     * @return the target endpoint of the API under test
     */

    public String getTargetEndpoint() {

        return targetEndpoint;
    }

    /**
     * @param targetEndpoint the target endpoint of the API under test
     */

    public void setTargetEndpoint(String targetEndpoint) {
        this.targetEndpoint = targetEndpoint;
    }
}
