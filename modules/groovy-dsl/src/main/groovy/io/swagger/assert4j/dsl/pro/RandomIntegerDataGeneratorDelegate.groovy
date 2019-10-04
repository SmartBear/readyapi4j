package io.swagger.assert4j.dsl.pro

import io.swagger.assert4j.testengine.teststeps.datasource.datagen.IntegerDataGeneratorBuilder

/**
 * Delegate for the 'randomInteger' closure in 'recipe'
 */
class RandomIntegerDataGeneratorDelegate {
    protected IntegerDataGeneratorBuilder integerDataGeneratorBuilder

    RandomIntegerDataGeneratorDelegate(IntegerDataGeneratorBuilder integerDataGeneratorBuilder) {
        this.integerDataGeneratorBuilder = integerDataGeneratorBuilder
    }

    void minimumValue(int minimumValue) {
        integerDataGeneratorBuilder.withMinimumValue(minimumValue)
    }

    void maximumValue(int maximumValue) {
        integerDataGeneratorBuilder.withMaximumValue(maximumValue)
    }
}
