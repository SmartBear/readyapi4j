package io.swagger.assert4j.testserver.teststeps;

import io.swagger.assert4j.testserver.teststeps.datasource.DataSourceTestStepBuilder;
import io.swagger.assert4j.testserver.teststeps.datasource.ExcelDataSourceTestStepBuilder;
import io.swagger.assert4j.testserver.teststeps.datasource.FileDataSourceTestStepBuilder;
import io.swagger.assert4j.testserver.teststeps.datasource.GridDataSourceTestStepBuilder;
import io.swagger.assert4j.testserver.teststeps.datasource.datagen.DataGenDataSourceTestStepBuilder;
import io.swagger.assert4j.teststeps.TestSteps;

/**
 * Factory methods for test steps that can only be executed by ReadyAPI TestServer, not by SoapUI.
 */
public class ServerTestSteps extends TestSteps {
    public static DataSourceTestStepBuilder dataSource() {
        return new DataSourceTestStepBuilder();
    }

    public static GridDataSourceTestStepBuilder gridDataSource() {
        return new GridDataSourceTestStepBuilder();
    }

    public static ExcelDataSourceTestStepBuilder excelDataSource() {
        return new ExcelDataSourceTestStepBuilder();
    }

    public static FileDataSourceTestStepBuilder fileDataSource() {
        return new FileDataSourceTestStepBuilder();
    }

    public static DataGenDataSourceTestStepBuilder dataGenDataSource() {
        return new DataGenDataSourceTestStepBuilder();
    }

    public static GridDataSourceTestStepBuilder gridDataSource(String name) {
        return (GridDataSourceTestStepBuilder) new GridDataSourceTestStepBuilder().named(name);
    }

    public static ExcelDataSourceTestStepBuilder excelDataSource(String name) {
        return (ExcelDataSourceTestStepBuilder) new ExcelDataSourceTestStepBuilder().named(name);
    }

    public static FileDataSourceTestStepBuilder fileDataSource(String name) {
        return (FileDataSourceTestStepBuilder) new FileDataSourceTestStepBuilder().named(name);
    }
}
