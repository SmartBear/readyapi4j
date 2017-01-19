package com.smartbear.readyapi4j.dsl

import com.smartbear.readyapi.client.model.PropertiesTestStep
import com.smartbear.readyapi4j.TestRecipe
import org.junit.Test

import static com.smartbear.readyapi4j.dsl.TestDsl.recipe


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
