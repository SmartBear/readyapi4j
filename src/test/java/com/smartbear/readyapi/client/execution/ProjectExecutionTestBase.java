package com.smartbear.readyapi.client.execution;

import org.junit.Before;

import static org.mockito.Mockito.mock;

class ProjectExecutionTestBase {
    static final String HOST = "thehost";
    static final int PORT = 6234;
    static final String BASE_PATH = "/custom_path";

    TestServerApi apiWrapper;
    TestServerRequestExecutor recipeExecutor;

    @Before
    public void setUpApiMockAndRecipeExecutor() throws Exception {
        apiWrapper = mock(TestServerApi.class);
        recipeExecutor = new TestServerRequestExecutor(ServerDefaults.DEFAULT_SCHEME, HOST, PORT, BASE_PATH, apiWrapper);
        recipeExecutor.setCredentials("theUser", "thePassword");
    }

}
