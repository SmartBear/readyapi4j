package com.smartbear.readyapi4j.samples.java;

import com.smartbear.readyapi4j.result.RecipeExecutionResult;
import org.junit.Test;

import static com.smartbear.readyapi4j.assertions.Assertions.json;
import static com.smartbear.readyapi4j.facade.execution.RecipeExecutionFacade.executeRecipe;
import static com.smartbear.readyapi4j.support.AssertionUtils.assertExecutionResult;
import static com.smartbear.readyapi4j.teststeps.TestSteps.GET;
import static com.smartbear.readyapi4j.teststeps.restrequest.ParameterBuilder.query;

public class SimpleRestTest {

    @Test
    public void simpleCountTest() throws Exception {
        RecipeExecutionResult result = executeRecipe("Simple Count Test",
                GET("https://api.swaggerhub.com/specs")
                        .withParameters(
                                query("specType", "API"),
                                query("query", "testengine")
                        )
                        .withAssertions(
                                json("$.totalCount", "2")
                        )
        );

        assertExecutionResult(result);
    }

    @Test
    public void simpleRedirectTest() throws Exception {
        RecipeExecutionResult result = executeRecipe("Simple Redirect Test",
                GET("https://api.swaggerhub.com/apis")
                        .assertValidStatusCodes(303)
        );

        assertExecutionResult(result);
    }
}
