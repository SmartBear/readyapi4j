package com.smartbear.readyapi4j.testserver.execution;

import org.junit.Before;

import static org.mockito.Mockito.mock;

public class ProjectExecutionTestBase {
    static final String HOST = "thehost";
    static final int PORT = 6234;
    static final String BASE_PATH = "/custom_path";

    TestServerClient testServerClient;
    protected TestServerApi apiWrapper;
    protected TestServerRecipeExecutor recipeExecutor;
    ProjectExecutor projectExecutor;
    SwaggerApiValidator swaggerApiValidator;

    @Before
    public void setUpApiMockAndRecipeExecutor() throws Exception {
        apiWrapper = mock(TestServerApi.class);
        testServerClient = new TestServerClient(ServerDefaults.DEFAULT_SCHEME, HOST, PORT, BASE_PATH, apiWrapper);
        testServerClient.setCredentials("theUser", "thePassword");
        recipeExecutor = testServerClient.createRecipeExecutor();
        projectExecutor = testServerClient.createProjectExecutor();
        swaggerApiValidator = testServerClient.createApiValidator();
    }

}
