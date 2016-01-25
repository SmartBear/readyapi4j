package com.smartbear.readyapi.client.teststeps.datasource;

import io.swagger.client.model.DataSource;
import io.swagger.client.model.FileDataSource;

import java.util.ArrayList;
import java.util.List;

public class FileDataSourceBuilder implements DataSourceBuilder {
    private FileDataSource fileDataSource = new FileDataSource();
    private List<String> properties = new ArrayList<>();

    public FileDataSourceBuilder addProperty(String propertyName) {
        properties.add(propertyName);
        return this;
    }

    public FileDataSourceBuilder withFilePath(String filePath) {
        fileDataSource.setFile(filePath);
        return this;
    }

    public FileDataSourceBuilder withCharSet(String charSet) {
        fileDataSource.setCharset(charSet);
        return this;
    }

    public FileDataSourceBuilder withSeparator(String separator) {
        fileDataSource.setSeparator(separator);
        return this;
    }

    public FileDataSourceBuilder trim() {
        fileDataSource.setTrim(true);
        return this;
    }

    public FileDataSourceBuilder quotedValues() {
        fileDataSource.setQuotedValues(true);
        return this;
    }

    @Override
    public DataSource build() {
        DataSource dataSource = new DataSource();
        dataSource.setProperties(properties);
        dataSource.setFile(fileDataSource);
        return dataSource;
    }
}
