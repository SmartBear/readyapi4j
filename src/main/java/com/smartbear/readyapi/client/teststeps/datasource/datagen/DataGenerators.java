package com.smartbear.readyapi.client.teststeps.datasource.datagen;

public class DataGenerators {
    public static BooleanDataGeneratorBuilder booleanDataGenerator(String propertyName) {
        return new BooleanDataGeneratorBuilder(propertyName);
    }
}
