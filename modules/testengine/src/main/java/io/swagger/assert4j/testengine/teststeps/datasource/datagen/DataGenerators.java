package io.swagger.assert4j.testengine.teststeps.datasource.datagen;

public class DataGenerators {
    public static BooleanDataGeneratorBuilder trueFalseBooleanTypeProperty(String propertyName) {
        return new BooleanDataGeneratorBuilder(propertyName);
    }

    public static BooleanDataGeneratorBuilder yesNoBooleanTypeProperty(String propertyName) {
        BooleanDataGeneratorBuilder booleanDataGeneratorBuilder = new BooleanDataGeneratorBuilder(propertyName);
        booleanDataGeneratorBuilder.withYesNoFormat();
        return booleanDataGeneratorBuilder;
    }

    public static BooleanDataGeneratorBuilder digitsBooleanTypeProperty(String propertyName) {
        BooleanDataGeneratorBuilder booleanDataGeneratorBuilder = new BooleanDataGeneratorBuilder(propertyName);
        booleanDataGeneratorBuilder.withDigitsFormat();
        return booleanDataGeneratorBuilder;
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

    public static ComputerAddressDataGeneratorBuilder ipv4ComputerAddressTypeProperty(String propertyName) {
        return new ComputerAddressDataGeneratorBuilder(propertyName);
    }

    public static ComputerAddressDataGeneratorBuilder mac48ComputerAddressTypeProperty(String propertyName) {
        ComputerAddressDataGeneratorBuilder computerAddressDataGeneratorBuilder = new ComputerAddressDataGeneratorBuilder(propertyName);
        computerAddressDataGeneratorBuilder.withMac48Format();
        return computerAddressDataGeneratorBuilder;
    }

    public static CustomStringDataGeneratorBuilder customStringTypeProperty(String propertyName) {
        return new CustomStringDataGeneratorBuilder(propertyName);
    }

    public static StringDataGeneratorBuilder stringTypeProperty(String propertyName) {
        return new StringDataGeneratorBuilder(propertyName);
    }

    public static StateNameDataGeneratorBuilder fullStateNameTypeProperty(String propertyName) {
        return new StateNameDataGeneratorBuilder(propertyName);
    }

    public static StateNameDataGeneratorBuilder shortStateNameTypeProperty(String propertyName) {
        StateNameDataGeneratorBuilder stateNameDataGeneratorBuilder = new StateNameDataGeneratorBuilder(propertyName);
        stateNameDataGeneratorBuilder.withAbbreviatedNames();
        return stateNameDataGeneratorBuilder;
    }

    public static NameDataGeneratorBuilder anyGenderFullNameTypeProperty(String propertyName) {
        return new NameDataGeneratorBuilder(propertyName);
    }

    public static NameDataGeneratorBuilder anyGenderFirstNameTypeProperty(String propertyName) {
        NameDataGeneratorBuilder nameDataGeneratorBuilder = new NameDataGeneratorBuilder(propertyName);
        nameDataGeneratorBuilder.withFirstNames();
        return nameDataGeneratorBuilder;
    }

    public static NameDataGeneratorBuilder anyGenderLastNameTypeProperty(String propertyName) {
        NameDataGeneratorBuilder nameDataGeneratorBuilder = new NameDataGeneratorBuilder(propertyName);
        nameDataGeneratorBuilder.withLatsNames();
        return nameDataGeneratorBuilder;
    }

    public static NameDataGeneratorBuilder maleFullNameTypeProperty(String propertyName) {
        NameDataGeneratorBuilder nameDataGeneratorBuilder = new NameDataGeneratorBuilder(propertyName);
        nameDataGeneratorBuilder.withGenderMale();
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
        return new IntegerDataGeneratorBuilder(propertyName);
    }


    public static IntegerDataGeneratorBuilder sequentialIntegerTypeProperty(String propertyName) {
        IntegerDataGeneratorBuilder integerDataGeneratorBuilder = new IntegerDataGeneratorBuilder(propertyName);
        integerDataGeneratorBuilder.withSequentialValues();
        return integerDataGeneratorBuilder;
    }

    public static RealNumberDataGeneratorBuilder randomRealNumberTypeProperty(String propertyName) {
        return new RealNumberDataGeneratorBuilder(propertyName);
    }

    public static RealNumberDataGeneratorBuilder sequentialRealNumberTypeProperty(String propertyName) {
        RealNumberDataGeneratorBuilder realNumberDataGeneratorBuilder = new RealNumberDataGeneratorBuilder(propertyName);
        realNumberDataGeneratorBuilder.withSequentialValues();
        return realNumberDataGeneratorBuilder;
    }

    public static ValuesFromSetDataGeneratorBuilder randomValueFromSetTypeProperty(String propertyName) {
        return new ValuesFromSetDataGeneratorBuilder(propertyName);
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

    public static DateAndTimeDataGeneratorBuilder randomDateAndTimeTypeProperty(String propertyName) {
        return new DateAndTimeDataGeneratorBuilder(propertyName);
    }

    public static DateAndTimeDataGeneratorBuilder sequentialDateAndTimeTypeProperty(String propertyName) {
        DateAndTimeDataGeneratorBuilder dateAndTimeDataGeneratorBuilder = new DateAndTimeDataGeneratorBuilder(propertyName);
        dateAndTimeDataGeneratorBuilder.withSequentialValues();
        return dateAndTimeDataGeneratorBuilder;
    }
}
