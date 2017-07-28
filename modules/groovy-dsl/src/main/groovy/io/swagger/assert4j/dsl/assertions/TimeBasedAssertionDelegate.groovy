package io.swagger.assert4j.dsl.assertions

import io.swagger.assert4j.assertions.AssertionBuilder

class TimeBasedAssertionDelegate {
    private Closure assertionBuilder;
    private BigDecimal time;
    private List<AssertionBuilder> assertionBuilders;

    TimeBasedAssertionDelegate(Closure assertionBuilder, BigDecimal time, List<AssertionBuilder> assertionBuilders) {
        this.assertionBuilder = assertionBuilder
        this.time = time
        this.assertionBuilders = assertionBuilders
    }

    int getMilliseconds() {
        addAssertionBuilder time
        return time
    }

    int getMs() {
        addAssertionBuilder time
        return time
    }

    int getSeconds() {
        addAssertionBuilder time * 1000
        return time * 1000
    }

    int getSecond() {
        getSeconds()
    }

    int getMinutes() {
        addAssertionBuilder time * 60_000
        time * 60_000
    }

    int getMinute() {
        getMinutes()
    }

    private void addAssertionBuilder(BigDecimal time) {
        assertionBuilders.add((AssertionBuilder) assertionBuilder.call(time))
    }
}
