package com.smartbear.readyapi4j;

import com.smartbear.readyapi4j.client.model.PluginTestStep;
import org.junit.Test;

import static com.smartbear.readyapi4j.TestRecipeBuilder.newTestRecipe;
import static com.smartbear.readyapi4j.teststeps.TestSteps.pluginTestStep;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class PluginTestStepBuilderTest {
    @Test
    public void buildsRecipeWithPluginTestStep() throws Exception {
        TestRecipe testRecipe = newTestRecipe(
                pluginTestStep("MQTTPublishTestStep")
                        .named("MQTT Publish Test Step")
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
