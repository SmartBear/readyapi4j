package com.smartbear.readyapi4j.dsl.pro

import com.smartbear.readyapi4j.testserver.teststeps.datasource.datagen.IntegerDataGeneratorBuilder

/**
 * Delegate for the 'sequentialInteger' closure in 'recipe'
 */
class SequentialIntegerDataGeneratorDelegate extends RandomIntegerDataGeneratorDelegate {

    SequentialIntegerDataGeneratorDelegate(IntegerDataGeneratorBuilder integerDataGeneratorBuilder) {
        super(integerDataGeneratorBuilder)
    }

    void incrementBy(int increment) {
        integerDataGeneratorBuilder.incrementBy(increment)
    }
}
