package com.smartbear.readyapi4j.testengine.teststeps.datasource.datagen;


import com.smartbear.readyapi4j.client.model.DataGenerator;

public class BasicDataGeneratorBuilder extends AbstractDataGeneratorBuilder<BasicDataGeneratorBuilder> {
    private String type;

    BasicDataGeneratorBuilder(String type, String property) {
        super(property);
        this.type = type;
    }

    @Override
    protected DataGenerator buildDataGenerator() {
        DataGenerator dataGenerator = new DataGenerator();
        dataGenerator.setType(type);
        return dataGenerator;
    }
}
