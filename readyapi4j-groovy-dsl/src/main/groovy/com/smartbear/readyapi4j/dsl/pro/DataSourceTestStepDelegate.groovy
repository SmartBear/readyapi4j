package com.smartbear.readyapi4j.dsl.pro

import com.smartbear.readyapi4j.testserver.teststeps.datasource.DataSourceTestStepBuilder

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
        ProDslDelegate delegate = new ProDslDelegate()
        testStepsDefinition.delegate = delegate
        testStepsDefinition.resolveStrategy = DELEGATE_FIRST
        testStepsDefinition.call()

        delegate.testStepBuilders.each { testStepBuilder -> dataSourceTestStepBuilder.addTestStep(testStepBuilder) }
    }
}
