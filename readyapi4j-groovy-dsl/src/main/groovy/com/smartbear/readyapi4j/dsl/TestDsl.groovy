package com.smartbear.readyapi4j.dsl

import com.smartbear.readyapi4j.TestRecipe

/**
 * TestDsl for the test steps supported in both local and remote execution.
 * See <code>ServerTestDsl</code> for test steps supported only by TestServer and not by local execution
 */
class TestDsl {

    static TestRecipe recipe(@DelegatesTo(DslDelegate) Closure definition) {
        DslDelegate delegate = new DslDelegate()
        definition.delegate = delegate
        definition.call()
        return delegate.buildRecipe()
    }

}