package com.smartbear.readyapi.client.teststeps.datasource.datagen;

import com.smartbear.readyapi.client.model.DataGenerator;
import com.smartbear.readyapi.client.model.RealNumberDataGenerator;

import java.math.BigDecimal;

public class RealNumberDataGeneratorBuilder extends AbstractDataGeneratorBuilder<RealNumberDataGeneratorBuilder> {
    private final RealNumberDataGenerator realNumberDataGenerator = new RealNumberDataGenerator();

    RealNumberDataGeneratorBuilder(String property) {
        super(property);
        realNumberDataGenerator.setType("Real");
        realNumberDataGenerator.setMinimumValue(new BigDecimal(1));
        realNumberDataGenerator.setMaximumValue(new BigDecimal(100));
        realNumberDataGenerator.setDecimalPlaces(2);
    }

    public RealNumberDataGeneratorBuilder withMinimumValue(BigDecimal minimumValue) {
        realNumberDataGenerator.setMinimumValue(minimumValue);
        return this;
    }

    public RealNumberDataGeneratorBuilder withMaximumValue(BigDecimal maximumValue) {
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

    RealNumberDataGeneratorBuilder withRandomValues() {
        realNumberDataGenerator.setGenerationMode(RealNumberDataGenerator.GenerationModeEnum.RANDOM);
        return this;
    }

    RealNumberDataGeneratorBuilder withSequentialValues() {
        realNumberDataGenerator.setGenerationMode(RealNumberDataGenerator.GenerationModeEnum.SEQUENTIAL);
        return this;
    }

    @Override
    protected DataGenerator buildDataGenerator() {
        return realNumberDataGenerator;
    }
}
