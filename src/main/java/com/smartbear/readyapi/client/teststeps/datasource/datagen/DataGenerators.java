package com.smartbear.readyapi.client.teststeps.datasource.datagen;

public class DataGenerators {
    public static BooleanDataGeneratorBuilder booleanTypeProperty(String propertyName) {
        return new BooleanDataGeneratorBuilder(propertyName);
    }

    public static BasicDataGeneratorBuilder cityTypeProperty(String propertyName) {
        return new BasicDataGeneratorBuilder("City", propertyName);
    }

    public static BasicDataGeneratorBuilder countryTypeProperty(String propertyName) {
        return new BasicDataGeneratorBuilder("Country", propertyName);
    }

    public static BasicDataGeneratorBuilder addressTypeProperty(String propertyName) {
        return new BasicDataGeneratorBuilder("Street Address", propertyName);
    }

    public static BasicDataGeneratorBuilder emailTypeProperty(String propertyName) {
        return new BasicDataGeneratorBuilder("E-Mail", propertyName);
    }

    public static BasicDataGeneratorBuilder guidTypeProperty(String propertyName) {
        return new BasicDataGeneratorBuilder("Guid", propertyName);
    }

    public static BasicDataGeneratorBuilder ssnTypeProperty(String propertyName) {
        return new BasicDataGeneratorBuilder("Social Security Number", propertyName);
    }

    public static ComputerAddressDataGeneratorBuilder computerAddressTypeProperty(String propertyName) {
        return new ComputerAddressDataGeneratorBuilder(propertyName);
    }

    public static CustomStringDataGeneratorBuilder customStringTypeProperty(String propertyName) {
        return new CustomStringDataGeneratorBuilder(propertyName);
    }

    public static StringDataGeneratorBuilder stringTypeProperty(String propertyName) {
        return new StringDataGeneratorBuilder(propertyName);
    }

    public static StateNameDataGeneratorBuilder stateNameTypeProperty(String propertyName) {
        return new StateNameDataGeneratorBuilder(propertyName);
    }

    public static NameDataGeneratorBuilder nameTypeProperty(String propertyName) {
        return new NameDataGeneratorBuilder(propertyName);
    }
}
