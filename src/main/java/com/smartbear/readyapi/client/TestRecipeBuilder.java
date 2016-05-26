package com.smartbear.readyapi.client;

import com.smartbear.readyapi.client.model.TestCase;
import com.smartbear.readyapi.client.model.TestStep;
import com.smartbear.readyapi.client.teststeps.TestStepBuilder;

import java.util.LinkedList;
import java.util.List;

public class TestRecipeBuilder {
    private List<TestStepBuilder> testStepBuilders = new LinkedList<>();
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
     * Client will throw an exception if file is not doesn't exist on client and on server.
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

    public TestRecipe buildTestRecipe() {
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
