package com.smartbear.readyapi4j.testserver.teststeps.datasource.datagen;

import com.smartbear.readyapi.client.model.DataGenerator;

public abstract class AbstractDataGeneratorBuilder<BuilderType> {
    private final String property;
    private int duplicationFactor = 1; //default value

    AbstractDataGeneratorBuilder(String property) {
        this.property = property;
    }

    public BuilderType duplicatedBy(int duplicatedBy) {
        if (duplicatedBy <= 0) {
            throw new IllegalArgumentException("Duplicated by should be greater than 0, actual : " + duplicatedBy);
        }
        this.duplicationFactor = duplicatedBy;
        return (BuilderType) this;
    }

    public DataGenerator build() {
        DataGenerator dataGenerator = buildDataGenerator();
        dataGenerator.setPropertyName(property);
        dataGenerator.setDuplicationFactor(duplicationFactor);
        return dataGenerator;
    }

    protected abstract DataGenerator buildDataGenerator();
}
