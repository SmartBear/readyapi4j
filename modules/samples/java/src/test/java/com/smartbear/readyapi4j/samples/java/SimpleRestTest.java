package com.smartbear.readyapi4j.samples.java;

import com.smartbear.readyapi4j.TestRecipe;
import org.junit.Test;

import static com.smartbear.readyapi4j.TestRecipeBuilder.newTestRecipe;
import static com.smartbear.readyapi4j.teststeps.TestSteps.GET;

public class SimpleRestTest extends ApiTestBase {

    @Test
    public void simpleCountTest() throws Exception {
        TestRecipe recipe = newTestRecipe()
            .addStep(
                GET("https://api.swaggerhub.com/apis")
                    .addQueryParameter("query", "testserver")
                    .assertJsonContent("$.totalCount", "4")
            )
            .buildTestRecipe();

        executeAndAssert(recipe);
    }

    @Test
    public void simpleTest() throws Exception {
        TestRecipe recipe = newTestRecipe()
            .addStep(
                GET("https://api.swaggerhub.com/apis")
                    .assertValidStatusCodes(200)
            )
            .buildTestRecipe();

        executeAndAssert(recipe);
    }
}
