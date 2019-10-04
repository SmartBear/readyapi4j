package com.smartbear.readyapi4j.dsl.pro

import com.smartbear.readyapi4j.testengine.teststeps.datasource.datagen.*

import static com.smartbear.readyapi4j.testengine.teststeps.datasource.datagen.DataGenerators.*
import static groovy.lang.Closure.DELEGATE_FIRST

class DataGenDataSourceTestStepDelegate extends DataSourceTestStepDelegate<DataGenDataSourceTestStepBuilder> {
    DataGenDataSourceTestStepDelegate(String testStepName) {
        super(new DataGenDataSourceTestStepBuilder(), testStepName)
    }

    void numberOfRows(int numberOfRows) {
        dataSourceTestStepBuilder.withNumberOfRows(numberOfRows)
    }

    void cityName(String propertyName) {
        dataSourceTestStepBuilder.withProperty(cityTypeProperty(propertyName))
    }

    void countryName(String propertyName) {
        dataSourceTestStepBuilder.withProperty(countryTypeProperty(propertyName))
    }

    void streetAddress(String propertyName) {
        dataSourceTestStepBuilder.withProperty(addressTypeProperty(propertyName))
    }

    void email(String propertyName) {
        dataSourceTestStepBuilder.withProperty(emailTypeProperty(propertyName))
    }

    void guidValue(String propertyName) {
        dataSourceTestStepBuilder.withProperty(guidTypeProperty(propertyName))
    }

    void socialSecurityNumber(String propertyName) {
        dataSourceTestStepBuilder.withProperty(ssnTypeProperty(propertyName))
    }

    void trueFalseBooleanValue(String propertyName) {
        dataSourceTestStepBuilder.withProperty(trueFalseBooleanTypeProperty(propertyName))
    }

    void yesNoBooleanValue(String propertyName) {
        dataSourceTestStepBuilder.withProperty(yesNoBooleanTypeProperty(propertyName))
    }

    void digitsBooleanValue(String propertyName) {
        dataSourceTestStepBuilder.withProperty(digitsBooleanTypeProperty(propertyName))
    }

    void ipv4ComputerAddress(String propertyName) {
        dataSourceTestStepBuilder.withProperty(ipv4ComputerAddressTypeProperty(propertyName))
    }

    void mac48ComputerAddress(String propertyName) {
        dataSourceTestStepBuilder.withProperty(mac48ComputerAddressTypeProperty(propertyName))
    }

    void customString(String propertyName, String customString) {
        CustomStringDataGeneratorBuilder propertyBuilder = customStringTypeProperty(propertyName)
        propertyBuilder.withValue(customString)
        dataSourceTestStepBuilder.withProperty(propertyBuilder)
    }

    void stringValue(String propertyName, @DelegatesTo(StringDataGeneratorDelegate) Closure dataGenConfig) {
        StringDataGeneratorBuilder stringDataGeneratorBuilder = stringTypeProperty(propertyName)
        StringDataGeneratorDelegate delegate = new StringDataGeneratorDelegate(stringDataGeneratorBuilder)
        addDataGenDataSource(delegate, dataGenConfig, stringDataGeneratorBuilder)
    }

    void fullStateName(String propertyName) {
        dataSourceTestStepBuilder.withProperty(fullStateNameTypeProperty(propertyName))
    }

    void abbreviatedStateName(String propertyName) {
        dataSourceTestStepBuilder.withProperty(shortStateNameTypeProperty(propertyName))
    }

    void dateAndTimeValue(String propertyName, @DelegatesTo(DateAndTimeDataGeneratorDelegate) Closure dataGenConfig) {
        DateAndTimeDataGeneratorBuilder builder = randomDateAndTimeTypeProperty(propertyName)
        DateAndTimeDataGeneratorDelegate delegate = new DateAndTimeDataGeneratorDelegate(builder)
        addDataGenDataSource(delegate, dataGenConfig, builder)
    }

    void randomInteger(String propertyName, @DelegatesTo(RandomIntegerDataGeneratorDelegate) Closure dataGenConfig) {
        IntegerDataGeneratorBuilder builder = randomIntegerTypeProperty(propertyName)
        RandomIntegerDataGeneratorDelegate delegate = new RandomIntegerDataGeneratorDelegate(builder)
        addDataGenDataSource(delegate, dataGenConfig, builder)
    }

    void sequentialInteger(String propertyName,
                           @DelegatesTo(SequentialIntegerDataGeneratorDelegate) Closure dataGenConfig) {
        IntegerDataGeneratorBuilder builder = sequentialIntegerTypeProperty(propertyName)
        SequentialIntegerDataGeneratorDelegate delegate = new SequentialIntegerDataGeneratorDelegate(builder)
        addDataGenDataSource(delegate, dataGenConfig, builder)
    }

    void maleFirstName(String propertyName) {
        dataSourceTestStepBuilder.withProperty(maleFirstNameTypeProperty(propertyName))
    }

    void maleLastName(String propertyName) {
        dataSourceTestStepBuilder.withProperty(maleLastNameTypeProperty(propertyName))
    }

    void maleFullName(String propertyName) {
        dataSourceTestStepBuilder.withProperty(maleFullNameTypeProperty(propertyName))
    }

    void femaleFirstName(String propertyName) {
        dataSourceTestStepBuilder.withProperty(femaleFirstNameTypeProperty(propertyName))
    }

    void femaleLastName(String propertyName) {
        dataSourceTestStepBuilder.withProperty(femaleLastNameTypeProperty(propertyName))
    }

    void femaleFullName(String propertyName) {
        dataSourceTestStepBuilder.withProperty(femaleFullNameTypeProperty(propertyName))
    }

    /**
     * Generates first names for both genders: Male and Female
     * @param propertyName property name
     */
    void firstName(String propertyName) {
        dataSourceTestStepBuilder.withProperty(anyGenderFirstNameTypeProperty(propertyName))
    }

    /**
     * Generates last names for both genders: Male and Female
     * @param propertyName property name
     */
    void lastName(String propertyName) {
        dataSourceTestStepBuilder.withProperty(anyGenderLastNameTypeProperty(propertyName))
    }

    /**
     * Generates full names for both genders: Male and Female
     * @param propertyName property name
     */
    void fullName(String propertyName) {
        dataSourceTestStepBuilder.withProperty(anyGenderFullNameTypeProperty(propertyName))
    }

    void randomValueFromSet(String propertyName, Set<String> predefinedValues) {
        ValuesFromSetDataGeneratorBuilder builder = randomValueFromSetTypeProperty(propertyName)
        builder.withValues(predefinedValues)
        dataSourceTestStepBuilder.withProperty(builder)
    }

    void sequentialValueFromSet(String propertyName, Set<String> predefinedValues) {
        ValuesFromSetDataGeneratorBuilder builder = sequentialValueFromSetTypeProperty(propertyName)
        builder.withValues(predefinedValues)
        dataSourceTestStepBuilder.withProperty(builder)
    }

    /**
     * Adds a phone number data generator
     * @param propertyName property name
     * @param format one of:'XXX-XXX-XXXX', '+1 XXX-XXX-XXXX', '+1 (XXX)-XXX-XXXX', '+X XXX-XXX-XXXX', '+X (XXX)-XXX-XXXX'
     */
    void phoneNumber(String propertyName, String format = 'XXX-XXX-XXXX') {
        PhoneNumberDataGeneratorBuilder phoneNumberDataGeneratorBuilder = phoneNumberTypeProperty(propertyName)
        phoneNumberDataGeneratorBuilder.withNumberFormat(format)
        dataSourceTestStepBuilder.withProperty(phoneNumberDataGeneratorBuilder)
    }

    /**
     * Adds a US ZIP codes generator
     * @param propertyName
     * @param format one of: 'All', 'XXXXX', 'XXXXX-XXXX'
     */
    void usZipCode(String propertyName, String format = 'All') {
        USZipDataGeneratorBuilder uSZipDataGeneratorBuilder = usZipCodeTypeProperty(propertyName)
        uSZipDataGeneratorBuilder.setFormat(format)
        dataSourceTestStepBuilder.withProperty(uSZipDataGeneratorBuilder)
    }

    /**
     * Adds a UK post codes generator
     * @param propertyName
     * @param format one of: 'All', 'A9 9AA', 'A99 9AA', 'AA9 9AA', 'A9A 9AA', 'AA99 9AA', 'AA9A 9AA'
     */
    void ukPostCode(String propertyName = 'UKPostCodes', String format = 'All') {
        UKPostCodeDataGeneratorBuilder ukPostCodeDataGeneratorBuilder = ukPostCodeTypeProperty(propertyName)
        ukPostCodeDataGeneratorBuilder.setFormat(format)
        dataSourceTestStepBuilder.withProperty(ukPostCodeDataGeneratorBuilder)
    }

    private void addDataGenDataSource(Object delegate, Closure dataGenConfig, AbstractDataGeneratorBuilder builder) {
        dataGenConfig.delegate = delegate
        dataGenConfig.resolveStrategy = DELEGATE_FIRST
        dataGenConfig.call()
        dataSourceTestStepBuilder.withProperty(builder)
    }

}
