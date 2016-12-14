package com.smartbear.readyapi4j.teststeps.plugin;

import com.smartbear.readyapi.client.model.PluginTestStep;
import com.smartbear.readyapi4j.teststeps.TestStepBuilder;

public class PluginTestStepBuilder implements TestStepBuilder<PluginTestStep> {
    private final PluginTestStep pluginTestStep = new PluginTestStep();

    public PluginTestStepBuilder(String pluginTestStepType) {
        pluginTestStep.setType(pluginTestStepType);
    }

    public PluginTestStepBuilder named(String testStepName) {
        pluginTestStep.setName(testStepName);
        return this;
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
