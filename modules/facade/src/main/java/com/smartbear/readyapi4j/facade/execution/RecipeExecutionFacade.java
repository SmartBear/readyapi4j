package com.smartbear.readyapi4j.facade.execution;

import com.smartbear.readyapi4j.TestRecipe;
import com.smartbear.readyapi4j.TestRecipeBuilder;
import com.smartbear.readyapi4j.execution.Execution;
import com.smartbear.readyapi4j.execution.RecipeExecutor;
import com.smartbear.readyapi4j.result.RecipeExecutionResult;
import com.smartbear.readyapi4j.teststeps.TestStepBuilder;

import static com.smartbear.readyapi4j.support.AssertionUtils.assertExecution;

/**
 * Utility for running recipes either locally or on a remote TestServer based on system/env property values<br/>
 * <p>
 * If a <code>testserver.endpoint</code> system or environment property is available and contains a valid URL
 * remote execution will be used by the executeRecipe method. The values of
 * <code>testserver.user</code> and <code>testserver.password</code> properties/environment variables will be used to
 * authenticate on the TestServer.
 * If no valid server URL has been specified recipe execution will be local instead.</p>
 */

public class RecipeExecutionFacade {

    private static RecipeExecutor executor;

    /**
     * Builds and executes a TestRecipe from the specified TestStepBuilders using either a local or remote
     * executor as configured. The recipe is always executed synchronously.
     *
     * @param testStepBuilders the builds for the TestSteps to execute
     * @return the result for executed recipe
     */
    public static RecipeExecutionResult executeRecipe(TestStepBuilder... testStepBuilders) {
        TestRecipe recipe = TestRecipeBuilder.newTestRecipe(testStepBuilders).buildTestRecipe();
        return executeRecipe(recipe);
    }

    /**
     * Builds and executes a TestRecipe from the specified TestStepBuilders using either a local or remote
     * executor as configured. The recipe is always executed synchronously.
     *
     * @param name the name of the recipe
     * @param testStepBuilders the builds for the TestSteps to execute
     * @return the result for executed recipe
     */
    public static RecipeExecutionResult executeRecipe(String name, TestStepBuilder... testStepBuilders) {
        TestRecipe recipe = TestRecipeBuilder.newTestRecipe(testStepBuilders).named(name).buildTestRecipe();
        return executeRecipe(recipe);
    }

    /**
     * Executes the specified TestRecipe and returns the result
     *
     * @param recipe the recipe to execute
     * @return the excution result
     */
    public static RecipeExecutionResult executeRecipe(TestRecipe recipe) {
        synchronized (Object.class) {
            if (executor == null) {
                executor = RecipeExecutorBuilder.buildDefault();
            }
        }

        Execution execution = executor.executeRecipe(recipe);
        assertExecution(execution);
        return execution.getExecutionResult();
    }
}
