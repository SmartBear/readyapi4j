package io.swagger.assert4j.testserver.teststeps.datasource.datagen;


import io.swagger.assert4j.client.model.DataGenDataSource;
import io.swagger.assert4j.client.model.DataGenerator;
import io.swagger.assert4j.client.model.DataSource;
import io.swagger.assert4j.testserver.teststeps.datasource.DataSourceBuilder;

import java.util.ArrayList;
import java.util.List;

class DataGenDataSourceBuilder implements DataSourceBuilder {

    private final DataGenDataSource dataGenDataSource = new DataGenDataSource();
    private List<AbstractDataGeneratorBuilder> dataGeneratorBuilders = new ArrayList<>();
    private int numberOfRows = 10; //default value

    DataGenDataSourceBuilder withNumberOfRows(int numberOfRows) {
        if (numberOfRows < 1) {
            throw new IllegalArgumentException("Number of rows should be greater than 0, actual: " + numberOfRows);
        }
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
        dataGenDataSource.setNumberOfRows(String.valueOf(numberOfRows));

        DataSource dataSource = new DataSource();
        dataSource.setDataGen(dataGenDataSource);
        return dataSource;
    }
}
