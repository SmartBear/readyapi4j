package com.smartbear.readyapi.client;

import com.smartbear.readyapi.client.execution.Execution;
import com.smartbear.readyapi.client.execution.RecipeExecutor;
import com.smartbear.readyapi.client.execution.Scheme;
import com.smartbear.readyapi.client.model.ProjectResultReport;
import org.junit.Test;

import java.util.Arrays;

import static com.smartbear.readyapi.client.TestRecipeBuilder.newTestRecipe;
import static com.smartbear.readyapi.client.teststeps.TestSteps.getRequest;
import static org.junit.Assert.assertEquals;

public class SimpleRecipeTest {

    @Test
    public void dumpsRecipe() throws Exception {
        TestRecipe recipe = newTestRecipe()
            .addStep(
                getRequest("https://api.swaggerhub.com/apis")
                    .addQueryParameter("query", "testserver")
                    .assertJsonContent("$.totalCount", "1" )
            )
            .buildTestRecipe();

        RecipeExecutor executor = new RecipeExecutor(
            Scheme.HTTP, "ready-api-test-server.swaggerhub31339dac46cf41e3.svc.tutum.io", 8080 );
        executor.setCredentials("defaultUser", "defaultPassword");
        Execution execution = executor.executeRecipe(recipe);

        assertEquals(Arrays.toString( execution.getErrorMessages().toArray()),
            ProjectResultReport.StatusEnum.FINISHED, execution.getCurrentStatus());
    }
}
