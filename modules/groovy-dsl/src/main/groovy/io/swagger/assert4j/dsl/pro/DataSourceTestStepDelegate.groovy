package io.swagger.assert4j.dsl.pro

import io.swagger.assert4j.TestRecipeBuilder
import io.swagger.assert4j.testengine.teststeps.datasource.DataSourceTestStepBuilder
import io.swagger.assert4j.teststeps.TestStepBuilder

import static groovy.lang.Closure.DELEGATE_FIRST

/**
 * Base class for data source test step delegates
 */
class DataSourceTestStepDelegate<Builder extends DataSourceTestStepBuilder> {

    protected Builder dataSourceTestStepBuilder

    DataSourceTestStepDelegate(Builder dataSourceTestStepBuilder, String testStepName) {
        this.dataSourceTestStepBuilder = dataSourceTestStepBuilder
        this.dataSourceTestStepBuilder.named(testStepName)
    }

    void testSteps(@DelegatesTo(ProDslDelegate) Closure testStepsDefinition) {
        final List<TestStepBuilder> testStepBuilders = []
        ProDslDelegate delegate = new ProDslDelegate(new TestRecipeBuilder() {
            @Override
            TestRecipeBuilder addStep(TestStepBuilder testStepBuilder) {
                testStepBuilders.add(testStepBuilder)
                return this
            }
        })
        testStepsDefinition.delegate = delegate
        testStepsDefinition.resolveStrategy = DELEGATE_FIRST
        testStepsDefinition.call()

        testStepBuilders.each { testStepBuilder -> dataSourceTestStepBuilder.addTestStep(testStepBuilder) }
    }
}
