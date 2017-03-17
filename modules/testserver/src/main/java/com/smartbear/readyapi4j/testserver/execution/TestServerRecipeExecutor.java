package com.smartbear.readyapi4j.testserver.execution;

import com.smartbear.readyapi.client.model.TestCase;
import com.smartbear.readyapi4j.TestRecipe;
import com.smartbear.readyapi4j.execution.Execution;
import com.smartbear.readyapi4j.execution.ExecutionMode;
import com.smartbear.readyapi4j.execution.RecipeExecutor;
import com.smartbear.readyapi4j.execution.RecipeFilter;
import com.smartbear.readyapi4j.extractor.ExtractorData;
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
    public TestServerExecution submitRecipe(TestRecipe recipe) throws ApiException {
        for (RecipeFilter recipeFilter : recipeFilters) {
            recipeFilter.filterRecipe(recipe);
        }

        TestServerExecution execution = doExecuteTestCase(recipe.getTestCase(), recipe.getExtractorData(), true);
        notifyExecutionStarted(execution);
        return execution;
    }

    @Override
    public TestServerExecution executeRecipe(TestRecipe recipe) throws ApiException {
        for (RecipeFilter recipeFilter : recipeFilters) {
            recipeFilter.filterRecipe(recipe);
        }

        TestServerExecution execution = doExecuteTestCase(recipe.getTestCase(), recipe.getExtractorData(), false);
        notifyExecutionFinished(execution.getCurrentReport());
        return execution;
    }

    private TestServerExecution doExecuteTestCase(TestCase testCase, ExtractorData optionalExtractorData, boolean async) throws ApiException {
        try {
            Optional<ExtractorData> extractorDataOptional = Optional.ofNullable(optionalExtractorData);
            extractorDataOptional.ifPresent(extractorData -> extractorDataList.add(extractorData));
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
