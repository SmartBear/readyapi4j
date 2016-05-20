package com.smartbear.readyapi.client.teststeps.datasource.datagen;

import com.smartbear.readyapi.client.model.DataGenerator;
import com.smartbear.readyapi.client.model.IntegerDataGenerator;

public class IntegerDataGeneratorBuilder extends AbstractDataGeneratorBuilder<IntegerDataGeneratorBuilder> {
    private final IntegerDataGenerator integerDataGenerator = new IntegerDataGenerator();

    IntegerDataGeneratorBuilder(String property) {
        super(property);
        integerDataGenerator.setType("Integer");
        integerDataGenerator.setGenrationMode(IntegerDataGenerator.GenrationModeEnum.RANDOM);
        integerDataGenerator.setMinimumValue(1);
        integerDataGenerator.setMaximumValue(100);
    }

    public IntegerDataGeneratorBuilder withMinimumValue(int minimumValue) {
        integerDataGenerator.setMinimumValue(minimumValue);
        return this;
    }

    public IntegerDataGeneratorBuilder withMaximumValue(int maximumValue) {
        integerDataGenerator.setMaximumValue(maximumValue);
        return this;
    }

    IntegerDataGeneratorBuilder withRandomValues() {
        integerDataGenerator.setGenrationMode(IntegerDataGenerator.GenrationModeEnum.RANDOM);
        return this;
    }

    IntegerDataGeneratorBuilder withSequentialValues() {
        integerDataGenerator.setGenrationMode(IntegerDataGenerator.GenrationModeEnum.SEQUENTIAL);
        return this;
    }

    @Override
    protected DataGenerator buildDataGenerator() {
        return integerDataGenerator;
    }
}
