package io.swagger.assert4j.testserver.execution;

import io.swagger.assert4j.TestRecipe;
import io.swagger.assert4j.execution.ExecutionMode;
import io.swagger.assert4j.execution.RecipeExecutor;
import io.swagger.assert4j.execution.RecipeFilter;
import io.swagger.assert4j.extractor.ExtractorData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Executor for executing Recipes on a TestServer instances - both synchronously and asynchronously
 */
public class TestServerRecipeExecutor extends AbstractTestServerExecutor implements RecipeExecutor {
    private static Logger logger = LoggerFactory.getLogger(TestServerRecipeExecutor.class);

    private final List<RecipeFilter> recipeFilters = new CopyOnWriteArrayList<>();

    TestServerRecipeExecutor(TestServerClient testServerClient) {
        super(testServerClient);
    }

    @Override
    public void addRecipeFilter(RecipeFilter recipeFilter) {
        recipeFilters.add(recipeFilter);
    }

    @Override
    public void removeRecipeFilter(RecipeFilter recipeFilter) {
        recipeFilters.remove(recipeFilter);
    }

    @Override
    public ExecutionMode getExecutionMode() {
        return ExecutionMode.REMOTE;
    }

    @Override
    public TestServerExecution submitRecipe(TestRecipe recipe) {
        for (RecipeFilter recipeFilter : recipeFilters) {
            recipeFilter.filterRecipe(recipe);
        }

        TestServerExecution execution = doExecuteTestCase(recipe, recipe.getExtractorData(), true);
        notifyExecutionStarted(execution);
        return execution;
    }

    @Override
    public TestServerExecution executeRecipe(TestRecipe recipe) {
        for (RecipeFilter recipeFilter : recipeFilters) {
            recipeFilter.filterRecipe(recipe);
        }

        TestServerExecution execution = doExecuteTestCase(recipe, recipe.getExtractorData(), false);
        notifyExecutionFinished(execution);
        return execution;
    }

    private TestServerExecution doExecuteTestCase(TestRecipe testRecipe, ExtractorData optionalExtractorData, boolean async) {
        try {
            Optional<ExtractorData> extractorDataOptional = Optional.ofNullable(optionalExtractorData);
            extractorDataOptional.ifPresent(extractorData -> extractorDataList.add(extractorData));
            TestServerExecution execution = testServerClient.postTestRecipe(testRecipe, async);
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
