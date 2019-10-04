package io.swagger.assert4j.testengine.execution;

import org.junit.Before;

import static org.mockito.Mockito.mock;

public class ProjectExecutionTestBase {
    static final String HOST = "thehost";
    static final int PORT = 6234;
    static final String BASE_PATH = "/custom_path";

    TestEngineClient testEngineClient;
    protected TestEngineApi apiWrapper;
    protected TestEngineRecipeExecutor recipeExecutor;
    ProjectExecutor projectExecutor;

    @Before
    public void setUpApiMockAndRecipeExecutor() throws Exception {
        apiWrapper = mock(TestEngineApi.class);
        testEngineClient = new TestEngineClient(ServerDefaults.DEFAULT_SCHEME, HOST, PORT, BASE_PATH, apiWrapper);
        testEngineClient.setCredentials("theUser", "thePassword");
        recipeExecutor = testEngineClient.createRecipeExecutor();
        projectExecutor = testEngineClient.createProjectExecutor();
    }

}
