package io.swagger.assert4j.dsl

import io.swagger.assert4j.client.model.GroovyScriptTestStep
import io.swagger.assert4j.TestRecipe
import org.junit.Test

import static io.swagger.assert4j.dsl.TestDsl.recipe
import static DataExtractor.extractFirstTestStep

class ScriptTestStepDslTest {

    private static final String SCRIPT_TEXT = "println 'Peekaboo, little Earth'"
    private static final String TEST_STEP_NAME = 'GroovyTestStep'

    @Test
    void buildsSimpleRecipe() throws Exception {
        TestRecipe recipe = recipe {
            groovyScriptStep SCRIPT_TEXT
        }

        GroovyScriptTestStep singleStep = extractFirstTestStep(recipe) as GroovyScriptTestStep
        assert singleStep.script == SCRIPT_TEXT
    }

    @Test
    void buildsGroovyStepWithName() throws Exception {
        TestRecipe recipe = recipe {
            groovyScriptStep SCRIPT_TEXT, TEST_STEP_NAME
        }

        GroovyScriptTestStep singleStep = extractFirstTestStep(recipe) as GroovyScriptTestStep
        assert singleStep.name == TEST_STEP_NAME
        assert singleStep.script == SCRIPT_TEXT
    }

}
