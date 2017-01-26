package com.smartbear.readyapi4j.dsl.pro

import com.smartbear.readyapi4j.testserver.teststeps.datasource.ExcelDataSourceTestStepBuilder

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
}
