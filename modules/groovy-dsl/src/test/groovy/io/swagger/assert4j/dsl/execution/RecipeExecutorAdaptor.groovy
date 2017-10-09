package io.swagger.assert4j.dsl.execution

import io.swagger.assert4j.TestRecipe
import io.swagger.assert4j.client.model.ProjectResultReport
import io.swagger.assert4j.execution.Execution
import io.swagger.assert4j.execution.ExecutionListener
import io.swagger.assert4j.execution.ExecutionMode
import io.swagger.assert4j.execution.RecipeExecutor
import io.swagger.assert4j.execution.RecipeFilter
import io.swagger.assert4j.testserver.execution.TestServerExecution

class RecipeExecutorAdaptor implements RecipeExecutor {
    @Override
    Execution submitRecipe(TestRecipe recipe) {
        return new TestServerExecution(null, null, new ProjectResultReport())
    }

    @Override
    Execution executeRecipe(TestRecipe recipe) {
        return new TestServerExecution(null, null, new ProjectResultReport())
    }

    @Override
    List<Execution> getExecutions() {
        return null
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
