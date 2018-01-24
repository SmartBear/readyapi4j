package io.swagger.assert4j.dsl.execution

import io.swagger.assert4j.execution.Execution
import io.swagger.assert4j.execution.RecipeExecutor

import static RecipeExecution.executeRecipe

class RecipeExecutionTest extends GroovyTestCase {

    void testThrowsExceptionWhenLocalRecipeExecutorNotInClasspath() throws Exception {
        shouldFail(IllegalStateException.class, {
            executeRecipe {
                groovyScriptStep 'println "Hello! From Groovy Script step."'
            }
        })
    }

    void testRecipeExecutionWithRecipeExecutor() {
        RecipeExecutor recipeExecutor = new RecipeExecutorAdaptor()
        Execution execution = executeRecipe recipeExecutor, {
            groovyScriptStep 'println "Hello! From Groovy Script step."'
        }
        assert execution
    }
}
