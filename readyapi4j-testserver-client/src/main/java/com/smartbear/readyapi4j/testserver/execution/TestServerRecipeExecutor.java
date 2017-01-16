package com.smartbear.readyapi4j.testserver.execution;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.smartbear.readyapi.client.model.Assertion;
import com.smartbear.readyapi.client.model.BooleanDataGenerator;
import com.smartbear.readyapi.client.model.ComputerAddressDataGenerator;
import com.smartbear.readyapi.client.model.CustomStringDataGenerator;
import com.smartbear.readyapi.client.model.DataGenerator;
import com.smartbear.readyapi.client.model.DateAndTimeDataGenerator;
import com.smartbear.readyapi.client.model.IntegerDataGenerator;
import com.smartbear.readyapi.client.model.NameDataGenerator;
import com.smartbear.readyapi.client.model.PhoneNumberDataGenerator;
import com.smartbear.readyapi.client.model.RealNumberDataGenerator;
import com.smartbear.readyapi.client.model.StateNameDataGenerator;
import com.smartbear.readyapi.client.model.StringDataGenerator;
import com.smartbear.readyapi.client.model.TestCase;
import com.smartbear.readyapi.client.model.TestStep;
import com.smartbear.readyapi.client.model.UKPostCodeDataGenerator;
import com.smartbear.readyapi.client.model.USZIPCodeDataGenerator;
import com.smartbear.readyapi.client.model.ValuesFromSetDataGenerator;
import com.smartbear.readyapi4j.RecipeFilter;
import com.smartbear.readyapi4j.TestRecipe;
import com.smartbear.readyapi4j.execution.Execution;
import com.smartbear.readyapi4j.execution.RecipeExecutor;
import com.smartbear.readyapi4j.extractor.ExtractorData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Responsible for executing test recipes and projects on a Ready! API Server, synchronously or asynchronously.
 */
public class TestServerRecipeExecutor extends AbstractTestServerExecutor implements RecipeExecutor {
    private static Logger logger = LoggerFactory.getLogger(TestServerRecipeExecutor.class);

    private final List<RecipeFilter> recipeFilters = new CopyOnWriteArrayList<>();

    private ObjectMapper objectMapper;

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
    public Execution submitRecipe(String jsonText) {
        return submitJsonText(jsonText, true);
    }

    @Override
    public Execution executeRecipe(String jsonText) {
        return submitJsonText(jsonText, false);
    }

    private Execution submitJsonText(String jsonText, boolean async) throws ApiException {
        try {
            ObjectMapper objectMapper = getObjectMapper();
            TestCase testCase = objectMapper.readValue(jsonText, TestCase.class);
            return doExecuteTestCase(testCase, null, async);
        } catch (IOException e) {
            logger.error("Failed to execute recipe", e);
            throw new ApiException(400, e.getMessage());
        }
    }

    private ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
            objectMapper.addMixIn(TestStep.class, TestStepMixin.class);
            objectMapper.addMixIn(DataGenerator.class, DataGeneratorTypeMixin.class);
            objectMapper.addMixIn(Assertion.class, AssertionMixin.class);
        }
        return objectMapper;
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
        TestServerExecution execution = doExecuteTestCase(recipe.getTestCase(), recipe.getExtractorData(), false);
        if (execution != null) {
            notifyExecutionFinished(execution.getCurrentReport());
        }
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

    @JsonTypeIdResolver(TestStepTypeResolver.class)
    @JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", visible = true)
    private static class TestStepMixin {
    }

    @JsonTypeIdResolver(AssertionTypeResolver.class)
    @JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", visible = true)
    private static class AssertionMixin {
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", visible = true)
    @JsonSubTypes({
            @JsonSubTypes.Type(value = BooleanDataGenerator.class, name = "Boolean"),
            @JsonSubTypes.Type(value = ComputerAddressDataGenerator.class, name = "Computer Address"),
            @JsonSubTypes.Type(value = NameDataGenerator.class, name = "Name"),
            @JsonSubTypes.Type(value = CustomStringDataGenerator.class, name = "Custom String"),
            @JsonSubTypes.Type(value = StringDataGenerator.class, name = "String"),
            @JsonSubTypes.Type(value = PhoneNumberDataGenerator.class, name = "Phone Number"),
            @JsonSubTypes.Type(value = StateNameDataGenerator.class, name = "State"),
            @JsonSubTypes.Type(value = UKPostCodeDataGenerator.class, name = "United Kingdom Postcode"),
            @JsonSubTypes.Type(value = USZIPCodeDataGenerator.class, name = "United States ZIP Code"),
            @JsonSubTypes.Type(value = IntegerDataGenerator.class, name = "Integer"),
            @JsonSubTypes.Type(value = RealNumberDataGenerator.class, name = "Real"),
            @JsonSubTypes.Type(value = ValuesFromSetDataGenerator.class, name = "Value from Set"),
            @JsonSubTypes.Type(value = DateAndTimeDataGenerator.class, name = "Date and Time"),
            @JsonSubTypes.Type(value = DataGenerator.class, name = "City"),
            @JsonSubTypes.Type(value = DataGenerator.class, name = "Country"),
            @JsonSubTypes.Type(value = DataGenerator.class, name = "Street Address"),
            @JsonSubTypes.Type(value = DataGenerator.class, name = "E-Mail"),
            @JsonSubTypes.Type(value = DataGenerator.class, name = "Guid"),
            @JsonSubTypes.Type(value = DataGenerator.class, name = "Social Security Number")
    })
    private static class DataGeneratorTypeMixin {
    }
}
