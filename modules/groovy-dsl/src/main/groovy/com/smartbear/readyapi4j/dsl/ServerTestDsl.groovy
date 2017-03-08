package com.smartbear.readyapi4j.dsl

import com.smartbear.readyapi4j.TestRecipe
import com.smartbear.readyapi4j.TestRecipeBuilder
import com.smartbear.readyapi4j.dsl.pro.ProDslDelegate

class ServerTestDsl {
    static TestRecipe recipe(@DelegatesTo(ProDslDelegate) Closure definition) {
        TestRecipeBuilder testRecipeBuilder = TestRecipeBuilder.newTestRecipe()
        ProDslDelegate delegate = new ProDslDelegate(testRecipeBuilder)
        definition.delegate = delegate
        definition.call()
        return testRecipeBuilder.buildTestRecipe()
    }
}
