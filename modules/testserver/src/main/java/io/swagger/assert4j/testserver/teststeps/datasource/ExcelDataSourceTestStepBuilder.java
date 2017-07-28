package io.swagger.assert4j.testserver.teststeps.datasource;

import java.util.List;

public class ExcelDataSourceTestStepBuilder extends DataSourceTestStepBuilder<ExcelDataSourceBuilder> {

    public ExcelDataSourceTestStepBuilder() {
        withDataSource(new ExcelDataSourceBuilder());
    }

    public ExcelDataSourceTestStepBuilder addProperty(String propertyName) {
        getDataSourceBuilder().addProperty(propertyName);
        return this;
    }

    public ExcelDataSourceTestStepBuilder startAtCell(String cell) {
        getDataSourceBuilder().startAtCell(cell);
        return this;
    }

    public ExcelDataSourceTestStepBuilder withWorksheet(String worksheet) {
        getDataSourceBuilder().withWorksheet(worksheet);
        return this;
    }

    public ExcelDataSourceTestStepBuilder withFilePath(String filePath) {
        getDataSourceBuilder().withFilePath(filePath);
        return this;
    }

    public ExcelDataSourceTestStepBuilder ignoreEmpty() {
        getDataSourceBuilder().ignoreEmpty();
        return this;
    }

    public void withProperties(List<String> properties) {
        getDataSourceBuilder().withProperties(properties);
    }
}
