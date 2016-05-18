package com.smartbear.readyapi.client.teststeps.datasource.datagen;

public class DataGenerators {
    public static BooleanDataGeneratorBuilder booleanTypeProperty(String propertyName) {
        return new BooleanDataGeneratorBuilder(propertyName);
    }
}
