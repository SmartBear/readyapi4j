package com.smartbear.readyapi4j.testengine.teststeps.datasource.datagen;

import com.smartbear.readyapi4j.client.model.DataGenerator;
import com.smartbear.readyapi4j.client.model.RealNumberDataGenerator;

public class RealNumberDataGeneratorBuilder extends AbstractDataGeneratorBuilder<RealNumberDataGeneratorBuilder> {
    private final RealNumberDataGenerator realNumberDataGenerator = new RealNumberDataGenerator();

    RealNumberDataGeneratorBuilder(String property) {
        super(property);
        realNumberDataGenerator.setType("Real");
        realNumberDataGenerator.setMinimumValue(Float.valueOf(1));
        realNumberDataGenerator.setMaximumValue(Float.valueOf(100));
        realNumberDataGenerator.setDecimalPlaces(2);
    }

    public RealNumberDataGeneratorBuilder withMinimumValue(Float minimumValue) {
        realNumberDataGenerator.setMinimumValue(minimumValue);
        return this;
    }

    public RealNumberDataGeneratorBuilder withMaximumValue(Float maximumValue) {
        realNumberDataGenerator.setMaximumValue(maximumValue);
        return this;
    }

    public RealNumberDataGeneratorBuilder withDecimalPlaces(int decimalPlaces) {
        if (decimalPlaces < 0) {
            throw new IllegalArgumentException("Decimal places value should be a positive value, actual: " + decimalPlaces);
        }
        realNumberDataGenerator.setDecimalPlaces(decimalPlaces);
        return this;
    }

    RealNumberDataGeneratorBuilder withSequentialValues() {
        realNumberDataGenerator.setGenerationMode(RealNumberDataGenerator.GenerationModeEnum.SEQUENTIAL);
        return this;
    }

    /**
     * Increment value for generation of sequential values when generationMode is Sequential. Ignored if generationMode is Random.
     *
     * @param incrementBy increment value
     * @return RealNumberDataGeneratorBuilder
     */
    public RealNumberDataGeneratorBuilder incrementBy(float incrementBy) {
        realNumberDataGenerator.setIncrementBy(incrementBy);
        return this;
    }

    @Override
    protected DataGenerator buildDataGenerator() {
        return realNumberDataGenerator;
    }
}
