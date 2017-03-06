package com.smartbear.readyapi4j.facade.execution;

import com.smartbear.readyapi4j.execution.RecipeExecutor;
import com.smartbear.readyapi4j.execution.RecipeFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for building a configured RecipeExecutor
 */

public class RecipeExecutorBuilder {

    private List<RecipeFilter> filters = new ArrayList<>();

    public RecipeExecutorBuilder withFilter(RecipeFilter filter) {
        filters.add(filter);
        return this;
    }

    public RecipeExecutor build() {
        RecipeExecutor executor = RecipeExecutionFacade.createRecipeExecutor();
        for (RecipeFilter filter : filters) {
            executor.addRecipeFilter(filter);
        }
        return executor;
    }
}
