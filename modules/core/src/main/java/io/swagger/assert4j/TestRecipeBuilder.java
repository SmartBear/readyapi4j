package io.swagger.assert4j;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import io.swagger.assert4j.client.model.*;
import io.swagger.assert4j.extractor.Extractor;
import io.swagger.assert4j.extractor.ExtractorData;
import io.swagger.assert4j.properties.PropertyBuilder;
import io.swagger.assert4j.teststeps.TestStepBuilder;
import io.swagger.assert4j.teststeps.propertytransfer.PropertyTransferBuilder;
import io.swagger.assert4j.teststeps.propertytransfer.PropertyTransferSourceBuilder;
import io.swagger.assert4j.teststeps.propertytransfer.PropertyTransferTargetBuilder;
import io.swagger.assert4j.teststeps.propertytransfer.PropertyTransferTestStepBuilder;
import io.swagger.assert4j.teststeps.request.HttpRequestStepBuilder;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.*;

import static io.swagger.assert4j.properties.Properties.property;

/**
 * Main class for building TestRecipes through code
 */

public class TestRecipeBuilder {
    private static final String TARGET_STEP = "#TestCase#";
    private static ObjectMapper objectMapper;
    private List<TestStepBuilder> testStepBuilders = new LinkedList<>();
    private List<PropertyBuilder> propertyBuilders = new LinkedList<>();
    private final TestCase testCase;
    private ExtractorData extractorData = new ExtractorData();

    public TestRecipeBuilder() {
        testCase = new TestCase();
        testCase.setFailTestCaseOnError(true);
    }

    /**
     * Creates a TestRecipe object from an existing JSON recipe
     *
     * @param jsonText the json recipe
     * @return the created TestRecipe
     * @throws IOException if there was an error during deserialization
     */
    public static TestRecipe createFrom(String jsonText) throws IOException {
        TestCase testCase = getObjectMapper().readValue(jsonText, TestCase.class);
        return new TestRecipe(testCase);
    }

    /**
     * Builds a recipe for the specified TestStep builders
     *
     * @param builders the TestStep builders
     * @return the resulting TestRecipe
     */
    public static TestRecipe buildRecipe(TestStepBuilder... builders) {
        return newTestRecipe(builders).buildTestRecipe();
    }

    /**
     * Builds a recipe for the specified TestStep builders
     *
     * @param name     the name of the recipe
     * @param builders the TestStep builders
     * @return the resulting TestRecipe
     */
    public static TestRecipe buildRecipe(String name, TestStepBuilder... builders) {
        return newTestRecipe(builders).named(name).buildTestRecipe();
    }

    private static ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper()
                    .addMixIn(TestStep.class, TestStepMixin.class)
                    .addMixIn(DataGenerator.class, DataGeneratorTypeMixin.class)
                    .addMixIn(Assertion.class, AssertionMixin.class)
                    .registerModule(new ParameterNamesModule())
                    .registerModule(new Jdk8Module())
                    .registerModule(new JavaTimeModule());
        }

        return objectMapper;
    }

    /**
     * Adds a TestStep to this TestRecipe
     *
     * @param testStepBuilder the builder for the TestTep to add
     * @return
     */

    public TestRecipeBuilder addStep(TestStepBuilder testStepBuilder) {
        this.testStepBuilders.add(testStepBuilder);
        return this;
    }

    /**
     * Certificate file can be added on the TestEngine in allowedFilePath directory. Otherwise it should be provided by the client.
     * Client will throw an exception if file doesn't exist on client and on server.
     *
     * @param filePath Certificate file path on local computer or on server
     */
    public TestRecipeBuilder withClientCertificate(String filePath) {
        testCase.setClientCertFileName(filePath);
        return this;
    }

    /**
     * @param password password for client certificate
     */

    public TestRecipeBuilder withClientCertificatePassword(String password) {
        testCase.setClientCertPassword(password);
        return this;
    }

    public TestRecipeBuilder named(String recipeName) {
        testCase.setName(recipeName);
        return this;
    }

    /**
     * @param propertyBuilders the PropertyBuilders for properties to add to the created TestCase
     */

    public TestRecipeBuilder withProperties(PropertyBuilder... propertyBuilders) {
        Arrays.asList(propertyBuilders).forEach(propertyBuilder -> {
            if (propertyBuilder != null) {
                this.propertyBuilders.add(propertyBuilder);
            }
        });
        return this;
    }

    /**
     * Adds a single property to the created TestCase
     *
     * @param key
     * @param value
     */

    public TestRecipeBuilder withProperty(String key, String value) {
        this.propertyBuilders.add(property(key, value));
        return this;
    }

    /**
     * @return the TestRecipe built from configured TestSteps and properties
     */
    public TestRecipe buildTestRecipe() {
        addTestSteps();
        addProperties();
        return new TestRecipe(testCase, extractorData);
    }

    private void addProperties() {
        Map<String, String> propertiesMap = new HashMap<>();
        propertyBuilders.forEach(propertyBuilder -> {
            PropertyBuilder.Property property = propertyBuilder.build();
            propertiesMap.put(property.getKey(), property.getValue());
        });
        testCase.setProperties(propertiesMap);
    }

    private void addTestSteps() {
        List<TestStep> testSteps = new LinkedList<>();
        TestStep currentTestStep = null;
        for (TestStepBuilder testStepBuilder : testStepBuilders) {
            testStepBuilder.setPreviousTestStep(currentTestStep);
            currentTestStep = testStepBuilder.build();
            testSteps.add(currentTestStep);
            if (testStepBuilder instanceof HttpRequestStepBuilder) {
                List<Extractor> extractorList = ((HttpRequestStepBuilder) testStepBuilder).getExtractors();
                TestStep extractedTestStep = handleExtractors(extractorList);
                if (extractedTestStep != null) {
                    testSteps.add(extractedTestStep);
                }
            }
        }
        String nextTestStep = null;
        for (int i = testSteps.size() - 1; i >= 0; i--) {
            TestStep testStep = testSteps.get(i);
            if (testStep instanceof PropertyTransferTestStep) {
                PropertyTransferTestStep transferTestStep = (PropertyTransferTestStep) testStep;
                for (PropertyTransfer propertyTransfer : transferTestStep.getTransfers()) {
                    if (propertyTransfer.getTarget() != null && propertyTransfer.getTarget().getTargetName() == null) {
                        propertyTransfer.getTarget().setTargetName(nextTestStep);
                    }
                }
            }
            nextTestStep = testStep.getName();
        }
        testCase.setTestSteps(testSteps);
    }

    private TestStep handleExtractors(List<Extractor> extractors) {
        PropertyTransferTestStep propertyTransferTestStep = null;
        if (!extractors.isEmpty()) {
            // Add the unique execution key as property, match it in the result report
            withProperty(ExtractorData.EXTRACTOR_DATA_KEY, extractorData.getExtractorDataId());
            PropertyTransferTestStepBuilder propertyTransferTestStepBuilder = new PropertyTransferTestStepBuilder();
            extractors.forEach(extractor -> {
                // Base the extractorId on the property if the path is empty, otherwise on the path
                String extractorId = extractorData
                        .addExtractorOperator(StringUtils.isEmpty(extractor.getPath()) ?
                                extractor.getProperty() : extractor.getPath(), extractor.getOperator());
                withProperty(extractorId, "");
                propertyTransferTestStepBuilder
                        .addTransfer(PropertyTransferBuilder
                                .newTransfer()
                                .named(extractorId)
                                .withSource(PropertyTransferSourceBuilder
                                        .aSource()
                                        .withPath(extractor.getPath())
                                        .withProperty(extractor.getProperty())
                                        .withSourceStep(extractor.getSource())
                                        .withPathLanguage(extractor.getPathLanguage())
                                ).withTarget(PropertyTransferTargetBuilder
                                        .aTarget()
                                        .withProperty(extractorId)
                                        .withTargetStep(TARGET_STEP)));
            });
            propertyTransferTestStep = propertyTransferTestStepBuilder.build();
        }
        return propertyTransferTestStep;
    }

    /**
     * @return a preconfigured instance of this class
     */

    public static TestRecipeBuilder newTestRecipe() {
        return new TestRecipeBuilder();
    }

    /**
     * Creates a TestRecipeBuilder containing the specified TestStepBuilders
     */

    public static TestRecipeBuilder newTestRecipe(TestStepBuilder... testStepBuilders) {
        TestRecipeBuilder recipeBuilder = newTestRecipe();
        for (TestStepBuilder arg : testStepBuilders) {
            recipeBuilder.addStep(arg);
        }

        return recipeBuilder;
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
