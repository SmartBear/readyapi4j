package com.smartbear.readyapi.client.execution;

import com.smartbear.readyapi.client.RecipeFilter;
import com.smartbear.readyapi.client.TestRecipe;
import com.smartbear.readyapi.client.model.TestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Responsible for executing test recipes and projects on a Ready! API Server, synchronously or asynchronously.
 */
public class RecipeExecutor extends AbstractExecutor {
    private static Logger logger = LoggerFactory.getLogger(RecipeExecutor.class);

    private final List<RecipeFilter> recipeFilters = new CopyOnWriteArrayList<>();

    RecipeExecutor(TestServerClient testServerClient) {
        super(testServerClient);
    }

    public void addRecipeFilter(RecipeFilter recipeFilter) {
        recipeFilters.add(recipeFilter);
    }

    public void removeRecipeFilter(RecipeFilter recipeFilter) {
        recipeFilters.remove(recipeFilter);
    }

    public Execution submitRecipe(TestRecipe recipe) throws ApiException {
        for (RecipeFilter recipeFilter : recipeFilters) {
            recipeFilter.filterRecipe(recipe);
        }

        Execution execution = doExecuteTestCase(recipe.getTestCase(), true);
        notifyRequestSubmitted(execution);
        return execution;
    }

    public Execution executeRecipe(TestRecipe recipe) throws ApiException {
        Execution execution = doExecuteTestCase(recipe.getTestCase(), false);
        if (execution != null) {
            notifyExecutionFinished(execution.getCurrentReport());
        }
        return execution;
    }

    private Execution doExecuteTestCase(TestCase testCase, boolean async) throws ApiException {
        try {
            Execution execution = testServerClient.postTestRecipe(testCase, async);
            cancelExecutionAndThrowExceptionIfPendingDueToMissingClientCertificate(execution.getCurrentReport(), testCase);
            return execution;
        } catch (ApiException e) {
            notifyErrorOccurred(e);
            logger.debug("An error occurred when sending test recipe to server. Details: " + e.toString());
            throw e;
        } catch (Exception e) {
            notifyErrorOccurred(e);
            logger.debug("An error occurred when sending test recipe to server", e);
            throw new ApiException(e);
        }
    }
}
