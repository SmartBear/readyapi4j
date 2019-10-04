package com.smartbear.readyapi4j.facade.execution;

import com.smartbear.readyapi4j.execution.UnsupportedTestStepException;
import org.junit.Test;

import java.util.Arrays;

import static com.smartbear.readyapi4j.facade.execution.RecipeExecutionFacade.executeRecipe;
import static com.smartbear.readyapi4j.testengine.teststeps.ServerTestSteps.gridDataSource;
import static com.smartbear.readyapi4j.teststeps.TestSteps.GET;

public class LocalExecutionTest {
    @Test(expected = UnsupportedTestStepException.class)
    public void localExecutionThrowsExceptionWhenRecipeWithProTestStep() throws Exception {
        executeRecipe(gridDataSource()
                .addProperty("property1", Arrays.asList("value1", "value2"))
                .addTestStep(GET("someurl"))
        );
    }
}
