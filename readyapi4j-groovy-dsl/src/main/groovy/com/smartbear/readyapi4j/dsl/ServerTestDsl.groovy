package com.smartbear.readyapi4j.dsl

import com.smartbear.readyapi4j.TestRecipe
import com.smartbear.readyapi4j.dsl.pro.ProDslDelegate
import com.smartbear.readyapi4j.teststeps.TestStepBuilder

import static com.smartbear.readyapi4j.TestRecipeBuilder.newTestRecipe

class ServerTestDsl {
    static TestRecipe recipe(@DelegatesTo(ProDslDelegate) Closure definition) {
        ProDslDelegate delegate = new ProDslDelegate()
        definition.delegate = delegate
        definition.call()
        return newTestRecipe(delegate.testStepBuilders.toArray(new TestStepBuilder[delegate.testStepBuilders.size()])).buildTestRecipe()
    }
}
