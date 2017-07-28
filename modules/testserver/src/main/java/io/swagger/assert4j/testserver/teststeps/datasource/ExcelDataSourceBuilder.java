package io.swagger.assert4j.testserver.teststeps.datasource;

import io.swagger.assert4j.client.model.DataSource;
import io.swagger.assert4j.client.model.ExcelDataSource;

import java.util.ArrayList;
import java.util.List;

public class ExcelDataSourceBuilder implements DataSourceBuilder {
    private ExcelDataSource excelDataSource = new ExcelDataSource();
    private List<String> properties = new ArrayList<>();

    public ExcelDataSourceBuilder addProperty(String propertyName) {
        properties.add(propertyName);
        return this;
    }

    public ExcelDataSourceBuilder withProperties(List<String> properties) {
        this.properties.addAll(properties);
        return this;
    }

    public ExcelDataSourceBuilder withFilePath(String filePath) {
        excelDataSource.setFile(filePath);
        return this;
    }

    public ExcelDataSourceBuilder withWorksheet(String worksheet) {
        excelDataSource.setWorksheet(worksheet);
        return this;
    }

    public ExcelDataSourceBuilder startAtCell(String cell) {
        excelDataSource.setStartAtCell(cell);
        return this;
    }

    public ExcelDataSourceBuilder ignoreEmpty() {
        excelDataSource.setIgnoreEmpty(true);
        return this;
    }

    @Override
    public DataSource build() {
        DataSource dataSource = new DataSource();
        dataSource.setProperties(properties);
        dataSource.setExcel(excelDataSource);
        return dataSource;
    }
}
