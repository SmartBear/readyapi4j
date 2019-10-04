package io.swagger.assert4j.teststeps.plugin;

import com.google.common.collect.Maps;
import io.swagger.assert4j.client.model.PluginTestStep;
import io.swagger.assert4j.teststeps.TestStepBuilder;

import java.util.Map;

public class PluginTestStepBuilder implements TestStepBuilder<PluginTestStep> {
    private final PluginTestStep pluginTestStep = new PluginTestStep();

    public PluginTestStepBuilder(String pluginTestStepType) {
        pluginTestStep.setType(pluginTestStepType);
    }

    public PluginTestStepBuilder named(String testStepName) {
        pluginTestStep.setName(testStepName);
        return this;
    }

    public PluginTestStepBuilder withConfigProperties(Map<String, Object> configuration) {
        if (pluginTestStep.getConfiguration() == null) {
            pluginTestStep.setConfiguration(Maps.newConcurrentMap());
        }
        pluginTestStep.getConfiguration().putAll(configuration);
        return this;
    }

    public PluginTestStepBuilder withConfigProperty(String propertyName, Object value) {
        if (pluginTestStep.getConfiguration() == null) {
            pluginTestStep.setConfiguration(Maps.newConcurrentMap());
        }
        pluginTestStep.getConfiguration().put(propertyName, value);
        return this;
    }

    public TestStepBuilder andWithConfigProperty(String propertyName, Object value) {
        return withConfigProperty(propertyName, value);
    }

    @Override
    public PluginTestStep build() {
        return pluginTestStep;
    }
}
