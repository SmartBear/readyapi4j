package com.smartbear.readyapi4j.dsl

import com.smartbear.readyapi4j.TestRecipeBuilder
import com.smartbear.readyapi4j.teststeps.delay.DelayTestStepBuilder

/**
 * This class builds a delay test step in two passes, so that a more natural-language like syntax can be used in the DSL.
 *
 * The getters aren't really getters; it's just a trick to make the DSL for delay steps free from parentheses.
 */
class DeferredDelayStepBuilder {
    private BigDecimal time
    private TestRecipeBuilder recipeBuilder

    DeferredDelayStepBuilder(BigDecimal time, TestRecipeBuilder recipeBuilder) {
        this.time = time
        this.recipeBuilder = recipeBuilder
    }

    TestRecipeBuilder getMilliseconds() {
        recipeBuilder.addStep(new DelayTestStepBuilder(time as int))
    }

    TestRecipeBuilder getSeconds() {
        recipeBuilder.addStep(new DelayTestStepBuilder(time * 1000 as int))
    }

    TestRecipeBuilder getSecond() {
        return getSeconds()
    }

    TestRecipeBuilder getMinutes() {
        recipeBuilder.addStep(new DelayTestStepBuilder(time * 60_000 as int))
    }

    TestRecipeBuilder getMinute() {
        return getMinutes()
    }

}
