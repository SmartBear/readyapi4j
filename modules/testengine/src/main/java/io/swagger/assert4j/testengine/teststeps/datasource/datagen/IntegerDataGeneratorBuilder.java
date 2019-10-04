package io.swagger.assert4j.testengine.teststeps.datasource.datagen;

import io.swagger.assert4j.client.model.DataGenerator;
import io.swagger.assert4j.client.model.IntegerDataGenerator;

public class IntegerDataGeneratorBuilder extends AbstractDataGeneratorBuilder<IntegerDataGeneratorBuilder> {
    private final IntegerDataGenerator integerDataGenerator = new IntegerDataGenerator();

    IntegerDataGeneratorBuilder(String property) {
        super(property);
        integerDataGenerator.setType("Integer");
        integerDataGenerator.setGenerationMode(IntegerDataGenerator.GenerationModeEnum.RANDOM);
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

    IntegerDataGeneratorBuilder withSequentialValues() {
        integerDataGenerator.setGenerationMode(IntegerDataGenerator.GenerationModeEnum.SEQUENTIAL);
        return this;
    }

    /**
     * Increment value for generation of sequential values when generationMode is Sequential. Ignored if generationMode is Random.
     *
     * @param incrementBy increment value
     * @return IntegerDataGeneratorBuilder
     */
    public IntegerDataGeneratorBuilder incrementBy(int incrementBy) {
        integerDataGenerator.setIncrementBy(incrementBy);
        return this;
    }

    @Override
    protected DataGenerator buildDataGenerator() {
        return integerDataGenerator;
    }
}
