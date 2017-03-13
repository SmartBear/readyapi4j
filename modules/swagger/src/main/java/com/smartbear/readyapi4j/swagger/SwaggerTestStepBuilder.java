package com.smartbear.readyapi4j.swagger;

import com.smartbear.readyapi4j.teststeps.TestSteps;
import com.smartbear.readyapi4j.teststeps.restrequest.RestRequestStepBuilder;
import com.smartbear.readyapi4j.teststeps.restrequest.RestRequestStepWithBodyBuilder;
import io.swagger.models.HttpMethod;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Swagger;
import io.swagger.models.parameters.BodyParameter;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.parameters.RefParameter;
import io.swagger.parser.SwaggerParser;
import io.swagger.parser.util.SwaggerDeserializationResult;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Map;

/**
 * Utility class for building RestRequestStepBuilders for operations in a Swagger 2.0 definition
 */

public class SwaggerTestStepBuilder {

    private static final Collection<TestSteps.HttpMethod> HTTP_METHODS_WITH_BODY =
            EnumSet.of(TestSteps.HttpMethod.POST, TestSteps.HttpMethod.PUT, TestSteps.HttpMethod.PATCH);
    private final Swagger swagger;
    private String targetEndpoint;
    private String targetBasePath;

    /**
     * Creates a SwaggerTestStepBuilder for the specified Swagger definition and target targetEndpoint
     *
     * @param swaggerUrl endpoint to the Swagger definition to use
     * @param targetEndpoint where the target API under test running
     * @throws IllegalArgumentException if the specified Swagger definition can not be parsed
     */
    public SwaggerTestStepBuilder(String swaggerUrl, String targetEndpoint) throws IllegalArgumentException {
        this.swagger = parseSwagger(swaggerUrl);
        setTargetEndpoint(targetEndpoint);
    }

    /**
     * Creates a SwaggerTestStepBuilder for the specified Swagger definition, uses the host and first scheme in the
     * definition for the target endpoint.
     *
     * @param swaggerUrl endpoint to the Swagger definition to use
     * @throws IllegalArgumentException if the specified Swagger definition can not be parsed
     */
    public SwaggerTestStepBuilder(String swaggerUrl) throws IllegalArgumentException {
        this(swaggerUrl, null);
    }

    /**
     * Creates a SwaggerTestStepBuilder for the specified Swagger definition and target targetEndpoint
     *
     * @param swaggerInputStream stream from which the Swagger definition can be read
     * @param targetEndpoint where the target API under test running
     * @throws IllegalArgumentException if the specified Swagger definition can not be parsed
     * @throws IOException if the Swagger definition can not be read
     */
    public SwaggerTestStepBuilder(InputStream swaggerInputStream, String targetEndpoint) throws IllegalArgumentException, IOException {
        this.swagger = parseSwagger(swaggerInputStream);
        setTargetEndpoint(targetEndpoint);
    }

    /**
     * Creates a SwaggerTestStepBuilder for the specified Swagger definition, uses the host and first scheme in the
     * definition for the target endpoint.
     *
     * @param swaggerInputStream stream from which the swagger definition can be read
     * @throws IllegalArgumentException if the specified Swagger definition can not be parsed
     * @throws IOException if the Swagger definition can not be read
     */
    public SwaggerTestStepBuilder(InputStream swaggerInputStream) throws IllegalArgumentException, IOException {
        this(swaggerInputStream, null);
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

    private Swagger parseSwagger(InputStream swaggerInputStream) throws IllegalArgumentException, IOException {
        String swaggerAsString = IOUtils.toString(swaggerInputStream, StandardCharsets.UTF_8);
        SwaggerDeserializationResult result = new SwaggerParser().readWithInfo(swaggerAsString);
        Swagger swagger = result.getSwagger();
        if (swagger == null) {
            throw new IllegalArgumentException("Failed to parse Swagger definition; " +
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
            Path swaggerPath = swagger.getPaths().get( path );

            for(HttpMethod method: swaggerPath.getOperationMap().keySet()) {
                Operation swaggerOperation = swaggerPath.getOperationMap().get(method);
                if (operationId.equalsIgnoreCase(swaggerOperation.getOperationId())) {
                    return new RestRequestStepBuilder<>(targetBasePath + path, toHttpMethod(method));
                }
            }
        }

        throw new IllegalArgumentException("operationId [" + operationId + "] not found in Swagger definition" );
    }

    /**
     * Creates a RestRequestStepBuilder for the specified operation with body
     *
     * @param operationId the operationId of an operation in the Swagger definition
     * @return a RestRequestStepWithBodyBuilder with the correct path and method
     * @throws IllegalArgumentException if the operationId is not found in the Swagger definition
     */
    public RestRequestStepWithBodyBuilder operationWithBody(String operationId) {
        for (Map.Entry<String, Path> path : swagger.getPaths().entrySet()) {

            for(Map.Entry<HttpMethod, Operation> method: path.getValue().getOperationMap().entrySet()) {
                Operation swaggerOperation = method.getValue();
                if (!operationId.equalsIgnoreCase(swaggerOperation.getOperationId())) {
                    continue;
                }
                final TestSteps.HttpMethod verb = toHttpMethod(method.getKey());
                ensureHttpMethodWithBody(verb);
                ensureBodyParameter(swaggerOperation);

                return new RestRequestStepWithBodyBuilder(targetBasePath + path.getKey(), verb);
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
        return new RestRequestStepBuilder<>(targetBasePath + path, method);
    }

    /**
     * Creates a RestRequestStepWithBodyBuilder for an arbitrary path and method - the
     * targetEndpoint and basePath will be prefixed to the specified path.
     *
     * @param path   the path to append to the targetEndpoint and basePath
     * @param method the HTTP method to use
     */
    public RestRequestStepWithBodyBuilder requestWithBody(String path, TestSteps.HttpMethod method){
        ensureHttpMethodWithBody(method);
        return new RestRequestStepWithBodyBuilder(targetBasePath + path, method);
    }

    private void ensureBodyParameter(Operation operation) {
        for (Parameter param : operation.getParameters()) {
            if (param instanceof BodyParameter) {
                return;
            } else if (param instanceof RefParameter){
                final Parameter unboxedParam = unboxRefParameter((RefParameter) param);
                if (unboxedParam instanceof BodyParameter){
                    return;
                }
            }
        }
        throw new IllegalArgumentException(
                "Body parameter is not defined for the [" + operation.getOperationId() + "] operation");
    }

    private Parameter unboxRefParameter(RefParameter param) {
        return swagger.getParameter(param.getSimpleRef());
    }

    private void ensureHttpMethodWithBody(TestSteps.HttpMethod method) {
        if (!HTTP_METHODS_WITH_BODY.contains(method)) {
            throw new IllegalArgumentException(
                    "Body parameter is not allowed for [" + method + "] methods");
        }
    }

    private TestSteps.HttpMethod toHttpMethod(HttpMethod swaggerMethod) {
        return TestSteps.HttpMethod.valueOf(swaggerMethod.name().toUpperCase());
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
        final String endpoint;
        if (targetEndpoint == null) {
            endpoint = swagger.getSchemes().isEmpty() ? "http" : swagger.getSchemes().get(0).toValue().toLowerCase() + "://" + swagger.getHost();
        } else {
            endpoint = targetEndpoint;
        }

        this.targetEndpoint = endpoint;

        final String basePath = swagger.getBasePath();
        if (basePath == null) {
            this.targetBasePath = endpoint;
        } else if (basePath.endsWith("/")) {
            this.targetBasePath = endpoint + basePath.substring(0, basePath.length() - 1);
        } else {
            this.targetBasePath = endpoint + basePath;
        }
    }
}
