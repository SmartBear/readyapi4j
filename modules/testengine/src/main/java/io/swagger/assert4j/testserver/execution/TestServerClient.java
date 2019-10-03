package io.swagger.assert4j.testserver.execution;

import io.swagger.assert4j.HttpBasicAuth;
import io.swagger.assert4j.TestRecipe;
import io.swagger.assert4j.client.model.HarLogRoot;
import io.swagger.assert4j.client.model.TestJobReport;
import io.swagger.assert4j.execution.Execution;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Main class for invoking a ReadyAPI TestServer instance
 */

public class TestServerClient {

    private String baseUrl;

    static {
        if (System.getProperty("org.slf4j.simpleLogger.defaultLogLevel") == null) { //Don't set if user has defined the log level
            System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "trace");
        }
    }

    private TestEngineApi apiStub;

    private HttpBasicAuth authentication;


    /**
     * Creates a TestServerClient for a TestServer instance at the specified endpoint
     *
     * @param scheme the endpoint protocol to use
     * @param host   the hostname to use
     * @param port   the port to use
     */

    public TestServerClient(Scheme scheme, String host, int port) {
        this(scheme, host, port, ServerDefaults.VERSION_PREFIX, new CodegenBasedTestEngineApi());
    }

    /**
     * Creates a TestServerClient for a TestServer instance at the specified endpoint with the default scheme
     *
     * @param host the hostname to use
     * @param port the port to use
     */

    public TestServerClient(String host, int port) {
        this(ServerDefaults.DEFAULT_SCHEME, host, port);
    }

    /**
     * Creates a TestServerClient for a TestServer instance at the specified endpoint with the default scheme and port
     *
     * @param host the hostname to use
     */

    public TestServerClient(String host) {
        this(host, ServerDefaults.DEFAULT_PORT);
    }


    /**
     * Builds a TestServerClient for the specified URL
     *
     * @param testserverUrl the complete URL of the TestServer instance
     * @return a constructed TestServerClient instance
     * @throws MalformedURLException if the URL was invalid
     */

    public static TestServerClient fromUrl(String testserverUrl) throws MalformedURLException {
        URL url = new URL(testserverUrl);
        return new TestServerClient(Scheme.valueOf(url.getProtocol().toUpperCase()), url.getHost(),
                url.getPort() == -1 ? 80 : url.getPort());
    }

    /**
     * @return a class for executing Test Recipes on the TestServer
     */

    public TestServerRecipeExecutor createRecipeExecutor() {
        return new TestServerRecipeExecutor(this);
    }

    /**
     * @return a class for executing standalone projects on the TestServer
     */

    public ProjectExecutor createProjectExecutor() {
        return new ProjectExecutor(this);
    }

    public ProjectExecutor createProjectExecutor(ProjectExecutor.PendingResonsePolicy pendingResponsePolicy) {
        return new ProjectExecutor(this, pendingResponsePolicy);
    }

    /**
     * @return a class for validating APIs against a Swagger definition
     */

    // Used for testing
    TestServerClient(Scheme scheme, String host, int port, String basePath, TestEngineApi apiStub) {
        this.apiStub = apiStub;
        baseUrl = buildBaseUrl(scheme, host, port, basePath);
        apiStub.setBasePath(baseUrl);
    }

    protected String buildBaseUrl(Scheme scheme, String host, int port, String basePath) {
        return String.format("%s://%s:%d%s", scheme.getValue(), host, port, basePath);
    }

    /**
     * Sets the user credentials to use to authenticate requests sent to the configured TestServer instance
     */

    public void setCredentials(String username, String password) {
        authentication = new HttpBasicAuth();
        authentication.setUsername(username);
        authentication.setPassword(password);
    }

    /**
     * Fluent method for setting the user credentials to use to authenticate requests sent to the
     * configured TestServer instance
     */

    public TestServerClient withCredentials(String username, String password) {
        setCredentials(username, password);
        return this;
    }

    /**
     * Sets the TestServerApi implementation to use for sending requests to the TestServer
     */

    public TestServerClient withApiStub(TestEngineApi apiStub) {
        this.apiStub = apiStub;
        return this;
    }

    protected String getBaseUrl() {
        return baseUrl;
    }

    TestServerExecution postTestRecipe(TestRecipe testRecipe, boolean async) {
        TestJobReport projectResultReport = apiStub.postTestRecipe(testRecipe, async, authentication);
        return new TestServerExecution(apiStub, authentication, projectResultReport);
    }

    TestServerExecution postProject(ProjectExecutionRequest projectExecutionRequest, boolean async) {
        TestJobReport projectResultReport = apiStub.postProject(projectExecutionRequest, async, authentication);
        return new TestServerExecution(apiStub, authentication, projectResultReport);
    }

    TestJobReport getExecutionStatus(String executionId) {
        return apiStub.getExecutionStatus(executionId, authentication);
    }

    void cancelExecution(String executionID) {
        apiStub.cancelExecution(executionID, authentication);
    }

    /**
     * Cancels an execution previously created by one of the executors
     *
     * @param execution the execution to cancle
     * @return the canceled execution
     */

    public TestServerExecution cancelExecution(final TestServerExecution execution) {
        TestJobReport projectResultReport = apiStub.cancelExecution(execution.getId(), authentication);
        execution.addResultReport(projectResultReport);
        return execution;
    }

    /**
     * Returns the HAR Log entry/entries for a specified transaction within a TestServer execution
     *
     * @param execution     the execution to query
     * @param transactionId the id of a specific transaction within the specified execution
     * @return the HAR Log for the specified transation
     */

    public HarLogRoot getTransactionLog(final Execution execution, String transactionId) {
        return apiStub.getTransactionLog(execution.getId(), transactionId, authentication);
    }


}
