package com.smartbear.readyapi4j.local.execution;

import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.impl.wsdl.testcase.WsdlProjectRunner;
import com.eviware.soapui.model.support.ProjectRunListenerAdapter;
import com.eviware.soapui.model.testsuite.ProjectRunContext;
import com.eviware.soapui.model.testsuite.ProjectRunner;
import com.eviware.soapui.support.types.StringToObjectMap;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartbear.ready.recipe.JsonRecipeParser;
import com.smartbear.ready.recipe.teststeps.TestCaseStruct;
import com.smartbear.readyapi4j.TestRecipe;
import com.smartbear.readyapi4j.client.model.TestJobReport;
import com.smartbear.readyapi4j.client.model.TestStep;
import com.smartbear.readyapi4j.execution.*;
import com.smartbear.readyapi4j.extractor.DataExtractors;
import com.smartbear.readyapi4j.teststeps.TestStepTypes;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

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

    @Override
    public ExecutionMode getExecutionMode() {
        return ExecutionMode.LOCAL;
    }

    private Execution postRecipe(TestRecipe testRecipe, boolean async) {
        List<TestStep> proTestSteps = testRecipe.getTestCase().getTestSteps()
                .stream()
                .filter(testStep -> testStep.getType().equals(TestStepTypes.DATA_SOURCE.getName()))
                .collect(Collectors.toList());
        if (!proTestSteps.isEmpty()) {
            throw new UnsupportedTestStepException("DataSource test step is supported only with Server execution mode.");
        }

        String executionId = UUID.randomUUID().toString();
        try {
            String jsonText = getObjectMapper().writeValueAsString(testRecipe.getTestCase());
            TestCaseStruct testCaseStruct = getObjectMapper().readValue(jsonText, TestCaseStruct.class);
            WsdlProject project = recipeParser.parse(testCaseStruct);
            StringToObjectMap properties = new StringToObjectMap();

            WsdlProjectRunner projectRunner = new WsdlProjectRunner(project, properties);
            SoapUIRecipeExecution execution = new SoapUIRecipeExecution(executionId, projectRunner);

            if (async) {
                prepareAsyncExecution(testRecipe, execution, projectRunner);
            }

            executionsMap.put(executionId, execution);
            projectRunner.start(async);
            if (!async) {
                notifyExecutionFinished(testRecipe, execution);
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
            objectMapper.setDefaultPropertyInclusion(JsonInclude.Value.construct(JsonInclude.Include.NON_EMPTY,
                    JsonInclude.Include.ALWAYS));
            objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        }
        return objectMapper;
    }

    private void prepareAsyncExecution(TestRecipe testRecipe, SoapUIRecipeExecution execution, WsdlProjectRunner projectRunner) {
        WsdlProject project = execution.getProject();
        project.addProjectRunListener(new ProjectRunListenerAdapter() {
            @Override
            public void beforeRun(ProjectRunner projectRunner, ProjectRunContext runContext) {
                String executionId = (String) runContext.getProperty(LOCAL_CLIENT_EXECUTION_ID);
                if (executionId != null) {
                    notifyExecutionStarted(executionsMap.get(executionId));
                }
            }

            @Override
            public void afterRun(ProjectRunner projectRunner, ProjectRunContext runContext) {
                String executionId = (String) runContext.getProperty(LOCAL_CLIENT_EXECUTION_ID);
                if (executionId != null) {
                    notifyExecutionFinished(testRecipe, executionsMap.get(executionId));
                }
            }
        });

        projectRunner.getRunContext().put(LOCAL_CLIENT_EXECUTION_ID, execution.getId());
    }

    private void notifyExecutionStarted(Execution execution) {
        for (ExecutionListener executionListener : executionListeners) {
            executionListener.executionStarted(execution);
        }
    }

    private void notifyErrorOccurred(Exception e) {
        for (ExecutionListener executionListener : executionListeners) {
            executionListener.errorOccurred(e);
        }
    }

    private void notifyExecutionFinished(TestRecipe testRecipe, Execution execution) {
        TestJobReport projectResultReport = execution.getCurrentReport();
        if (testRecipe.getExtractorData() != null) {
            DataExtractors.runDataExtractors(projectResultReport, Arrays.asList(testRecipe.getExtractorData()));
        }

        for (ExecutionListener executionListener : executionListeners) {
            executionListener.executionFinished(execution);
        }
    }
}
