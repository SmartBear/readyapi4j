package com.smartbear.readyapi4j.local.execution;

import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.impl.wsdl.testcase.WsdlProjectRunner;
import com.eviware.soapui.model.support.ProjectRunListenerAdapter;
import com.eviware.soapui.model.testsuite.ProjectRunContext;
import com.eviware.soapui.model.testsuite.ProjectRunner;
import com.eviware.soapui.support.types.StringToObjectMap;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.smartbear.ready.recipe.JsonRecipeParser;
import com.smartbear.ready.recipe.teststeps.TestCaseStruct;
import com.smartbear.readyapi.client.model.ProjectResultReport;
import com.smartbear.readyapi4j.ExecutionListener;
import com.smartbear.readyapi4j.TestRecipe;
import com.smartbear.readyapi4j.execution.DataExtractors;
import com.smartbear.readyapi4j.execution.Execution;
import com.smartbear.readyapi4j.execution.RecipeExecutionException;
import com.smartbear.readyapi4j.execution.RecipeExecutor;
import com.smartbear.readyapi4j.execution.RecipeFilter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Class that can execute a Test recipe locally, using the SoapUI core classes.
 */
public class SoapUIRecipeExecutor implements RecipeExecutor {
    private static final String LOCAL_CLIENT_EXECUTION_ID = "SoapUILocalClient#ExecutionId";

    private final Map<String, SoapUIRecipeExecution> executionsMap = new HashMap<>();
    private final JsonRecipeParser recipeParser = new JsonRecipeParser();
    private final List<ExecutionListener> executionListeners = new CopyOnWriteArrayList<>();
    private final List<RecipeFilter> recipeFilters = new CopyOnWriteArrayList<>();
    private ObjectMapper objectMapper;

    @Override
    public Execution submitRecipe(TestRecipe recipe) {
        applyRecipeFilters(recipe);
        return postRecipe(recipe, true);
    }

    private void applyRecipeFilters(TestRecipe recipe) {
        recipeFilters.forEach(filter -> filter.filterRecipe(recipe));
    }

    @Override
    public Execution executeRecipe(TestRecipe recipe) {
        applyRecipeFilters(recipe);
        return postRecipe(recipe, false);
    }

    @Override
    public List<Execution> getExecutions() {
        return Lists.newArrayList(executionsMap.values());
    }

    @Override
    public void addExecutionListener(ExecutionListener listener) {
        executionListeners.add(listener);
    }

    @Override
    public void removeExecutionListener(ExecutionListener listener) {
        executionListeners.remove(listener);
    }

    @Override
    public void addRecipeFilter(RecipeFilter recipeFilter) {
        recipeFilters.add(recipeFilter);
    }

    @Override
    public void removeRecipeFilter(RecipeFilter recipeFilter) {
        recipeFilters.remove(recipeFilter);
    }

    private Execution postRecipe(TestRecipe testRecipe, boolean async) {
        String executionId = UUID.randomUUID().toString();
        try {
            String jsonText = getObjectMapper().writeValueAsString(testRecipe.getTestCase());
            TestCaseStruct testCaseStruct = getObjectMapper().readValue(jsonText, TestCaseStruct.class);
            WsdlProject project = recipeParser.parse(testCaseStruct);
            StringToObjectMap properties = new StringToObjectMap();

            if (async) {
                prepareAsyncExecution(testRecipe, executionId, project, properties);
            }

            WsdlProjectRunner projectRunner = new WsdlProjectRunner(project, properties);

            SoapUIRecipeExecution execution = new SoapUIRecipeExecution(executionId, projectRunner);
            executionsMap.put(executionId, execution);
            projectRunner.start(async);
            if (!async) {
                notifyExecutionFinished(testRecipe, execution.getCurrentReport());
            }
            return execution;
        } catch (Exception e) {
            notifyErrorOccurred(e);
            throw new RecipeExecutionException("Failed to execute Test recipe", e);
        }
    }

    private ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        }
        return objectMapper;
    }

    private void prepareAsyncExecution(TestRecipe testRecipe, String executionId, WsdlProject project, StringToObjectMap properties) {
        project.addProjectRunListener(new ProjectRunListenerAdapter() {
            @Override
            public void beforeRun(ProjectRunner projectRunner, ProjectRunContext runContext) {
                String executionId = (String) runContext.getProperty(LOCAL_CLIENT_EXECUTION_ID);
                if (executionId != null) {
                    notifyExecutionStarted(executionsMap.get(executionId).getCurrentReport());
                }
            }

            @Override
            public void afterRun(ProjectRunner projectRunner, ProjectRunContext runContext) {
                String executionId = (String) runContext.getProperty(LOCAL_CLIENT_EXECUTION_ID);
                if (executionId != null) {
                    notifyExecutionFinished(testRecipe, executionsMap.get(executionId).getCurrentReport());
                }
            }
        });
        properties.put(LOCAL_CLIENT_EXECUTION_ID, executionId);
    }


    private void notifyExecutionStarted(ProjectResultReport projectResultReport) {
        if (projectResultReport != null) {
            for (ExecutionListener executionListener : executionListeners) {
                executionListener.executionStarted(projectResultReport);
            }
        }
    }

    private void notifyErrorOccurred(Exception e) {
        for (ExecutionListener executionListener : executionListeners) {
            executionListener.errorOccurred(e);
        }
    }

    private void notifyExecutionFinished(TestRecipe testRecipe, ProjectResultReport projectResultReport) {
        if( testRecipe.getExtractorData() != null ) {
            DataExtractors.runDataExtractors(projectResultReport, Arrays.asList(testRecipe.getExtractorData()));
        }

        for (ExecutionListener executionListener : executionListeners) {
            executionListener.executionFinished(projectResultReport);
        }
    }
}
