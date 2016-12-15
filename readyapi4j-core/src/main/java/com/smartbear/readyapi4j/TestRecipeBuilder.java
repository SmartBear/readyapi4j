package com.smartbear.readyapi4j;

import com.smartbear.readyapi.client.model.PropertyTransferTestStep;
import com.smartbear.readyapi.client.model.TestCase;
import com.smartbear.readyapi.client.model.TestStep;
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.smartbear.readyapi4j.properties.Properties.property;

public class TestRecipeBuilder {
    private static final String TARGET_STEP = "#TestCase#";
    private List<TestStepBuilder> testStepBuilders = new LinkedList<>();
    private List<PropertyBuilder> propertyBuilders = new LinkedList<>();
    private final TestCase testCase;
    private ExtractorData extractorData = new ExtractorData();

    public TestRecipeBuilder() {
        testCase = new TestCase();
        testCase.setFailTestCaseOnError(true);
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
        for (TestStepBuilder testStepBuilder : testStepBuilders) {
            testSteps.add(testStepBuilder.build());
            if (testStepBuilder instanceof HttpRequestStepBuilder) {
                List<Extractor> extractorList = ((HttpRequestStepBuilder) testStepBuilder).getExtractors();
                TestStep testStep = handleExtractors(extractorList);
                if (testStep != null) {
                    testSteps.add(testStep);
                }
            }
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
}
