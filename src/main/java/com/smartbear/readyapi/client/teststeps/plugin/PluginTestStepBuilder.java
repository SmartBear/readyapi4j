package com.smartbear.readyapi.client.teststeps.plugin;

import com.smartbear.readyapi.client.model.PluginTestStep;
import com.smartbear.readyapi.client.teststeps.TestStepBuilder;

public class PluginTestStepBuilder implements TestStepBuilder<PluginTestStep> {
    private final PluginTestStep pluginTestStep = new PluginTestStep();

    public PluginTestStepBuilder(String pluginTestStepType) {
        pluginTestStep.setType(pluginTestStepType);
    }

    public PluginTestStepBuilder withConfigProperty(String propertyName, Object value) {
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
