package com.smartbear.readyapi4j.dsl

import com.smartbear.readyapi4j.teststeps.TestStepBuilder
import com.smartbear.readyapi4j.teststeps.delay.DelayTestStepBuilder

/**
 * This class builds a delay test step in two passes, so that a more natural-language like syntax can be used in the DSL.
 *
 * The getters aren't really getters; it's just a trick to make the DSL for delay steps free from parentheses.
 */
class DeferredDelayStepBuilder {
    private BigDecimal time
    List<TestStepBuilder> testStepBuilders

    DeferredDelayStepBuilder(BigDecimal time, List<TestStepBuilder> testStepBuilders) {
        this.time = time
        this.testStepBuilders = testStepBuilders
    }

    boolean getMilliseconds() {
        testStepBuilders.add(new DelayTestStepBuilder(time as int))
    }

    boolean getSeconds() {
        testStepBuilders.add(new DelayTestStepBuilder(time * 1000 as int))
    }

    boolean getSecond() {
        return getSeconds()
    }

    boolean getMinutes() {
        testStepBuilders.add(new DelayTestStepBuilder(time * 60_000 as int))
    }

    boolean getMinute() {
        return getMinutes()
    }

}
