package com.smartbear.readyapi4j.testengine.teststeps.datasource.datagen;

import com.smartbear.readyapi4j.client.model.DataGenerator;
import com.smartbear.readyapi4j.client.model.StringDataGenerator;

public class StringDataGeneratorBuilder extends AbstractDataGeneratorBuilder<StringDataGeneratorBuilder> {
    private final StringDataGenerator stringDataGenerator = new StringDataGenerator();

    StringDataGeneratorBuilder(String property) {
        super(property);
        stringDataGenerator.setType("String");
    }

    public StringDataGeneratorBuilder withMinimumCharacters(int minimumCharacters) {
        if (minimumCharacters < 1) {
            throw new IllegalArgumentException("Minimum characters should be greater than 0, actual: " + minimumCharacters);
        }
        stringDataGenerator.setMinimumCharacters(minimumCharacters);
        return this;
    }

    public StringDataGeneratorBuilder withMaximumCharacters(int maximumCharacters) {
        if (maximumCharacters < 1) {
            throw new IllegalArgumentException("Maximum characters should be greater than 0, actual: " + maximumCharacters);
        }

        if (maximumCharacters < stringDataGenerator.getMinimumCharacters()) {
            throw new IllegalArgumentException("Maximum characters should be greater than minimum number of characters, provided minimum characters: " + stringDataGenerator.getMinimumCharacters());
        }
        stringDataGenerator.setMaximumCharacters(maximumCharacters);
        return this;
    }

    public StringDataGeneratorBuilder withDigits() {
        stringDataGenerator.setUseDigits(true);
        return this;
    }

    public StringDataGeneratorBuilder withoutDigits() {
        stringDataGenerator.setUseDigits(false);
        return this;
    }

    public StringDataGeneratorBuilder withLetters() {
        stringDataGenerator.setUseLetters(true);
        return this;
    }

    public StringDataGeneratorBuilder withoutLetters() {
        stringDataGenerator.setUseLetters(false);
        return this;
    }

    public StringDataGeneratorBuilder withSpaces() {
        stringDataGenerator.setUseSpaces(true);
        return this;
    }

    public StringDataGeneratorBuilder withoutSpaces() {
        stringDataGenerator.setUseSpaces(false);
        return this;
    }

    public StringDataGeneratorBuilder withPunctuationMarks() {
        stringDataGenerator.setUsePunctuationMarks(true);
        return this;
    }

    public StringDataGeneratorBuilder withoutPunctuationMarks() {
        stringDataGenerator.setUsePunctuationMarks(false);
        return this;
    }

    @Override
    protected DataGenerator buildDataGenerator() {
        return stringDataGenerator;
    }
}
