package com.smartbear.readyapi4j.swagger;

import com.smartbear.readyapi4j.teststeps.TestSteps;
import com.smartbear.readyapi4j.teststeps.restrequest.RestRequestStepBuilder;
import com.smartbear.readyapi4j.teststeps.restrequest.RestRequestStepWithBodyBuilder;
import io.swagger.models.HttpMethod;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Scheme;
import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import io.swagger.parser.util.SwaggerDeserializationResult;

import java.util.Arrays;
import java.util.List;

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
        List<Scheme> schemes = swagger.getSchemes();
        this.targetEndpoint = schemes.isEmpty() ? "http": schemes.get(0).toValue().toLowerCase() + "://" + swagger.getHost();
    }

    private Swagger parseSwagger(String swaggerUrl) throws IllegalArgumentException {
        SwaggerDeserializationResult result = new SwaggerParser().readWithInfo(swaggerUrl, null, true);
        Swagger swagger = result.getSwagger();
        if( swagger == null ){
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
    public RestRequestStepBuilder<? extends RestRequestStepBuilder> operation(String operationId) {
        for( String path : swagger.getPaths().keySet()){
            Path swaggerPath = swagger.getPaths().get( path );

            for(HttpMethod method: swaggerPath.getOperationMap().keySet()) {
                Operation swaggerOperation = swaggerPath.getOperationMap().get(method);
                if (operationId.equalsIgnoreCase(swaggerOperation.getOperationId())) {
                    TestSteps.HttpMethod httpMethod = TestSteps.HttpMethod.valueOf(method.name().toUpperCase());
                    if( isHttpMethodWithBody( httpMethod )) {
                        return new RestRequestStepWithBodyBuilder(
                            targetEndpoint + swagger.getBasePath() + path, httpMethod
                        );
                    }
                    else {
                        return new RestRequestStepBuilder<>(
                            targetEndpoint + swagger.getBasePath() + path, httpMethod
                        );
                    }
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
    public RestRequestStepBuilder<? extends RestRequestStepBuilder> request(String path, TestSteps.HttpMethod method){
        String basePath = swagger.getBasePath();
        if( basePath == null ){
            basePath = "";
        } else if( basePath.endsWith("/")){
            basePath = basePath.substring(0, basePath.length()-1);
        }

        if( isHttpMethodWithBody( method )){
            return new RestRequestStepWithBodyBuilder(targetEndpoint + basePath + path, method);
        }
        else {
            return new RestRequestStepBuilder<>(targetEndpoint + basePath + path, method);
        }
    }

    private boolean isHttpMethodWithBody(TestSteps.HttpMethod method) {
        return method.equals( TestSteps.HttpMethod.POST ) || method.equals(TestSteps.HttpMethod.PUT) ||
            method.equals( TestSteps.HttpMethod.PATCH );
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
