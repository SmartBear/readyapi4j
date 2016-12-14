package com.smartbear.readyapi4j.execution;

import com.smartbear.readyapi4j.ExecutionListener;
import com.smartbear.readyapi4j.TestRecipe;
import com.smartbear.readyapi4j.execution.Execution;

import java.util.List;

/**
 * Defines a class that can execute recipes
 */

public interface RecipeExecutor {

    Execution submitRecipe(TestRecipe recipe);

    Execution executeRecipe(TestRecipe recipe);

    List<Execution> getExecutions();

    void addExecutionListener(ExecutionListener listener);

    void removeExecutionListener(ExecutionListener listener);
}
