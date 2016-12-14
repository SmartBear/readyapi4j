package com.smartbear.readyapi4j.execution;

import com.smartbear.readyapi.client.model.ProjectResultReport;
import io.swagger.client.auth.HttpBasicAuth;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.net.URL;

import static com.smartbear.readyapi4j.execution.ExecutionTestHelper.makeFinishedReport;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.when;

public class SwaggerExecutionTest extends ProjectExecutionTestBase {
    @Test
    public void executesSwaggerFile() throws Exception {
        ProjectResultReport endReport = makeFinishedReport("executionId");
        when(apiWrapper.postSwagger(any(File.class), any(SwaggerApiValidator.SwaggerFormat.class), any(String.class), any(String.class), anyBoolean(),
                any(HttpBasicAuth.class))).thenReturn(endReport);
        Execution execution = swaggerApiValidator.validateApiSynchronously(getSwaggerFile(), SwaggerApiValidator.SwaggerFormat.JSON, "http://google.com", "http://google.com");
        assertThat(execution.getCurrentStatus(), is(ProjectResultReport.StatusEnum.FINISHED));
    }

    @Test(expected = ApiException.class)
    public void throwsExceptionIfSwaggerFileDoesNotExist() throws Exception {
        new TestServerClient("localhost").createApiValidator().validateApiSynchronously(new File("non-existing-file.json"), SwaggerApiValidator.SwaggerFormat.JSON, null, "http://google.com");
    }

    @Test
    public void executesSwaggerFromURL() throws Exception {
        ProjectResultReport endReport = makeFinishedReport("executionId");
        when(apiWrapper.postSwagger(any(URL.class), any(String.class), any(String.class), anyBoolean(), any(HttpBasicAuth.class)))
                .thenReturn(endReport);
        Execution execution = swaggerApiValidator.validateApiSynchronously(new URL("http://abc.com"), "http://google.com" , "http://google.com");
        assertThat(execution.getCurrentStatus(), is(ProjectResultReport.StatusEnum.FINISHED));
    }

    private File getSwaggerFile() {
        return new File(SwaggerExecutionTest.class.getResource("/pet-store-2.0-minimal.json").getFile());
    }

    @Ignore("manual test")
    public void manualTest() {
        File swaggerFile = new File(SwaggerExecutionTest.class.getResource("/pet-store-2.0-minimal.json").getFile());
        TestServerClient testServerClient = new TestServerClient("localhost");
        testServerClient.setCredentials("prakash", "password");
        Execution execution = testServerClient.createApiValidator().validateApiSynchronously(swaggerFile, SwaggerApiValidator.SwaggerFormat.JSON, null, "http://petstore.swagger.io");
        System.out.println(execution.getCurrentReport());
    }
}
