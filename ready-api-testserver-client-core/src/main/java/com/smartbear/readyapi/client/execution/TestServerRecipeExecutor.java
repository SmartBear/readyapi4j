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
public class TestServerRecipeExecutor extends AbstractTestServerExecutor implements RecipeExecutor {
    private static Logger logger = LoggerFactory.getLogger(TestServerRecipeExecutor.class);

    private final List<RecipeFilter> recipeFilters = new CopyOnWriteArrayList<>();

    TestServerRecipeExecutor(TestServerClient testServerClient) {
        super(testServerClient);
    }

    public void addRecipeFilter(RecipeFilter recipeFilter) {
        recipeFilters.add(recipeFilter);
    }

    public void removeRecipeFilter(RecipeFilter recipeFilter) {
        recipeFilters.remove(recipeFilter);
    }

    @Override
    public TestServerExecution submitRecipe(TestRecipe recipe) throws ApiException {
        for (RecipeFilter recipeFilter : recipeFilters) {
            recipeFilter.filterRecipe(recipe);
        }

        TestServerExecution execution = doExecuteTestCase(recipe.getTestCase(), true);
        notifyExecutionStarted(execution);
        return execution;
    }

    @Override
    public TestServerExecution executeRecipe(TestRecipe recipe) throws ApiException {
        TestServerExecution execution = doExecuteTestCase(recipe.getTestCase(), false);
        if (execution != null) {
            notifyExecutionFinished(execution.getCurrentReport());
        }
        return execution;
    }

    private TestServerExecution doExecuteTestCase(TestCase testCase, boolean async) throws ApiException {
        try {
            TestServerExecution execution = testServerClient.postTestRecipe(testCase, async);
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

    @Override
    public List<Execution> getExecutions() throws ApiException {
        return testServerClient.getExecutions();
    }
}
