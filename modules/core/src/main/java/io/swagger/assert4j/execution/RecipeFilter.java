package io.swagger.assert4j.execution;

import io.swagger.assert4j.TestRecipe;

/**
 * Allows pre-processing of recipes before they are sent to the test execution engine. Filters can
 * be added to a RecipeExecutor via its addRecipeFilter method
 */

public interface RecipeFilter {

    /**
     * Filters the specified recipe
     *
     * @param testRecipe
     */
    void filterRecipe( TestRecipe testRecipe );
}
