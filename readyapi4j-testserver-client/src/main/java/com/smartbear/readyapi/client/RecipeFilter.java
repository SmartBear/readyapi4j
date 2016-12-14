package com.smartbear.readyapi.client;

/**
 * Allows pre-processing of recipes before they are sent to the TestServer
 */

public interface RecipeFilter {
    void filterRecipe( TestRecipe testRecipe );
}
