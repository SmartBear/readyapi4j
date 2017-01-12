package com.smartbear.readyapi4j.dsl.assertions

import com.smartbear.readyapi4j.assertions.AssertionBuilder
import com.smartbear.readyapi4j.assertions.Assertions


class ResponseSlaAssertionDelegate {
    private int time
    private List<AssertionBuilder> assertionBuilders

    ResponseSlaAssertionDelegate(int time, List<AssertionBuilder> assertionBuilders) {
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

    private void addAssertionBuilder(int time) {
        assertionBuilders.add(Assertions.responseSLA(time))
    }
}
