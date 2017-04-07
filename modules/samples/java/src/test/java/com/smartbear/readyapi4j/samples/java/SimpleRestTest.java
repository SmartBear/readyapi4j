package com.smartbear.readyapi4j.samples.java;

import com.smartbear.readyapi4j.result.RecipeExecutionResult;
import org.junit.Test;

import static com.smartbear.readyapi4j.facade.execution.RecipeExecutionFacade.executeRecipe;
import static com.smartbear.readyapi4j.support.AssertionUtils.assertExecutionResult;
import static com.smartbear.readyapi4j.teststeps.TestSteps.GET;

public class SimpleRestTest extends ApiTestBase {

    @Test
    public void simpleCountTest() throws Exception {
        RecipeExecutionResult result = executeRecipe(
            GET("https://api.swaggerhub.com/apis")
                .addQueryParameter("query", "testserver")
                .assertJsonContent("$.totalCount", "4")
        );

        assertExecutionResult(result);
    }

    @Test
    public void simpleTest() throws Exception {
        RecipeExecutionResult result = executeRecipe(
            GET("https://api.swaggerhub.com/apis")
                .assertValidStatusCodes(200)
        );

        assertExecutionResult(result);
    }
}
