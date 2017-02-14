package com.smartbear.readyapi4j.swagger;

import com.smartbear.readyapi4j.teststeps.TestSteps;
import com.smartbear.readyapi4j.teststeps.restrequest.RestRequestStepBuilder;
import io.swagger.models.HttpMethod;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;

/**
 * Utility class for building RestRequestStepBuilders for operations in a Swagger 2.0 definition
 */

public class SwaggerTestStepBuilder {

    private final Swagger swagger;
    private final String host;

    /**
     * @param swaggerUrl endpoint to the Swagger definition to use
     * @param host host where the target API is running
     */

    public SwaggerTestStepBuilder(String swaggerUrl, String host) {
        swagger = new SwaggerParser().read(swaggerUrl);
        this.host = host;
    }

    /**
     * @param operationId the operationId of an operation in the Swagger definition
     * @return a RestRequestStepBuilder with the correct path and method
     * @throws IllegalArgumentException if the operationId is not found in the Swagger definition
     */

    public RestRequestStepBuilder<RestRequestStepBuilder> requestStep(String operationId) {

        for( String path : swagger.getPaths().keySet()){
            Path p = swagger.getPaths().get( path );

            for(HttpMethod method: p.getOperationMap().keySet()) {

                Operation op = p.getOperationMap().get(method);
                if (operationId.equalsIgnoreCase(op.getOperationId())) {
                    return new RestRequestStepBuilder<>(
                        host + swagger.getBasePath() + path,
                        TestSteps.HttpMethod.valueOf(method.name().toUpperCase()));
                }
            }
        }

        throw new IllegalArgumentException("operationId [" + operationId + "] not found in Swagger definition" );
    }
}
