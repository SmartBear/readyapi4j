package com.smartbear.readyapi;

import com.smartbear.readyapi.client.TestRecipe;
import com.smartbear.readyapi.client.execution.Execution;
import com.smartbear.readyapi.client.execution.ProjectExecutionRequest;
import com.smartbear.readyapi.client.execution.ProjectExecutor;
import com.smartbear.readyapi.client.execution.RecipeExecutor;
import com.smartbear.readyapi.client.execution.TestServerClient;
import com.smartbear.readyapi.client.support.AssertionUtils;
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
        projectExecutor = testServerClient.createProjectExecutor();
    }

    public static void executeAndAssert(TestRecipe recipe) {
        LOG.debug("Executing recipe: " + recipe.toString());
        AssertionUtils.assertExecution(recipeExecutor.executeRecipe(recipe));
    }

    public static Execution executeRecipe(TestRecipe recipe) throws Exception {
        return recipeExecutor.executeRecipe(recipe);
    }

    public static Execution executeProject(File file) throws Exception {
        ProjectExecutionRequest request = ProjectExecutionRequest.Builder.newInstance().withProjectFile(file).build();
        return projectExecutor.executeProject(request);
    }
}
