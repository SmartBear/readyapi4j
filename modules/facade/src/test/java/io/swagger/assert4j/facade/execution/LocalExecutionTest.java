package io.swagger.assert4j.facade.execution;

import io.swagger.assert4j.execution.UnsupportedTestStepException;
import org.junit.Test;

import java.util.Arrays;

import static io.swagger.assert4j.facade.execution.RecipeExecutionFacade.executeRecipe;
import static io.swagger.assert4j.testserver.teststeps.ServerTestSteps.gridDataSource;
import static io.swagger.assert4j.teststeps.TestSteps.GET;

public class LocalExecutionTest {
    @Test(expected = UnsupportedTestStepException.class)
    public void localExecutionThrowsExceptionWhenRecipeWithProTestStep() throws Exception {
        executeRecipe(gridDataSource()
                .addProperty("property1", Arrays.asList("value1", "value2"))
                .addTestStep(GET("someurl"))
        );
    }
}
