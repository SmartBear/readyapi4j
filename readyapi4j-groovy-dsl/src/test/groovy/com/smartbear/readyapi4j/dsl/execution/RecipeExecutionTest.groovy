package com.smartbear.readyapi4j.dsl.execution

import com.smartbear.readyapi4j.execution.Execution
import com.smartbear.readyapi4j.execution.RecipeExecutor

import static com.smartbear.readyapi4j.dsl.execution.RecipeExecution.executeRecipe

class RecipeExecutionTest extends GroovyTestCase {

    void testThrowsExceptionWhenLocalRecipeExecutorNotInClasspath() throws Exception {
        shouldFail(IllegalStateException.class, {
            executeRecipe {
                groovyScriptStep 'println Hello! From Groovy Script step.'
            }
        })
    }

    void testRecipeExecutionWithRecipeExecutor() {
        RecipeExecutor recipeExecutor = new RecipeExecutorAdaptor()
        Execution execution = executeRecipe recipeExecutor, {
            groovyScriptStep 'println Hello! From Groovy Script step.'
        }
        assert execution
    }
}
