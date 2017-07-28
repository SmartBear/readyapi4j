package io.swagger.assert4j.dsl

import io.swagger.assert4j.TestRecipe
import io.swagger.assert4j.TestRecipeBuilder
import io.swagger.assert4j.dsl.pro.ProDslDelegate

class ServerTestDsl {
    static TestRecipe recipe(@DelegatesTo(ProDslDelegate) Closure definition) {
        TestRecipeBuilder testRecipeBuilder = TestRecipeBuilder.newTestRecipe()
        ProDslDelegate delegate = new ProDslDelegate(testRecipeBuilder)
        definition.delegate = delegate
        definition.call()
        return testRecipeBuilder.buildTestRecipe()
    }
}
