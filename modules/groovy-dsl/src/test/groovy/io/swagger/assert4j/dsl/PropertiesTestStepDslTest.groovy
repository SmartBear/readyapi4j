package io.swagger.assert4j.dsl

import io.swagger.assert4j.TestRecipe
import io.swagger.assert4j.client.model.PropertiesTestStep
import org.junit.Test

import static io.swagger.assert4j.dsl.TestDsl.recipe

class PropertiesTestStepDslTest {

    private static final Map<String, String> PROPERTIES_MAP = [property1: 'value1', property2: 'value2']
    private static final String TEST_STEP_NAME = 'PropertiesTestStep'

    @Test
    void buildRecipeWithPropertiesTestStep() throws Exception {
        TestRecipe testRecipe = recipe {
            properties PROPERTIES_MAP, TEST_STEP_NAME
        }
        PropertiesTestStep propertiesTestStep = testRecipe.testCase.testSteps[0] as PropertiesTestStep
        assert propertiesTestStep.name == TEST_STEP_NAME
        assert propertiesTestStep.properties == PROPERTIES_MAP
    }
}
