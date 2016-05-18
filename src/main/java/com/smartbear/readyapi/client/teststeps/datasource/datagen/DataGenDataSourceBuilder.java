package com.smartbear.readyapi.client.teststeps.datasource.datagen;


import com.smartbear.readyapi.client.model.DataGenDataSource;
import com.smartbear.readyapi.client.model.DataGenerator;
import com.smartbear.readyapi.client.model.DataSource;
import com.smartbear.readyapi.client.teststeps.datasource.DataSourceBuilder;

import java.util.ArrayList;
import java.util.List;

class DataGenDataSourceBuilder implements DataSourceBuilder {

    private final DataGenDataSource dataGenDataSource = new DataGenDataSource();
    private List<AbstractDataGeneratorBuilder> dataGeneratorBuilders = new ArrayList<>();
    private int numberOfRows;

    DataGenDataSourceBuilder withNumberOfRows(int numberOfRows) {
        this.numberOfRows = numberOfRows;
        return this;
    }

    DataGenDataSourceBuilder addDataGenerator(AbstractDataGeneratorBuilder dataGeneratorBuilder) {
        dataGeneratorBuilders.add(dataGeneratorBuilder);
        return this;
    }

    @Override
    public DataSource build() {
        List<DataGenerator> dataGenerators = new ArrayList<>();
        for (AbstractDataGeneratorBuilder dataGeneratorBuilder : dataGeneratorBuilders) {
            dataGenerators.add(dataGeneratorBuilder.build());
        }
        dataGenDataSource.setDataGenerators(dataGenerators);
        dataGenDataSource.setNumberOfRows(numberOfRows);

        DataSource dataSource = new DataSource();
        dataSource.setDataGen(dataGenDataSource);
        return dataSource;
    }
}
