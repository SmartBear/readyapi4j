package com.smartbear.readyapi.client.execution;

import com.smartbear.readyapi.client.ExecutionListener;
import com.smartbear.readyapi.client.TestRecipe;

import java.util.List;

/**
 * Defines a class that can execute recipes
 */

public interface RecipeExecutor {

    Execution submitRecipe(TestRecipe recipe) throws ApiException;

    Execution executeRecipe(TestRecipe recipe) throws ApiException;

    List<Execution> getExecutions() throws ApiException;

    void addExecutionListener(ExecutionListener listener);

    void removeExecutionListener(ExecutionListener listener);
}
