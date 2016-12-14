package com.smartbear.readyapi4j;

/**
 * Allows pre-processing of recipes before they are sent to the TestServer
 */

public interface RecipeFilter {
    void filterRecipe( TestRecipe testRecipe );
}
