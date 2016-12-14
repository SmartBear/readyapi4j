package com.smartbear.readyapi4j.teststeps.datasource.datagen;

import com.smartbear.readyapi.client.model.CustomStringDataGenerator;
import com.smartbear.readyapi.client.model.DataGenerator;

public class CustomStringDataGeneratorBuilder extends AbstractDataGeneratorBuilder<CustomStringDataGeneratorBuilder> {
    private final CustomStringDataGenerator customStringDataGenerator = new CustomStringDataGenerator();

    CustomStringDataGeneratorBuilder(String property) {
        super(property);
        customStringDataGenerator.setType("Custom String");
    }

    public CustomStringDataGeneratorBuilder withValue(String value) {
        customStringDataGenerator.setValue(value);
        return this;
    }

    @Override
    protected DataGenerator buildDataGenerator() {
        return customStringDataGenerator;
    }
}
