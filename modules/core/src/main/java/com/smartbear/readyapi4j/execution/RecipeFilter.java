package com.smartbear.readyapi4j.execution;

import com.smartbear.readyapi4j.TestRecipe;

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
    void filterRecipe(TestRecipe testRecipe);
}
