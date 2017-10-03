package io.swagger.assert4j.samples.java;

import io.swagger.assert4j.TestRecipe;
import io.swagger.assert4j.TestRecipeBuilder;
import io.swagger.assert4j.result.RecipeExecutionResult;
import org.junit.Test;

import static io.swagger.assert4j.assertions.Assertions.json;
import static io.swagger.assert4j.assertions.Assertions.statusCodes;
import static io.swagger.assert4j.facade.execution.RecipeExecutionFacade.executeRecipe;
import static io.swagger.assert4j.support.AssertionUtils.assertExecutionResult;
import static io.swagger.assert4j.teststeps.TestSteps.GET;
import static io.swagger.assert4j.teststeps.TestSteps.propertyTransfer;
import static io.swagger.assert4j.teststeps.propertytransfer.PropertyTransferBuilder.fromPreviousResponse;
import static io.swagger.assert4j.teststeps.restrequest.ParameterBuilder.query;

public class SimpleRestTest {

    @Test
    public void simpleCountTest() throws Exception {
        RecipeExecutionResult result = executeRecipe("Simple Count Test",
            GET("https://api.swaggerhub.com/specs")
                .withParameters(
                    query("specType", "API"),
                    query("query", "testserver")
                )
                .withAssertions(
                    json("$.totalCount", "4")
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

    @Test
    public void propertyTransferTest() throws Exception {
        TestRecipe recipe = TestRecipeBuilder.buildRecipe(
            GET( "http://petstore.swagger.io/v2/pet/findByStatus?status=test" ).named("getPets"),
            propertyTransfer(
                fromPreviousResponse( "$.[0].id" ).toNextRequestProperty("id")
            ),
            GET( "http://petstore.swagger.io/v2/pet/{id}" ).named( "getPet" ).
                withAssertions(
                    statusCodes( 200 )
                )
        );

        assertExecutionResult( executeRecipe( recipe ));
    }
}
