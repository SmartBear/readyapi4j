package com.smartbear.readyapi.client.execution;

import com.smartbear.readyapi.client.RepositoryProjectExecutionRequest;
import com.smartbear.readyapi.client.model.HarLogRoot;
import com.smartbear.readyapi.client.model.ProjectResultReport;
import com.smartbear.readyapi.client.model.ProjectResultReports;
import com.smartbear.readyapi.client.model.TestCase;
import io.swagger.client.auth.HttpBasicAuth;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TestServerClient {

    private String baseUrl;

    static {
        if (System.getProperty("org.slf4j.simpleLogger.defaultLogLevel") == null) { //Don't set if user has defined the log level
            System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "trace");
        }
    }

    private TestServerApi apiStub;

    private HttpBasicAuth authentication;


    public TestServerClient(Scheme scheme, String host, int port) {
        this(scheme, host, port, ServerDefaults.VERSION_PREFIX, new CodegenBasedTestServerApi());
    }

    public TestServerClient(String host, int port) {
        this(ServerDefaults.DEFAULT_SCHEME, host, port);
    }

    public TestServerClient(String host) {
        this(host, ServerDefaults.DEFAULT_PORT);
    }

    public static TestServerClient fromUrl(String testserverUrl) throws MalformedURLException {
        URL url = new URL(testserverUrl);
        return new TestServerClient(Scheme.valueOf(url.getProtocol().toUpperCase()), url.getHost(),
                url.getPort() == -1 ? 80 : url.getPort());
    }

    public TestServerRecipeExecutor createRecipeExecutor() {
        return new TestServerRecipeExecutor(this);
    }

    public ProjectExecutor createProjectExecutor() {
        return new ProjectExecutor(this);
    }

    public SwaggerApiValidator createApiValidator() {
        return new SwaggerApiValidator(this);
    }

    // Used for testing
    TestServerClient(Scheme scheme, String host, int port, String basePath, TestServerApi apiStub) {
        this.apiStub = apiStub;
        baseUrl = buildBaseUrl(scheme, host, port, basePath);
        apiStub.setBasePath(baseUrl);
    }

    protected String buildBaseUrl(Scheme scheme, String host, int port, String basePath) {
        return String.format("%s://%s:%d%s", scheme.getValue(), host, port, basePath);
    }

    public void setCredentials(String username, String password) {
        authentication = new HttpBasicAuth();
        authentication.setUsername(username);
        authentication.setPassword(password);
    }

    public TestServerClient withCredentials(String username, String password) {
        setCredentials(username, password);
        return this;
    }

    public TestServerClient withApiStub(TestServerApi apiStub) {
        this.apiStub = apiStub;
        return this;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    TestServerExecution postTestRecipe(TestCase testCase, boolean async) {
        ProjectResultReport projectResultReport = apiStub.postTestRecipe(testCase, async, authentication);
        return new TestServerExecution(apiStub, authentication, projectResultReport);
    }

    TestServerExecution postProject(ProjectExecutionRequest projectExecutionRequest, boolean async) {
        ProjectResultReport projectResultReport = apiStub.postProject(projectExecutionRequest, async, authentication);
        return new TestServerExecution(apiStub, authentication, projectResultReport);
    }

    TestServerExecution postRepositoryProject(RepositoryProjectExecutionRequest executionRequest, boolean async) {
        ProjectResultReport projectResultReport = apiStub.postRepositoryProject(executionRequest, async, authentication);
        return new TestServerExecution(apiStub, authentication, projectResultReport);
    }

    Execution postSwagger(File swaggerFile, SwaggerApiValidator.SwaggerFormat swaggerFormat, String callBackUrl, String endpoint, boolean async) {
        ProjectResultReport projectResultReport = apiStub.postSwagger(swaggerFile, swaggerFormat, endpoint, callBackUrl, true, authentication);
        return new TestServerExecution(apiStub, authentication, projectResultReport);
    }

    Execution postSwagger(URL swaggerApiURL, String endpoint, String callBackUrl, boolean async) {
        ProjectResultReport projectResultReport = apiStub.postSwagger(swaggerApiURL, endpoint, callBackUrl, true, authentication);
        return new TestServerExecution(apiStub, authentication, projectResultReport);
    }

    ProjectResultReport getExecutionStatus(String executionId) {
        return apiStub.getExecutionStatus(executionId, authentication);
    }

    void cancelExecution(String executionID) {
        apiStub.cancelExecution(executionID, authentication);
    }

    public TestServerExecution cancelExecution(final TestServerExecution execution) {
        ProjectResultReport projectResultReport = apiStub.cancelExecution(execution.getId(), authentication);
        execution.addResultReport(projectResultReport);
        return execution;
    }

    public HarLogRoot getTransactionLog(final Execution execution, String transactionId) {
        return apiStub.getTransactionLog(execution.getId(), transactionId, authentication);
    }

    public List<Execution> getExecutions() {
        List<Execution> executions = new ArrayList<>();
        ProjectResultReports projectResultReport = apiStub.getExecutions(authentication);
        for (ProjectResultReport resultReport : projectResultReport.getProjectResultReports()) {
            executions.add(new TestServerExecution(apiStub, authentication, resultReport));
        }
        return executions;
    }

}
