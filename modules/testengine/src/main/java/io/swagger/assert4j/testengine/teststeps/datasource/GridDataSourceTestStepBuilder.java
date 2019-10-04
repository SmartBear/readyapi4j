package io.swagger.assert4j.testengine.teststeps.datasource;

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
