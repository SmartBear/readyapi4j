package com.smartbear.readyapi.client;

import com.smartbear.readyapi.client.teststeps.TestStepBuilder;
import io.swagger.client.model.TestCase;
import io.swagger.client.model.TestStep;

import java.util.LinkedList;
import java.util.List;

public class TestRecipeBuilder {
    private List<TestStepBuilder> testStepBuilders = new LinkedList<>();

    public TestRecipeBuilder addStep(TestStepBuilder testStepBuilder) {
        this.testStepBuilders.add(testStepBuilder);
        return this;
    }

    public TestRecipe buildTestRecipe() {
        TestCase testCase = new TestCase();
        addTestSteps(testCase);
        return new TestRecipe(testCase);
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


}
