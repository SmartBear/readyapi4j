package com.smartbear.readyapi4j.dsl.pro

import com.smartbear.readyapi4j.testserver.teststeps.datasource.GridDataSourceTestStepBuilder

/**
 * Delegate for the 'gridDataSource'  closure in 'recipe'
 */
class GridDataSourceTestStepDelegate extends DataSourceTestStepDelegate<GridDataSourceTestStepBuilder> {
    GridDataSourceTestStepDelegate(String testStepName) {
        super(new GridDataSourceTestStepBuilder(), testStepName)
    }

    void property(String propertyName, List<String> values) {
        dataSourceTestStepBuilder.addProperty(propertyName, values)
    }
}
