package com.smartbear.readyapi4j.dsl.execution

import com.smartbear.readyapi4j.execution.Execution
import com.smartbear.readyapi4j.execution.RecipeExecutor

import static com.smartbear.readyapi4j.dsl.execution.RecipeExecution.executeRecipe
import static com.smartbear.readyapi4j.dsl.execution.RecipeExecution.executeRecipeLocally

class RecipeExecutionTest extends GroovyTestCase {

    void testThrowsExceptionWhenLocalRecipeExecutorNotInClasspath() throws Exception {
        shouldFail(IllegalStateException.class, {
            executeRecipeLocally {
                get 'http://google.com'
            }
        })
    }

    void testRecipeExecutionWithRecipeExecutor() {
        RecipeExecutor recipeExecutor = new RecipeExecutorAdaptor()
        Execution execution = executeRecipe recipeExecutor, {
            get 'http://google.com'
        }
        assert execution
    }
}
