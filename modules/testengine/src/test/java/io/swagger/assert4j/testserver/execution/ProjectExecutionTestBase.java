package io.swagger.assert4j.testserver.execution;

import org.junit.Before;

import static org.mockito.Mockito.mock;

public class ProjectExecutionTestBase {
    static final String HOST = "thehost";
    static final int PORT = 6234;
    static final String BASE_PATH = "/custom_path";

    TestServerClient testServerClient;
    protected TestEngineApi apiWrapper;
    protected TestServerRecipeExecutor recipeExecutor;
    ProjectExecutor projectExecutor;

    @Before
    public void setUpApiMockAndRecipeExecutor() throws Exception {
        apiWrapper = mock(TestEngineApi.class);
        testServerClient = new TestServerClient(ServerDefaults.DEFAULT_SCHEME, HOST, PORT, BASE_PATH, apiWrapper);
        testServerClient.setCredentials("theUser", "thePassword");
        recipeExecutor = testServerClient.createRecipeExecutor();
        projectExecutor = testServerClient.createProjectExecutor();
    }

}
