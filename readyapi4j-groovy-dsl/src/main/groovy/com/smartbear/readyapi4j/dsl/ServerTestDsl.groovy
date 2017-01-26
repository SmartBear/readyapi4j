package com.smartbear.readyapi4j.dsl

import com.smartbear.readyapi4j.TestRecipe
import com.smartbear.readyapi4j.dsl.pro.ProDslDelegate


class ServerTestDsl {
    static TestRecipe recipe(@DelegatesTo(ProDslDelegate) Closure definition) {
        ProDslDelegate delegate = new ProDslDelegate()
        definition.delegate = delegate
        definition.call()
        return delegate.buildRecipe()
    }
}
