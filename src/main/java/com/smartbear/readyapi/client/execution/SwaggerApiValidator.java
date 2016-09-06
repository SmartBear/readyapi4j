package com.smartbear.readyapi.client.execution;

import java.io.File;
import java.net.URL;

public class SwaggerApiValidator extends AbstractExecutor {

    SwaggerApiValidator(TestServerClient testServerClient) {
        super(testServerClient);
    }

    enum SwaggerFormat {
        JSON("application/json"),
        YAML("application/yaml");

        private final String mimeType;

        SwaggerFormat(String mimeType) {
            this.mimeType = mimeType;
        }

        public String getMimeType() {
            return mimeType;
        }
    }

    /**
     * Asynchronous execution of Swagger Definition:
     * Submit Swagger specification file to TestServer to create and execute tests for each api defined in specifications.
     *
     * @param swaggerFile   Swagger file
     * @param swaggerFormat format
     * @param endpoint      endpoint against which tests should be executed.
     *                      Tests will be executed against the host specified in Swagger definition if endpoint is not provided.
     * @param callBackUrl Callback Url
     * @return execution with executionId to be used to query the status
     */
    public Execution validateApiAsynchronously(File swaggerFile, SwaggerFormat swaggerFormat, String endpoint, String callBackUrl) {
        return testServerClient.postSwagger(swaggerFile, swaggerFormat, callBackUrl, endpoint, true);
    }

    /**
     * Synchronous execution of Swagger Definition:
     * Submit Swagger specification file to TestServer to create and execute tests for each api defined in specifications.
     *
     * @param swaggerFile   Swagger file
     * @param swaggerFormat format
     * @param endpoint      endpoint against which tests should be executed.
     *                      Tests will be executed against the host specified in Swagger definition if endpoint is not provided.
     * @param callBackUrl Callback Url
     * @return execution with execution report
     */
    public Execution validateApiSynchronously(File swaggerFile, SwaggerFormat swaggerFormat, String endpoint, String callBackUrl) {
        return testServerClient.postSwagger(swaggerFile, swaggerFormat, callBackUrl, endpoint, false);
    }

    /**
     * Asynchronous : Submit URL of Swagger specification to TestServer to create and execute tests for each api defined in specifications.
     *
     * @param swaggerApiURL URL of Swagger API
     * @param endpoint      endpoint against which tests should be executed.
     *                      Tests will be executed against the host specified in Swagger definition if endpoint is not provided.
     * @return execution with executionId to be used to query the status
     */
    public Execution validateApiAsynchronously(URL swaggerApiURL, String endpoint, String callBackUrl) {
        return testServerClient.postSwagger(swaggerApiURL, endpoint, callBackUrl,  true);
    }

    /**
     * Synchronous execution: Submit URL of Swagger specification to TestServer to create and execute tests for each api defined in specifications.
     *
     * @param swaggerApiURL URL of Swagger API
     * @param endpoint      endpoint against which tests should be executed.
     *                      Tests will be executed against the host specified in Swagger definition if endpoint is not provided.
     * @return execution with execution report
     */
    public Execution validateApiSynchronously(URL swaggerApiURL, String endpoint, String callBackUrl) {
        return testServerClient.postSwagger(swaggerApiURL, endpoint, callBackUrl, false);
    }
}
