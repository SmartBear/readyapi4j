package com.smartbear.readyapi4j.dsl.pro

import com.smartbear.readyapi4j.dsl.DslDelegate

/**
 * The delegate responding to commands inside the "recipe" closure of ServerTestDsl.
 */
class ProDslDelegate extends DslDelegate {

    void excelDataSource(String testStepName, @DelegatesTo(ExcelDataSourceTestStepDelegate) Closure dataSourceConfig) {
        addDataSourceTestStep(new ExcelDataSourceTestStepDelegate(testStepName), dataSourceConfig)
    }

    void fileDataSource(String testStepName, @DelegatesTo(FileDataSourceTestStepDelegate) Closure dataSourceConfig) {
        addDataSourceTestStep(new FileDataSourceTestStepDelegate(testStepName), dataSourceConfig)
    }

    void gridDataSource(String testStepName, @DelegatesTo(GridDataSourceTestStepDelegate) Closure dataSourceConfig) {
        addDataSourceTestStep(new GridDataSourceTestStepDelegate(testStepName), dataSourceConfig)
    }

    private void addDataSourceTestStep(DataSourceTestStepDelegate delegate, Closure dataSourceConfig) {
        dataSourceConfig.delegate = delegate
        dataSourceConfig.resolveStrategy = Closure.DELEGATE_FIRST
        dataSourceConfig.call()

        testStepBuilders.add(delegate.dataSourceTestStepBuilder)
    }
}
