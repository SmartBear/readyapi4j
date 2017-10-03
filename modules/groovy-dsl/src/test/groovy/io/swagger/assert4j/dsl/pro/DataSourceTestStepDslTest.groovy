package io.swagger.assert4j.dsl.pro

import io.swagger.assert4j.client.model.DataSourceTestStep
import io.swagger.assert4j.client.model.ExcelDataSource
import io.swagger.assert4j.client.model.FileDataSource
import io.swagger.assert4j.client.model.RestTestRequestStep
import io.swagger.assert4j.TestRecipe
import org.junit.Test

import static io.swagger.assert4j.dsl.ServerTestDsl.recipe

class DataSourceTestStepDslTest {

    private static final String EXCEL_FILE_PATH = 'C:/workspace/data-source-excel.xls'
    private static final String FILE_PATH = 'C:/workspace/data-source.csv'
    private static final String SHEET_NAME = 'Sheet1'
    private static final String CELL = 'A1'
    private static List<String> PROPERTIES = ['Name', 'Address']

    @Test
    void createsRecipeWithExcelDataSource() throws Exception {
        TestRecipe testRecipe = recipe {
            usingExcelFile EXCEL_FILE_PATH, "ExcelDataSource", {
                worksheet SHEET_NAME
                startAtCell CELL
                ignoreEmpty
                propertyNames PROPERTIES

                testSteps {
                    get 'http://somehost.com'
                }
            }
        }
        DataSourceTestStep testStep = testRecipe.testCase.testSteps[0] as DataSourceTestStep
        ExcelDataSource dataSource = testStep.dataSource.excel
        assert dataSource.file == EXCEL_FILE_PATH
        assert dataSource.worksheet == SHEET_NAME
        assert dataSource.startAtCell == CELL
        assert dataSource.ignoreEmpty
        assertPropertiesAndNestedTestStep(testStep)
    }

    @Test
    void createsRecipeWithFileDataSource() throws Exception {
        TestRecipe testRecipe = recipe {
            usingCsvFile FILE_PATH, "FileDataSource", {
                charSet 'UTF-8'
                separator ';'
                trim
                quotedValues
                propertyNames PROPERTIES
                testSteps {
                    get 'http://somehost.com'
                }
            }
        }
        DataSourceTestStep testStep = testRecipe.testCase.testSteps[0] as DataSourceTestStep
        FileDataSource dataSource = testStep.dataSource.file
        assert dataSource.file == FILE_PATH
        assert dataSource.charset == 'UTF-8'
        assert dataSource.separator == ';'
        assert dataSource.trim
        assert dataSource.quotedValues
        assertPropertiesAndNestedTestStep(testStep)
    }

    @Test
    void createsRecipeWithGridDataSource() throws Exception {
        Map<String, List<String>> data = [name: ['value1', 'value2'], name2: ['value3']]
        TestRecipe testRecipe = recipe {
            usingData data
        }
        DataSourceTestStep testStep = testRecipe.testCase.testSteps[0] as DataSourceTestStep
        assert testStep.dataSource.grid == data
    }

    private static void assertPropertiesAndNestedTestStep(DataSourceTestStep testStep) {
        assert testStep.dataSource.properties == PROPERTIES
        assert (testStep.testSteps[0] as RestTestRequestStep).URI == 'http://somehost.com'
    }
}
