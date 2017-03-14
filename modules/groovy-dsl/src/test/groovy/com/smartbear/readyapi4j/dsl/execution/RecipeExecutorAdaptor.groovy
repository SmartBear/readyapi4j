package com.smartbear.readyapi4j.dsl.execution

import com.smartbear.readyapi.client.model.ProjectResultReport
import com.smartbear.readyapi4j.ExecutionListener
import com.smartbear.readyapi4j.TestRecipe
import com.smartbear.readyapi4j.execution.Execution
import com.smartbear.readyapi4j.execution.ExecutionMode
import com.smartbear.readyapi4j.execution.RecipeExecutor
import com.smartbear.readyapi4j.execution.RecipeFilter
import com.smartbear.readyapi4j.testserver.execution.TestServerExecution


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
