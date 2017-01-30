package com.smartbear.readyapi4j.dsl.pro

import com.smartbear.readyapi4j.testserver.teststeps.datasource.ExcelDataSourceTestStepBuilder

import static groovy.lang.Closure.DELEGATE_FIRST

class ExcelDataSourceTestStepDelegate {
    ExcelDataSourceTestStepBuilder dataSourceTestStepBuilder

    ExcelDataSourceTestStepDelegate(String testStepName) {
        this.dataSourceTestStepBuilder = new ExcelDataSourceTestStepBuilder()
        dataSourceTestStepBuilder.named(testStepName)
    }

    void filePath(String filePath) {
        dataSourceTestStepBuilder.withFilePath(filePath)
    }

    void worksheet(String worksheet) {
        dataSourceTestStepBuilder.withWorksheet(worksheet)
    }

    void startAtCell(String startCell) {
        dataSourceTestStepBuilder.startAtCell(startCell)
    }

    boolean getIgnoreEmpty() {
        dataSourceTestStepBuilder.ignoreEmpty()
    }

    void propertyNames(List<String> properties = []) {
        dataSourceTestStepBuilder.withProperties(properties)
    }

    void testSteps(@DelegatesTo(ProDslDelegate) Closure testStepsDefinition) {
        ProDslDelegate delegate = new ProDslDelegate()
        testStepsDefinition.delegate = delegate
        testStepsDefinition.resolveStrategy = DELEGATE_FIRST
        testStepsDefinition.call()

        delegate.testStepBuilders.each { testStepBuilder -> dataSourceTestStepBuilder.addTestStep(testStepBuilder) }
    }
}
