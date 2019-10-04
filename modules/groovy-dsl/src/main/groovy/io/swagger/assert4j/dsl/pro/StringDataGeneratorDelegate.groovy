package io.swagger.assert4j.dsl.pro

import io.swagger.assert4j.testengine.teststeps.datasource.datagen.StringDataGeneratorBuilder

/**
 * Delegate for the 'stringValue' closure in 'recipe'
 */
class StringDataGeneratorDelegate {
    private StringDataGeneratorBuilder dataGeneratorBuilder;

    StringDataGeneratorDelegate(StringDataGeneratorBuilder dataGeneratorBuilder) {
        this.dataGeneratorBuilder = dataGeneratorBuilder
    }

    void minimumCharacters(int minimumCharacters) {
        dataGeneratorBuilder.withMinimumCharacters(minimumCharacters)
    }

    void maximumCharacters(int maximumCharacters) {
        dataGeneratorBuilder.withMaximumCharacters(maximumCharacters)
    }

    /**
     * Not a real getter, just a hack to be able to use it in closure without parentheses.
     * By default letters will be part of generated string, depending on the length of the generated string
     */
    StringDataGeneratorBuilder getWithoutLetters() {
        dataGeneratorBuilder.withoutLetters()
    }

    /**
     * Not a real getter, just a hack to be able to use it in closure without parentheses.
     * By default digits will be part of generated string,, depending on the length of the generated string
     */
    StringDataGeneratorBuilder getWithoutDigits() {
        dataGeneratorBuilder.withoutDigits()
    }

    /**
     * Not a real getter, just a hack to be able to use it in closure without parentheses.
     * By default spaces will be part of generated string, depending on the length of the generated string
     */
    StringDataGeneratorBuilder getWithoutSpaces() {
        dataGeneratorBuilder.withoutSpaces()
    }

    /**
     * Not a real getter, just a hack to be able to use it in closure without parentheses.
     * By default punctuation marks will be part of generated string, depending on the length of the generated string
     */
    StringDataGeneratorBuilder getWithoutPunctuationMarks() {
        dataGeneratorBuilder.withoutPunctuationMarks()
    }
}
