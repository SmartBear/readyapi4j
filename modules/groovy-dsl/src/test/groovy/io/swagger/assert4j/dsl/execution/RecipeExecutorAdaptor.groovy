package io.swagger.assert4j.dsl.execution

import io.swagger.assert4j.TestRecipe
import io.swagger.assert4j.client.model.TestJobReport
import io.swagger.assert4j.execution.*
import io.swagger.assert4j.testengine.execution.TestEngineExecution

class RecipeExecutorAdaptor implements RecipeExecutor {
    @Override
    Execution submitRecipe(TestRecipe recipe) {
        return new TestEngineExecution(null, null, new TestJobReport())
    }

    @Override
    Execution executeRecipe(TestRecipe recipe) {
        return new TestEngineExecution(null, null, new TestJobReport())
    }

    @Override
    void addExecutionListener(ExecutionListener listener) {

    }

    @Override
    void removeExecutionListener(ExecutionListener listener) {

    }

    @Override
    void addRecipeFilter(RecipeFilter recipeFilter) {

    }

    @Override
    void removeRecipeFilter(RecipeFilter recipeFilter) {

    }

    @Override
    ExecutionMode getExecutionMode() {
        return ExecutionMode.REMOTE
    }
}
