package io.swagger.assert4j.testengine.teststeps.datasource;

import io.swagger.assert4j.client.model.DataSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GridDataSourceBuilder implements DataSourceBuilder {
    private Map<String, List<String>> propertyValues = new HashMap<>();

    public GridDataSourceBuilder addProperty(String property, List<String> values) {
        propertyValues.put(property, values);
        return this;
    }

    @Override
    public DataSource build() {
        DataSource dataSource = new DataSource();
        dataSource.setGrid(propertyValues);
        return dataSource;
    }
}
