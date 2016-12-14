package com.smartbear.readyapi4j.teststeps.datasource;

import com.smartbear.readyapi.client.model.DataSourceTestStep;
import com.smartbear.readyapi.client.model.TestStep;
import com.smartbear.readyapi4j.teststeps.TestStepBuilder;
import com.smartbear.readyapi4j.teststeps.TestStepTypes;

import java.util.LinkedList;
import java.util.List;

public class DataSourceTestStepBuilder<DataSourceBuilderType extends DataSourceBuilder> implements TestStepBuilder<DataSourceTestStep> {
    private DataSourceTestStep testStep = new DataSourceTestStep();
    private DataSourceBuilderType dataSourceBuilder;
    private List<TestStep> nestedTestSteps = new LinkedList<>();

    public DataSourceTestStepBuilder named(String name) {
        testStep.setName(name);
        return this;
    }

    public DataSourceTestStepBuilder withDataSource(DataSourceBuilderType dataSourceBuilder) {
        this.dataSourceBuilder = dataSourceBuilder;
        return this;
    }

    protected DataSourceBuilderType getDataSourceBuilder() {
        return dataSourceBuilder;
    }

    public DataSourceTestStepBuilder addTestStep(TestStepBuilder testStepBuilder) {
        this.nestedTestSteps.add(testStepBuilder.build());
        return this;
    }

    @Override
    public DataSourceTestStep build() {
        testStep.setType(TestStepTypes.DATA_SOURCE.getName());
        testStep.setDataSource(dataSourceBuilder.build());
        testStep.setTestSteps(nestedTestSteps);
        return testStep;
    }
}
