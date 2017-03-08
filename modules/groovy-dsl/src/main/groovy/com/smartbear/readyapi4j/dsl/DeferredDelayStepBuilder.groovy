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
    private TestRecipeBuilder testRecipeBuilder

    DeferredDelayStepBuilder(BigDecimal time, TestRecipeBuilder testRecipeBuilder) {
        this.time = time
        this.testRecipeBuilder = testRecipeBuilder
    }

    boolean getMilliseconds() {
        testRecipeBuilder.addStep(new DelayTestStepBuilder(time as int))
    }

    boolean getSeconds() {
        testRecipeBuilder.addStep(new DelayTestStepBuilder(time * 1000 as int))
    }

    boolean getSecond() {
        return getSeconds()
    }

    boolean getMinutes() {
        testRecipeBuilder.addStep(new DelayTestStepBuilder(time * 60_000 as int))
    }

    boolean getMinute() {
        return getMinutes()
    }

}
