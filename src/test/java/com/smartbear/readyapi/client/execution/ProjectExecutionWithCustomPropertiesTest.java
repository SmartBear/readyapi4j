package com.smartbear.readyapi.client.execution;

import com.smartbear.readyapi.client.RepositoryProjectExecutionRequest;
import com.smartbear.readyapi.client.model.CustomProperties;
import com.smartbear.readyapi.client.model.ProjectResultReport;
import com.sun.jersey.api.client.GenericType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.smartbear.readyapi.client.execution.ExecutionTestHelper.makeFinishedReport;
import static com.smartbear.readyapi.client.execution.ProjectExecutionTestBase.BASE_PATH;
import static com.smartbear.readyapi.client.execution.ProjectExecutionTestBase.HOST;
import static com.smartbear.readyapi.client.execution.ProjectExecutionTestBase.PORT;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProjectExecutionWithCustomPropertiesTest {
    private static final String TEST_SUITE_NAME = "TestSuite-1";

    private ApiClientWrapper apiClientWrapper;
    private RecipeExecutor recipeExecutor;
    private RepositoryProjectExecutionRequest executionRequest;

    @Before
    public void setUp() throws Exception {
        apiClientWrapper = Mockito.mock(ApiClientWrapper.class);
        TestServerApi testServerApi = new CodegenBasedTestServerApi(apiClientWrapper);
        recipeExecutor = new RecipeExecutor(ServerDefaults.DEFAULT_SCHEME, HOST, PORT, BASE_PATH, testServerApi);
        executionRequest = createRepositoryProjectExecutionRequest();
    }

    @Test
    public void invokesProjectExecutionWithCustomProperties() throws Exception {
        ProjectResultReport endReport = makeFinishedReport("executionId");
        when(apiClientWrapper.invokeAPI(any(String.class), eq("POST"), any(List.class), eq(executionRequest.getCustomPropertiesMap().values()),
                any(Map.class), eq("application/json"), eq("application/json"), any(String[].class), any(GenericType.class))).thenReturn(endReport);

        recipeExecutor.executeRepositoryProject(executionRequest);

        ArgumentCaptor<Collection> customPropertiesCaptor = ArgumentCaptor.forClass(Collection.class);
        verify(apiClientWrapper).invokeAPI(eq("/readyapi/executions/project"), eq("POST"), any(List.class), customPropertiesCaptor.capture(),
                any(Map.class), eq("application/json"), eq("application/json"), any(String[].class), any(GenericType.class));

        Iterator customProperties = customPropertiesCaptor.getValue().iterator();
        assertCustomProperties((CustomProperties) customProperties.next());
        assertCustomProperties((CustomProperties) customProperties.next());
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

    private RepositoryProjectExecutionRequest createRepositoryProjectExecutionRequest() {
        HashMap<String, String> testSuiteProperties = new HashMap<>();
        testSuiteProperties.put("property1", "value1");
        testSuiteProperties.put("property2", "value2");

        return RepositoryProjectExecutionRequest.Builder.newInstance()
                .fromRepository("compositeprojects")
                .forProject("Environment-test.xml")
                .forEnvironment("staging")
                .testSuite(TEST_SUITE_NAME)
                .testCase("TestCase-1")
                .addCustomProperty(TEST_SUITE_NAME, "Test-suite-property", "test-suite-property-value")
                .addCustomProperty("TestCase-1", "Test-case-property", "test-case-property-value")
                .addCustomProperties(TEST_SUITE_NAME, testSuiteProperties)
                .build();
    }
}
