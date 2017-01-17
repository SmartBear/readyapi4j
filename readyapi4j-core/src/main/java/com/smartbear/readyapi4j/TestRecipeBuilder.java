package com.smartbear.readyapi4j;

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
import com.smartbear.readyapi.client.model.PropertyTransfer;
import com.smartbear.readyapi.client.model.PropertyTransferTestStep;
import com.smartbear.readyapi.client.model.RealNumberDataGenerator;
import com.smartbear.readyapi.client.model.StateNameDataGenerator;
import com.smartbear.readyapi.client.model.StringDataGenerator;
import com.smartbear.readyapi.client.model.TestCase;
import com.smartbear.readyapi.client.model.TestStep;
import com.smartbear.readyapi.client.model.UKPostCodeDataGenerator;
import com.smartbear.readyapi.client.model.USZIPCodeDataGenerator;
import com.smartbear.readyapi.client.model.ValuesFromSetDataGenerator;
import com.smartbear.readyapi4j.extractor.Extractor;
import com.smartbear.readyapi4j.extractor.ExtractorData;
import com.smartbear.readyapi4j.properties.PropertyBuilder;
import com.smartbear.readyapi4j.teststeps.TestStepBuilder;
import com.smartbear.readyapi4j.teststeps.propertytransfer.PropertyTransferBuilder;
import com.smartbear.readyapi4j.teststeps.propertytransfer.PropertyTransferSourceBuilder;
import com.smartbear.readyapi4j.teststeps.propertytransfer.PropertyTransferTargetBuilder;
import com.smartbear.readyapi4j.teststeps.propertytransfer.PropertyTransferTestStepBuilder;
import com.smartbear.readyapi4j.teststeps.request.HttpRequestStepBuilder;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.smartbear.readyapi4j.properties.Properties.property;

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

    public static TestRecipe createFrom(String jsonText) throws IOException {
        TestCase testCase = getObjectMapper().readValue(jsonText, TestCase.class);
        return new TestRecipe(testCase);
    }

    private static ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
            objectMapper.addMixIn(TestStep.class, TestStepMixin.class);
            objectMapper.addMixIn(DataGenerator.class, DataGeneratorTypeMixin.class);
            objectMapper.addMixIn(Assertion.class, AssertionMixin.class);
        }
        return objectMapper;
    }

    public TestRecipeBuilder addStep(TestStepBuilder testStepBuilder) {
        this.testStepBuilders.add(testStepBuilder);
        return this;
    }

    /**
     * Certificate file can be added on the TestServer in allowedFilePath directory. Otherwise it should be provided by the client.
     * Client will throw an exception if file doesn't exist on client and on server.
     *
     * @param filePath Certificate file path
     */
    public TestRecipeBuilder withClientCertificate(String filePath) {
        testCase.setClientCertFileName(filePath);
        return this;
    }

    public TestRecipeBuilder withClientCertificatePassword(String password) {
        testCase.setClientCertPassword(password);
        return this;
    }

    public TestRecipeBuilder withProperties(PropertyBuilder... propertyBuilders) {
        Arrays.asList(propertyBuilders).forEach(propertyBuilder -> {
            if (propertyBuilder != null) {
                this.propertyBuilders.add(propertyBuilder);
            }
        });
        return this;
    }

    public TestRecipeBuilder withProperty(String key, String value) {
        this.propertyBuilders.add(property(key, value));
        return this;
    }

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
