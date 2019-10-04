package io.swagger.assert4j.dsl

import io.swagger.assert4j.TestRecipe
import io.swagger.assert4j.client.model.DelayTestStep
import io.swagger.assert4j.client.model.TestStep
import org.junit.Test

import static DataExtractor.extractFirstTestStep
import static io.swagger.assert4j.dsl.TestDsl.recipe

class DelayTestStepDslTest {

    @Test
    void buildsDelayWithMilliseconds() throws Exception {
        TestRecipe recipe = recipe {
            pause 1200 milliseconds
        }

        verifyDelay(recipe, 1200)
    }

    @Test
    void buildsDelayWithSeconds() throws Exception {
        TestRecipe recipe = recipe {
            pause 2.5 seconds
        }

        verifyDelay(recipe, 2500)
    }

    @Test
    void buildsDelayWithOneSecond() throws Exception {
        TestRecipe recipe = recipe {
            pause 1 second
        }

        verifyDelay(recipe, 1000)
    }

    @Test
    void buildsDelayWithMinutes() throws Exception {
        TestRecipe recipe = recipe {
            pause 1.5 minutes
        }

        verifyDelay(recipe, 90_000)
    }

    @Test
    void buildsDelayWithOneMinute() throws Exception {
        TestRecipe recipe = recipe {
            pause 1 minute
        }

        verifyDelay(recipe, 60_000)
    }

    private static void verifyDelay(TestRecipe recipe, int delay) {
        TestStep singleStep = extractFirstTestStep(recipe) as DelayTestStep
        assert singleStep.delay == delay
    }

}
