package io.swagger.assert4j.dsl

import io.swagger.assert4j.client.model.PluginTestStep
import io.swagger.assert4j.TestRecipe
import org.junit.Test

import static io.swagger.assert4j.dsl.TestDsl.recipe


class PluginTestStepDslTest {

    private static final String TEST_STEP_TYPE = 'MQTTReceiveTestStep'
    private static final String TEST_STEP_NAME = 'My plugin test step'

    @Test
    void createsRecipeWithPluginProvidedTestStep() throws Exception {
        TestRecipe testRecipe = recipe {
            pluginProvidedStep {
                type TEST_STEP_TYPE
                name TEST_STEP_NAME
                configuration property1: 'value1', property2: [nestedProperty1: 'nestedValue1']
            }
        }
        PluginTestStep pluginTestStep = testRecipe.testCase.testSteps[0] as PluginTestStep
        assert pluginTestStep.type == TEST_STEP_TYPE
        assert pluginTestStep.name == TEST_STEP_NAME
        assert pluginTestStep.configuration['property1'] == 'value1'
        assert pluginTestStep.configuration['property2'] == [nestedProperty1: 'nestedValue1']

    }
}
