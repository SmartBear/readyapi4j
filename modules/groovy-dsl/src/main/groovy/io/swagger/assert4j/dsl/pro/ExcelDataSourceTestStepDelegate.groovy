package io.swagger.assert4j.dsl.pro

import io.swagger.assert4j.testserver.teststeps.datasource.ExcelDataSourceTestStepBuilder

/**
 * Delegate for the 'usingExcelFile' closure in 'recipe'
 */
class ExcelDataSourceTestStepDelegate extends DataSourceTestStepDelegate<ExcelDataSourceTestStepBuilder> {

    ExcelDataSourceTestStepDelegate(String filePath, String testStepName) {
        super(new ExcelDataSourceTestStepBuilder(), testStepName)
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
}
