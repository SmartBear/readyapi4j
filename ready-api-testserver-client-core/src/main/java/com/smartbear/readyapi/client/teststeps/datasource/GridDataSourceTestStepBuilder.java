package com.smartbear.readyapi.client.teststeps.datasource;

import java.util.List;

public class GridDataSourceTestStepBuilder extends DataSourceTestStepBuilder<GridDataSourceBuilder> {

    public GridDataSourceTestStepBuilder() {
        withDataSource(new GridDataSourceBuilder());
    }

    public GridDataSourceTestStepBuilder addProperty(String property, List<String> propertyValues) {
        getDataSourceBuilder().addProperty(property, propertyValues);
        return this;
    }
}
