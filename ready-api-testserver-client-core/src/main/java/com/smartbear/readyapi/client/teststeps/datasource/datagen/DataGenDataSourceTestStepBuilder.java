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

    public DataGenDataSourceTestStepBuilder withProperty(AbstractDataGeneratorBuilder dataGeneratorBuilder) {
        getDataSourceBuilder().addDataGenerator(dataGeneratorBuilder);
        return this;
    }

    public DataGenDataSourceTestStepBuilder withProperties(AbstractDataGeneratorBuilder... dataGeneratorBuilders) {
        for (AbstractDataGeneratorBuilder dataGeneratorBuilder : dataGeneratorBuilders) {
            getDataSourceBuilder().addDataGenerator(dataGeneratorBuilder);
        }
        return this;
    }

    public DataGenDataSourceTestStepBuilder andProperty(AbstractDataGeneratorBuilder dataGeneratorBuilder) {
        return withProperty(dataGeneratorBuilder);
    }
}