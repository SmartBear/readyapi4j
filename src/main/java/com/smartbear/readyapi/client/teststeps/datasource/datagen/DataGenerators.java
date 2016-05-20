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

    public static StateNameDataGeneratorBuilder fullStateNameTypeProperty(String propertyName) {
        StateNameDataGeneratorBuilder stateNameDataGeneratorBuilder = new StateNameDataGeneratorBuilder(propertyName);
        stateNameDataGeneratorBuilder.withFullNames();
        return stateNameDataGeneratorBuilder;
    }

    public static StateNameDataGeneratorBuilder shortStateNameTypeProperty(String propertyName) {
        StateNameDataGeneratorBuilder stateNameDataGeneratorBuilder = new StateNameDataGeneratorBuilder(propertyName);
        stateNameDataGeneratorBuilder.withAbbreviatedNames();
        return stateNameDataGeneratorBuilder;
    }

    public static NameDataGeneratorBuilder anyGenderFullNameTypeProperty(String propertyName) {
        NameDataGeneratorBuilder nameDataGeneratorBuilder = new NameDataGeneratorBuilder(propertyName);
        nameDataGeneratorBuilder.withGenderAny();
        nameDataGeneratorBuilder.withFullNames();
        return nameDataGeneratorBuilder;
    }

    public static NameDataGeneratorBuilder anyGenderFirstNameTypeProperty(String propertyName) {
        NameDataGeneratorBuilder nameDataGeneratorBuilder = new NameDataGeneratorBuilder(propertyName);
        nameDataGeneratorBuilder.withGenderAny();
        nameDataGeneratorBuilder.withFirstNames();
        return nameDataGeneratorBuilder;
    }

    public static NameDataGeneratorBuilder anyGenderLastNameTypeProperty(String propertyName) {
        NameDataGeneratorBuilder nameDataGeneratorBuilder = new NameDataGeneratorBuilder(propertyName);
        nameDataGeneratorBuilder.withGenderAny();
        nameDataGeneratorBuilder.withLatsNames();
        return nameDataGeneratorBuilder;
    }

    public static NameDataGeneratorBuilder maleFullNameTypeProperty(String propertyName) {
        NameDataGeneratorBuilder nameDataGeneratorBuilder = new NameDataGeneratorBuilder(propertyName);
        nameDataGeneratorBuilder.withGenderMale();
        nameDataGeneratorBuilder.withFullNames();
        return nameDataGeneratorBuilder;
    }

    public static NameDataGeneratorBuilder maleFirstNameTypeProperty(String propertyName) {
        NameDataGeneratorBuilder nameDataGeneratorBuilder = new NameDataGeneratorBuilder(propertyName);
        nameDataGeneratorBuilder.withGenderMale();
        nameDataGeneratorBuilder.withFirstNames();
        return nameDataGeneratorBuilder;
    }

    public static NameDataGeneratorBuilder maleLastNameTypeProperty(String propertyName) {
        NameDataGeneratorBuilder nameDataGeneratorBuilder = new NameDataGeneratorBuilder(propertyName);
        nameDataGeneratorBuilder.withGenderMale();
        nameDataGeneratorBuilder.withLatsNames();
        return nameDataGeneratorBuilder;
    }

    public static NameDataGeneratorBuilder femaleFullNameTypeProperty(String propertyName) {
        NameDataGeneratorBuilder nameDataGeneratorBuilder = new NameDataGeneratorBuilder(propertyName);
        nameDataGeneratorBuilder.withGenderFemale();
        nameDataGeneratorBuilder.withFullNames();
        return nameDataGeneratorBuilder;
    }

    public static NameDataGeneratorBuilder femaleFirstNameTypeProperty(String propertyName) {
        NameDataGeneratorBuilder nameDataGeneratorBuilder = new NameDataGeneratorBuilder(propertyName);
        nameDataGeneratorBuilder.withGenderFemale();
        nameDataGeneratorBuilder.withFirstNames();
        return nameDataGeneratorBuilder;
    }

    public static NameDataGeneratorBuilder femaleLastNameTypeProperty(String propertyName) {
        NameDataGeneratorBuilder nameDataGeneratorBuilder = new NameDataGeneratorBuilder(propertyName);
        nameDataGeneratorBuilder.withGenderFemale();
        nameDataGeneratorBuilder.withLatsNames();
        return nameDataGeneratorBuilder;
    }

    public static IntegerDataGeneratorBuilder randomIntegerTypeProperty(String propertyName) {
        IntegerDataGeneratorBuilder integerDataGeneratorBuilder = new IntegerDataGeneratorBuilder(propertyName);
        integerDataGeneratorBuilder.withRandomValues();
        return integerDataGeneratorBuilder;
    }


    public static IntegerDataGeneratorBuilder sequentialIntegerTypeProperty(String propertyName) {
        IntegerDataGeneratorBuilder integerDataGeneratorBuilder = new IntegerDataGeneratorBuilder(propertyName);
        integerDataGeneratorBuilder.withSequentialValues();
        return integerDataGeneratorBuilder;
    }

    public static RealNumberDataGeneratorBuilder randomRealNumberTypeProperty(String propertyName) {
        RealNumberDataGeneratorBuilder realNumberDataGeneratorBuilder = new RealNumberDataGeneratorBuilder(propertyName);
        realNumberDataGeneratorBuilder.withRandomValues();
        return realNumberDataGeneratorBuilder;
    }

    public static RealNumberDataGeneratorBuilder sequentialRealNumberTypeProperty(String propertyName) {
        RealNumberDataGeneratorBuilder realNumberDataGeneratorBuilder = new RealNumberDataGeneratorBuilder(propertyName);
        realNumberDataGeneratorBuilder.withSequentialValues();
        return realNumberDataGeneratorBuilder;
    }

    public static ValuesFromSetDataGeneratorBuilder randomValueFromSetTypeProperty(String propertyName) {
        ValuesFromSetDataGeneratorBuilder dataGeneratorBuilder = new ValuesFromSetDataGeneratorBuilder(propertyName);
        dataGeneratorBuilder.withRandomValues();
        return dataGeneratorBuilder;
    }

    public static ValuesFromSetDataGeneratorBuilder sequentialValueFromSetTypeProperty(String propertyName) {
        ValuesFromSetDataGeneratorBuilder dataGeneratorBuilder = new ValuesFromSetDataGeneratorBuilder(propertyName);
        dataGeneratorBuilder.withSequentialValues();
        return dataGeneratorBuilder;
    }

    public static UKPostCodeDataGeneratorBuilder ukPostCodeTypeProperty(String propertyName) {
        return new UKPostCodeDataGeneratorBuilder(propertyName);
    }

    public static USZipDataGeneratorBuilder usZipCodeTypeProperty(String propertyName) {
        return new USZipDataGeneratorBuilder(propertyName);
    }

    public static PhoneNumberDataGeneratorBuilder phoneNumberTypeProperty(String propertyName) {
        return new PhoneNumberDataGeneratorBuilder(propertyName);
    }
}
