package com.smartbear.readyapi.client.teststeps.datasource.datagen;


import com.smartbear.readyapi.client.teststeps.datasource.DataSourceTestStepBuilder;

public class DataGenDataSourceTestStepBuilder extends DataSourceTestStepBuilder<DataGenDataSourceBuilder> {

    public DataGenDataSourceTestStepBuilder() {
        setDataSource(new DataGenDataSourceBuilder());
    }

    public DataGenDataSourceTestStepBuilder setNumberOfRows(int numberOfRows) {
        getDataSourceBuilder().setNumberOfRows(numberOfRows);
        return this;
    }

    public DataGenDataSourceTestStepBuilder addDataGenerator(AbstractDataGeneratorBuilder dataGeneratorBuilder) {
        getDataSourceBuilder().addDataGenerator(dataGeneratorBuilder);
        return this;
    }
}