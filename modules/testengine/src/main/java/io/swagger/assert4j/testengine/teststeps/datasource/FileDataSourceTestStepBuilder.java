package io.swagger.assert4j.testengine.teststeps.datasource;

public class FileDataSourceTestStepBuilder extends DataSourceTestStepBuilder<FileDataSourceBuilder> {

    public FileDataSourceTestStepBuilder() {
        withDataSource(new FileDataSourceBuilder());
    }

    public FileDataSourceTestStepBuilder addProperty(String propertyName) {
        getDataSourceBuilder().addProperty(propertyName);
        return this;
    }

    public FileDataSourceTestStepBuilder trim() {
        getDataSourceBuilder().trim();
        return this;
    }

    public FileDataSourceTestStepBuilder withCharSet(String charSet) {
        getDataSourceBuilder().withCharSet(charSet);
        return this;
    }

    public FileDataSourceTestStepBuilder withSeparator(String separator) {
        getDataSourceBuilder().withSeparator(separator);
        return this;
    }

    public FileDataSourceTestStepBuilder withFilePath(String filePath) {
        getDataSourceBuilder().withFilePath(filePath);
        return this;
    }

    public FileDataSourceTestStepBuilder quotedValues() {
        getDataSourceBuilder().quotedValues();
        return this;
    }
}
