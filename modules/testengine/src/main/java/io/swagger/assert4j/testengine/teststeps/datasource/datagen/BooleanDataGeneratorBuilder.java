package io.swagger.assert4j.testengine.teststeps.datasource.datagen;

import io.swagger.assert4j.client.model.BooleanDataGenerator;
import io.swagger.assert4j.client.model.DataGenerator;

public class BooleanDataGeneratorBuilder extends AbstractDataGeneratorBuilder<BooleanDataGeneratorBuilder> {

    private final BooleanDataGenerator booleanDataGenerator = new BooleanDataGenerator();

    BooleanDataGeneratorBuilder(String property) {
        super(property);
        booleanDataGenerator.setType("Boolean");
        booleanDataGenerator.setFormat(BooleanDataGenerator.FormatEnum.TRUE_FALSE);
    }

    BooleanDataGeneratorBuilder withYesNoFormat() {
        booleanDataGenerator.setFormat(BooleanDataGenerator.FormatEnum.YES_NO);
        return this;
    }

    BooleanDataGeneratorBuilder withDigitsFormat() {
        booleanDataGenerator.setFormat(BooleanDataGenerator.FormatEnum._1_0);
        return this;
    }

    @Override
    protected DataGenerator buildDataGenerator() {
        return booleanDataGenerator;
    }
}
