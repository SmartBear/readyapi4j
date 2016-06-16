package com.smartbear.readyapi.client;

import com.smartbear.readyapi.client.model.PluginTestStep;
import org.junit.Test;

import static com.smartbear.readyapi.client.TestRecipeBuilder.newTestRecipe;
import static com.smartbear.readyapi.client.teststeps.TestSteps.pluginTestStep;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class PluginTestStepBuilderTest {
    @Test
    public void buildsRecipeWithPluginTestStep() throws Exception {
        TestRecipe testRecipe = newTestRecipe()
                .addStep(pluginTestStep("MQTTPublishTestStep")
                        .withConfigProperty("config", new TestStepConfig())
                        .andWithConfigProperty("ClientID", "Client1")
                )
                .buildTestRecipe();

        PluginTestStep pluginTestStep = (PluginTestStep) testRecipe.getTestCase().getTestSteps().get(0);
        assertThat(pluginTestStep.getType(), is("MQTTPublishTestStep"));
        assertThat(pluginTestStep.getConfiguration().size(), is(2));
        assertThat(pluginTestStep.getConfiguration().get("config"), is(instanceOf(TestStepConfig.class)));
        assertThat((String) pluginTestStep.getConfiguration().get("ClientID"), is("Client1"));
    }

    private class TestStepConfig {
        public String value1 = "bla bla";
        public Integer intValue = 32;
    }
}
