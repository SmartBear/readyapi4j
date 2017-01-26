package com.smartbear.readyapi4j.dsl.pro

import com.smartbear.readyapi.client.model.DataSourceTestStep
import com.smartbear.readyapi.client.model.ExcelDataSource
import com.smartbear.readyapi4j.TestRecipe
import org.junit.Test

import static com.smartbear.readyapi4j.dsl.ServerTestDsl.recipe

class ExcelDataSourceTestStepDslTest {

    private static final String EXCEL_FILE_PATH = 'C:/workspace/data-source-excel.xls'
    private static final String SHEET_NAME = 'Sheet1'
    private static final String CELL = 'A1'

    @Test
    void createsRecipeWithExcelDataSource() throws Exception {
        TestRecipe testRecipe = recipe {
            excelDataSource "DataSourceStep", {
                filePath EXCEL_FILE_PATH
                worksheet SHEET_NAME
                startAtCell CELL
                ignoreEmpty
            }
        }
        DataSourceTestStep testStep = testRecipe.testCase.testSteps[0] as DataSourceTestStep
        ExcelDataSource dataSource = testStep.dataSource.excel
        assert dataSource.file == EXCEL_FILE_PATH
        assert dataSource.worksheet == SHEET_NAME
        assert dataSource.startAtCell == CELL
        assert dataSource.ignoreEmpty
    }
}
