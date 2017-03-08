package com.smartbear.readyapi.readyapi4j.cucumber;

import com.google.common.collect.Lists;
import com.smartbear.readyapi.client.model.TestCase;
import com.smartbear.readyapi.client.model.TestStep;
import com.smartbear.readyapi4j.execution.Execution;
import com.smartbear.readyapi4j.support.AssertionUtils;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.runtime.java.guice.ScenarioScoped;

import javax.inject.Inject;
import java.util.List;

/**
 * Class that builds and runs the recipe to be executed
 */

@ScenarioScoped
public class CucumberRecipeBuilder {

    private final CucumberRecipeExecutor executor;
    private List<TestStep> testSteps = Lists.newArrayList();
    private final TestCase testCase;
    private boolean assertResult = true;

    @Inject
    public CucumberRecipeBuilder(CucumberRecipeExecutor executor) {
        this.executor = executor;

        testCase = new TestCase();
        testCase.setFailTestCaseOnError(true);
    }

    /**
     * This is the actual handler that runs the created TestCase after it
     * has been assembled by the invoked StepDefs during Cucumber execution.
     * If the executor runs the recipe synchronously this call will also
     * asserts that the execution finished without errors
     *
     * @param scenario
     */

    @After
    public void run(Scenario scenario) {
        testCase.setTestSteps(testSteps);
        Execution execution = executor.runTestCase(testCase, scenario);

        if( assertResult && !executor.isAsync() ) {
            AssertionUtils.assertExecution(execution);
        }
    }

    /**
     * The TestCase that is being built by this builder. Use this if you want to
     * customize any of the TestCase properties before execution.
     *
     * @return the current TestCase
     */

    public TestCase getTestCase() {
        return testCase;
    }

    /**
     * Adds a TestStep to the TestCase that is being built; your StepDefs should
     * call this method whenever a new TestStep should get added for execution.
     *
     * @param testStep the TestStep to add
     */

    public void addTestStep(TestStep testStep) {
        testSteps.add(testStep);
    }

    /**
     * Returns the underlying executor used for executing the constructed TestCase
     *
     * @return the underlying executor
     */

    public CucumberRecipeExecutor getExecutor() {
        return executor;
    }

    /**
     * @return if this builder will assert synchronous execution results
     */

    public boolean isAssertResult() {
        return assertResult;
    }

    /**
     * Sets if this builder should assert synchronous execution results
     * (default is true)
     *
     * @param assertResult if this builder should assert synchronous execution results
     */

    public void setAssertResult(boolean assertResult) {
        this.assertResult = assertResult;
    }
}
