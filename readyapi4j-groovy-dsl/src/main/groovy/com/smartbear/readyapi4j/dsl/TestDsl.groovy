package com.smartbear.readyapi4j.dsl

import com.smartbear.readyapi4j.TestRecipe
import com.smartbear.readyapi4j.TestRecipeBuilder

/**
 * TestDsl for the test steps supported in both local and remote execution.
 * See <code>ServerTestDsl</code> for test steps supported only by TestServer and not by local execution
 */
class TestDsl {

    static TestRecipe recipe(@DelegatesTo(DslDelegate) Closure definition) {
        TestRecipeBuilder testRecipeBuilder = TestRecipeBuilder.newTestRecipe()
        DslDelegate delegate = new DslDelegate(testRecipeBuilder)
        definition.delegate = delegate
        definition.call()
        return testRecipeBuilder.buildTestRecipe()
    }

}