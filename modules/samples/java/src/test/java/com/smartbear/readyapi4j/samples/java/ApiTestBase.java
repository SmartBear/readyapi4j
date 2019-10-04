package com.smartbear.readyapi4j.samples.java;

import com.smartbear.readyapi4j.TestRecipe;
import com.smartbear.readyapi4j.execution.Execution;
import com.smartbear.readyapi4j.support.ExecutionLogger;
import com.smartbear.readyapi4j.testengine.execution.ProjectExecutionRequest;
import com.smartbear.readyapi4j.testengine.execution.ProjectExecutor;
import com.smartbear.readyapi4j.testengine.execution.TestEngineClient;
import com.smartbear.readyapi4j.testengine.execution.TestEngineRecipeExecutor;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.MalformedURLException;

public class ApiTestBase {

    final static Logger LOG = LoggerFactory.getLogger(ApiTestBase.class);
    private static TestEngineClient testEngineClient;
    private static ProjectExecutor projectExecutor;
    private static TestEngineRecipeExecutor recipeExecutor;

    @BeforeClass
    public static void initExecutor() throws MalformedURLException {
        String hostName = System.getProperty("testengine.endpoint", "http://localhost:8080");
        String user = System.getProperty("testengine.user", "admin");
        String password = System.getProperty("testengine.password", "testengine");

        testEngineClient = TestEngineClient.fromUrl(hostName);
        testEngineClient.setCredentials(user, password);

        projectExecutor = testEngineClient.createProjectExecutor();
        projectExecutor.addExecutionListener(new ExecutionLogger("logs"));

        recipeExecutor = testEngineClient.createRecipeExecutor();
        recipeExecutor.addExecutionListener(new ExecutionLogger("logs"));
    }

    public static Execution executeProject(File file) throws Exception {
        ProjectExecutionRequest request = ProjectExecutionRequest.Builder.forProjectFile(file).build();
        return projectExecutor.executeProject(request);
    }

    public static Execution executeTestRecipe(TestRecipe testRecipe) throws Exception {
        return recipeExecutor.executeRecipe(testRecipe);
    }
}
