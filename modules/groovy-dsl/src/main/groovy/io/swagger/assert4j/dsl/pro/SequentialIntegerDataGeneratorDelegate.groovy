package io.swagger.assert4j.dsl.pro

import io.swagger.assert4j.testserver.teststeps.datasource.datagen.IntegerDataGeneratorBuilder

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
