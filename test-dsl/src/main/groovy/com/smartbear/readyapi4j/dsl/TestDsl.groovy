package com.smartbear.readyapi4j.dsl

import com.smartbear.readyapi4j.TestRecipe

class TestDsl {

    static TestRecipe recipe(@DelegatesTo(DslDelegate) Closure definition) {
        DslDelegate delegate = new DslDelegate()
        definition.delegate = delegate
        definition.call()
        return delegate.buildRecipe()
    }

}