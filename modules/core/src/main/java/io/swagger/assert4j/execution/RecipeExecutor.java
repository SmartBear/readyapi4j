package io.swagger.assert4j.execution;

import io.swagger.assert4j.ExecutionListener;
import io.swagger.assert4j.TestRecipe;

import java.util.List;

/**
 * Defines a class that can execute recipes
 */

public interface RecipeExecutor {

    /**
     * Submit a Test recipe for asynchronous execution.
     *
     * @param recipe Test recipe to be executed.
     * @return an instance of <code>Execution</code> containing the current state of execution
     */
    Execution submitRecipe(TestRecipe recipe);

    /**
     * Submit a Test recipe for synchronous execution.
     *
     * @param recipe Test recipe to be executed.
     * @return an instance of <code>Execution</code> containing the execution result
     */
    Execution executeRecipe(TestRecipe recipe);

    /**
     * @return List of all the execution stored on server
     */
    List<Execution> getExecutions();

    /**
     * Adds an execution listener, used when a recipe is submitted for asynchronous execution.
     *
     * @param listener listener to be added
     */
    void addExecutionListener(ExecutionListener listener);

    /**
     * Removes the provided listener
     *
     * @param listener listener to be removed
     */
    void removeExecutionListener(ExecutionListener listener);

    /**
     * Adds a recipe filter for preprocessing generated recipes
     *
     * @param recipeFilter
     */

    void addRecipeFilter(RecipeFilter recipeFilter);

    /**
     * Removes an existing recipe filter
     * @param recipeFilter
     */

    void removeRecipeFilter(RecipeFilter recipeFilter);

    /**
     * @return executionMode <code>ExecutionMode.LOCAL</code> if running locally, <code>ExecutionMode.LOCAL</code> otherwise (when running on TestServer)
     */
    ExecutionMode getExecutionMode();
}
