package io.swagger.assert4j.testengine.execution;

import io.swagger.assert4j.client.model.CustomProperties;
import org.junit.Before;
import org.mockito.Mockito;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ProjectExecutionWithCustomPropertiesTest {
    private static final String TEST_SUITE_NAME = "TestSuite-1";

    private ApiClientWrapper apiClientWrapper;
    private ProjectExecutor projectExecutor;

    @Before
    public void setUp() throws Exception {
        apiClientWrapper = Mockito.mock(ApiClientWrapper.class);
        TestEngineApi testEngineApi = new CodegenBasedTestEngineApi(apiClientWrapper);
        TestEngineClient testEngineClient = new TestEngineClient(ServerDefaults.DEFAULT_SCHEME, ProjectExecutionTestBase.HOST, ProjectExecutionTestBase.PORT, ProjectExecutionTestBase.BASE_PATH, testEngineApi);
        projectExecutor = testEngineClient.createProjectExecutor();
    }

    private void assertCustomProperties(CustomProperties customProperties) {
        if (customProperties.getTargetName().equals(TEST_SUITE_NAME)) {
            assertThat(customProperties.getProperties().get("property1"), is("value1"));
            assertThat(customProperties.getProperties().get("property2"), is("value2"));
            assertThat(customProperties.getProperties().get("Test-suite-property"), is("test-suite-property-value"));
        } else {
            assertThat(customProperties.getProperties().get("Test-case-property"), is("test-case-property-value"));
        }
    }
}
