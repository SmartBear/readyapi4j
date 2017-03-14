package com.smartbear.readyapi4j.samples.java;

import com.smartbear.readyapi4j.TestRecipe;
import com.smartbear.readyapi4j.execution.Execution;
import com.smartbear.readyapi4j.execution.RecipeExecutor;
import com.smartbear.readyapi4j.support.AssertionUtils;
import com.smartbear.readyapi4j.support.FileLoggingExecutionListener;
import com.smartbear.readyapi4j.testserver.execution.ProjectExecutionRequest;
import com.smartbear.readyapi4j.testserver.execution.ProjectExecutor;
import com.smartbear.readyapi4j.testserver.execution.TestServerClient;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.MalformedURLException;

public class ApiTestBase {

    final static Logger LOG = LoggerFactory.getLogger(ApiTestBase.class);
    private static TestServerClient testServerClient;
    private static RecipeExecutor recipeExecutor;
    private static ProjectExecutor projectExecutor;

    @BeforeClass
    public static void initExecutor() throws MalformedURLException {
        String hostName = System.getProperty("testserver.host", "http://testserver.readyapi.io:8080");
        String user = System.getProperty("testserver.user", "demoUser");
        String password = System.getProperty("testserver.password", "demoPassword");

        testServerClient = TestServerClient.fromUrl(hostName);
        testServerClient.setCredentials(user, password);

        recipeExecutor = testServerClient.createRecipeExecutor();
        recipeExecutor.addExecutionListener( new FileLoggingExecutionListener( "logs"));
        projectExecutor = testServerClient.createProjectExecutor();
        projectExecutor.addExecutionListener( new FileLoggingExecutionListener( "logs"));
    }

    public static void executeAndAssert(TestRecipe recipe) {
        LOG.debug("Executing recipe: " + recipe.toString());
        AssertionUtils.assertExecution(recipeExecutor.executeRecipe(recipe));
    }

    public static Execution executeRecipe(TestRecipe recipe) throws Exception {
        return recipeExecutor.executeRecipe(recipe);
    }

    public static Execution executeProject(File file) throws Exception {
        ProjectExecutionRequest request = ProjectExecutionRequest.Builder.forProjectFile(file).build();
        return projectExecutor.executeProject(request);
    }
}
