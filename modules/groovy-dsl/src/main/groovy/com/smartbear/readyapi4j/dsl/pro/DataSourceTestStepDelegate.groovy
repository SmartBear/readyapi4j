package com.smartbear.readyapi4j.dsl.pro

import com.smartbear.readyapi4j.TestRecipeBuilder
import com.smartbear.readyapi4j.testengine.teststeps.datasource.DataSourceTestStepBuilder
import com.smartbear.readyapi4j.teststeps.TestStepBuilder

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
