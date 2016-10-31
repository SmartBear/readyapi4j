package com.smartbear.readyapi.client;

import com.smartbear.readyapi.client.model.TestCase;
import com.smartbear.readyapi.client.model.TestStep;
import com.smartbear.readyapi.client.properties.PropertyBuilder;
import com.smartbear.readyapi.client.teststeps.TestStepBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.smartbear.readyapi.client.properties.Properties.property;

public class TestRecipeBuilder {
    private List<TestStepBuilder> testStepBuilders = new LinkedList<>();
    private List<PropertyBuilder> propertyBuilders = new LinkedList<>();
    private final TestCase testCase;

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
        addTestSteps(testCase);
        addProperties(testCase);
        return new TestRecipe(testCase);
    }

    private void addProperties(TestCase testCase){
        Map<String, String> propertiesMap= new HashMap<>();
        propertyBuilders.forEach(propertyBuilder -> {
            PropertyBuilder.Property property = propertyBuilder.build();
            propertiesMap.put(property.getKey(),property.getValue());
        });
        testCase.setProperties(propertiesMap);
    }

    private void addTestSteps(TestCase testCase) {
        List<TestStep> testSteps = new LinkedList<>();
        for (TestStepBuilder testStepBuilder : testStepBuilders) {
            testSteps.add(testStepBuilder.build());
        }
        testCase.setTestSteps(testSteps);
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
