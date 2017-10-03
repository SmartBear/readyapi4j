package io.swagger.assert4j.teststeps.properties;

import io.swagger.assert4j.client.model.PropertiesTestStep;
import io.swagger.assert4j.teststeps.TestStepBuilder;
import io.swagger.assert4j.teststeps.TestStepTypes;

import java.util.Map;

public class PropertiesTestStepBuilder implements TestStepBuilder<PropertiesTestStep> {
    private final PropertiesTestStep propertiesTestStep = new PropertiesTestStep();

    public PropertiesTestStepBuilder() {
    }

    public PropertiesTestStepBuilder(Map<String, String> properties) {
        propertiesTestStep.setProperties(properties);
    }

    public PropertiesTestStepBuilder named(String testStepName) {
        propertiesTestStep.setName(testStepName);
        return this;
    }

    public PropertiesTestStepBuilder addProperty(String name, String value) {
        propertiesTestStep.getProperties().put(name, value);
        return this;
    }

    public PropertiesTestStepBuilder addProperties(Map<String, String> properties) {
        propertiesTestStep.getProperties().putAll(properties);
        return this;
    }

    @Override
    public PropertiesTestStep build() {
        propertiesTestStep.setType(TestStepTypes.PROPERTIES.getName());
        return propertiesTestStep;
    }
}
