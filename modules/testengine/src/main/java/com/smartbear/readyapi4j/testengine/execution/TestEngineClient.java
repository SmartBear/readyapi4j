package com.smartbear.readyapi4j.testengine.execution;

import com.smartbear.readyapi4j.HttpBasicAuth;
import com.smartbear.readyapi4j.TestRecipe;
import com.smartbear.readyapi4j.client.model.HarLogRoot;
import com.smartbear.readyapi4j.client.model.TestJobReport;
import com.smartbear.readyapi4j.execution.Execution;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Main class for invoking a ReadyAPI TestEngine instance
 */

public class TestEngineClient {

    private String baseUrl;

    static {
        if (System.getProperty("org.slf4j.simpleLogger.defaultLogLevel") == null) { //Don't set if user has defined the log level
            System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "trace");
        }
    }

    private TestEngineApi apiStub;

    private HttpBasicAuth authentication;


    /**
     * Creates a TestEngineClient for a TestEngine instance at the specified endpoint
     *
     * @param scheme the endpoint protocol to use
     * @param host   the hostname to use
     * @param port   the port to use
     */

    public TestEngineClient(Scheme scheme, String host, int port) {
        this(scheme, host, port, ServerDefaults.VERSION_PREFIX, new CodegenBasedTestEngineApi());
    }

    /**
     * Creates a TestEngineClient for a TestEngine instance at the specified endpoint with the default scheme
     *
     * @param host the hostname to use
     * @param port the port to use
     */

    public TestEngineClient(String host, int port) {
        this(ServerDefaults.DEFAULT_SCHEME, host, port);
    }

    /**
     * Creates a TestEngineClient for a TestEngine instance at the specified endpoint with the default scheme and port
     *
     * @param host the hostname to use
     */

    public TestEngineClient(String host) {
        this(host, ServerDefaults.DEFAULT_PORT);
    }


    /**
     * Builds a TestEngineClient for the specified URL
     *
     * @param testengineUrl the complete URL of the TestEngine instance
     * @return a constructed TestEngineClient instance
     * @throws MalformedURLException if the URL was invalid
     */

    public static TestEngineClient fromUrl(String testengineUrl) throws MalformedURLException {
        URL url = new URL(testengineUrl);
        return new TestEngineClient(Scheme.valueOf(url.getProtocol().toUpperCase()), url.getHost(),
                url.getPort() == -1 ? 80 : url.getPort());
    }

    /**
     * @return a class for executing Test Recipes on the TestEngine
     */

    public TestEngineRecipeExecutor createRecipeExecutor() {
        return new TestEngineRecipeExecutor(this);
    }

    /**
     * @return a class for executing standalone projects on the TestEngine
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
    TestEngineClient(Scheme scheme, String host, int port, String basePath, TestEngineApi apiStub) {
        this.apiStub = apiStub;
        baseUrl = buildBaseUrl(scheme, host, port, basePath);
        apiStub.setBasePath(baseUrl);
    }

    protected String buildBaseUrl(Scheme scheme, String host, int port, String basePath) {
        return String.format("%s://%s:%d%s", scheme.getValue(), host, port, basePath);
    }

    /**
     * Sets the user credentials to use to authenticate requests sent to the configured TestEngine instance
     */

    public void setCredentials(String username, String password) {
        authentication = new HttpBasicAuth();
        authentication.setUsername(username);
        authentication.setPassword(password);
    }

    /**
     * Fluent method for setting the user credentials to use to authenticate requests sent to the
     * configured TestEngine instance
     */

    public TestEngineClient withCredentials(String username, String password) {
        setCredentials(username, password);
        return this;
    }

    /**
     * Sets the TestEngineApi implementation to use for sending requests to the TestEngine
     */

    public TestEngineClient withApiStub(TestEngineApi apiStub) {
        this.apiStub = apiStub;
        return this;
    }

    protected String getBaseUrl() {
        return baseUrl;
    }

    TestEngineExecution postTestRecipe(TestRecipe testRecipe, boolean async) {
        TestJobReport projectResultReport = apiStub.postTestRecipe(testRecipe, async, authentication);
        return new TestEngineExecution(apiStub, authentication, projectResultReport);
    }

    TestEngineExecution postProject(ProjectExecutionRequest projectExecutionRequest, boolean async) {
        TestJobReport projectResultReport = apiStub.postProject(projectExecutionRequest, async, authentication);
        return new TestEngineExecution(apiStub, authentication, projectResultReport);
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

    public TestEngineExecution cancelExecution(final TestEngineExecution execution) {
        TestJobReport projectResultReport = apiStub.cancelExecution(execution.getId(), authentication);
        execution.addResultReport(projectResultReport);
        return execution;
    }

    /**
     * Returns the HAR Log entry/entries for a specified transaction within a TestEngine execution
     *
     * @param execution     the execution to query
     * @param transactionId the id of a specific transaction within the specified execution
     * @return the HAR Log for the specified transation
     */

    public HarLogRoot getTransactionLog(final Execution execution, String transactionId) {
        return apiStub.getTransactionLog(execution.getId(), transactionId, authentication);
    }


}
