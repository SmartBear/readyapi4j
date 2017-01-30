package com.smartbear.readyapi4j.dsl.pro

import com.smartbear.readyapi4j.dsl.DslDelegate


class ProDslDelegate extends DslDelegate {

    void excelDataSource(String testStepName, @DelegatesTo(ExcelDataSourceTestStepDelegate) Closure dataSourceConfig) {
        ExcelDataSourceTestStepDelegate delegate = new ExcelDataSourceTestStepDelegate(testStepName)
        dataSourceConfig.delegate = delegate
        dataSourceConfig.resolveStrategy = Closure.DELEGATE_FIRST
        dataSourceConfig.call()

        testStepBuilders.add(delegate.dataSourceTestStepBuilder)
    }
}
