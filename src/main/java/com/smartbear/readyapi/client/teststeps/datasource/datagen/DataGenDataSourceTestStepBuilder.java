package com.smartbear.readyapi.client.teststeps.datasource.datagen;


import com.smartbear.readyapi.client.teststeps.datasource.DataSourceTestStepBuilder;

public class DataGenDataSourceTestStepBuilder extends DataSourceTestStepBuilder<DataGenDataSourceBuilder> {

    public DataGenDataSourceTestStepBuilder() {
        withDataSource(new DataGenDataSourceBuilder());
    }

    public DataGenDataSourceTestStepBuilder withNumberOfRows(int numberOfRows) {
        getDataSourceBuilder().withNumberOfRows(numberOfRows);
        return this;
    }

    public DataGenDataSourceTestStepBuilder addDataGenerator(AbstractDataGeneratorBuilder dataGeneratorBuilder) {
        getDataSourceBuilder().addDataGenerator(dataGeneratorBuilder);
        return this;
    }
}