package com.smartbear.readyapi4j.dsl

import com.smartbear.readyapi.client.model.GroovyScriptTestStep
import com.smartbear.readyapi4j.TestRecipe
import org.junit.Test

import static TestDsl.recipe
import static com.smartbear.readyapi4j.dsl.DataExtractor.extractFirstTestStep
import static org.hamcrest.CoreMatchers.is
import static org.junit.Assert.assertThat

class ScriptTestStepDslTest {

    private static final String SCRIPT_TEXT = "println 'Peekaboo, little Earth'"

    @Test
    void buildsSimpleRecipe() throws Exception {
        TestRecipe recipe = recipe {
            groovyScriptStep SCRIPT_TEXT
        }

        GroovyScriptTestStep singleStep = extractFirstTestStep(recipe) as GroovyScriptTestStep
        assertThat(singleStep.script, is(SCRIPT_TEXT))
    }

}
