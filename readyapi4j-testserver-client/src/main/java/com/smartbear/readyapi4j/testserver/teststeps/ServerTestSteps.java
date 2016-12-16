package com.smartbear.readyapi4j.testserver.teststeps;

import com.smartbear.readyapi4j.teststeps.TestSteps;
import com.smartbear.readyapi4j.testserver.teststeps.datasource.DataSourceTestStepBuilder;
import com.smartbear.readyapi4j.testserver.teststeps.datasource.ExcelDataSourceTestStepBuilder;
import com.smartbear.readyapi4j.testserver.teststeps.datasource.FileDataSourceTestStepBuilder;
import com.smartbear.readyapi4j.testserver.teststeps.datasource.GridDataSourceTestStepBuilder;
import com.smartbear.readyapi4j.testserver.teststeps.datasource.datagen.DataGenDataSourceTestStepBuilder;

/**
 * Factory methods for test steps that can only be executed by Ready! API TestServer, not by SoapUI.
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
