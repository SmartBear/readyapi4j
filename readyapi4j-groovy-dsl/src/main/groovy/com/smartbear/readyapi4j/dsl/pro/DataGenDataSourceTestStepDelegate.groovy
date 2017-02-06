package com.smartbear.readyapi4j.dsl.pro

import com.smartbear.readyapi4j.testserver.teststeps.datasource.datagen.AbstractDataGeneratorBuilder
import com.smartbear.readyapi4j.testserver.teststeps.datasource.datagen.CustomStringDataGeneratorBuilder
import com.smartbear.readyapi4j.testserver.teststeps.datasource.datagen.DataGenDataSourceTestStepBuilder
import com.smartbear.readyapi4j.testserver.teststeps.datasource.datagen.DateAndTimeDataGeneratorBuilder
import com.smartbear.readyapi4j.testserver.teststeps.datasource.datagen.IntegerDataGeneratorBuilder
import com.smartbear.readyapi4j.testserver.teststeps.datasource.datagen.StringDataGeneratorBuilder

import static com.smartbear.readyapi4j.testserver.teststeps.datasource.datagen.DataGenerators.addressTypeProperty
import static com.smartbear.readyapi4j.testserver.teststeps.datasource.datagen.DataGenerators.anyGenderFirstNameTypeProperty
import static com.smartbear.readyapi4j.testserver.teststeps.datasource.datagen.DataGenerators.anyGenderFullNameTypeProperty
import static com.smartbear.readyapi4j.testserver.teststeps.datasource.datagen.DataGenerators.anyGenderLastNameTypeProperty
import static com.smartbear.readyapi4j.testserver.teststeps.datasource.datagen.DataGenerators.cityTypeProperty
import static com.smartbear.readyapi4j.testserver.teststeps.datasource.datagen.DataGenerators.countryTypeProperty
import static com.smartbear.readyapi4j.testserver.teststeps.datasource.datagen.DataGenerators.customStringTypeProperty
import static com.smartbear.readyapi4j.testserver.teststeps.datasource.datagen.DataGenerators.digitsBooleanTypeProperty
import static com.smartbear.readyapi4j.testserver.teststeps.datasource.datagen.DataGenerators.emailTypeProperty
import static com.smartbear.readyapi4j.testserver.teststeps.datasource.datagen.DataGenerators.femaleFullNameTypeProperty
import static com.smartbear.readyapi4j.testserver.teststeps.datasource.datagen.DataGenerators.femaleLastNameTypeProperty
import static com.smartbear.readyapi4j.testserver.teststeps.datasource.datagen.DataGenerators.fullStateNameTypeProperty
import static com.smartbear.readyapi4j.testserver.teststeps.datasource.datagen.DataGenerators.guidTypeProperty
import static com.smartbear.readyapi4j.testserver.teststeps.datasource.datagen.DataGenerators.ipv4ComputerAddressTypeProperty
import static com.smartbear.readyapi4j.testserver.teststeps.datasource.datagen.DataGenerators.mac48ComputerAddressTypeProperty
import static com.smartbear.readyapi4j.testserver.teststeps.datasource.datagen.DataGenerators.maleFirstNameTypeProperty
import static com.smartbear.readyapi4j.testserver.teststeps.datasource.datagen.DataGenerators.maleFullNameTypeProperty
import static com.smartbear.readyapi4j.testserver.teststeps.datasource.datagen.DataGenerators.maleLastNameTypeProperty
import static com.smartbear.readyapi4j.testserver.teststeps.datasource.datagen.DataGenerators.randomDateAndTimeTypeProperty
import static com.smartbear.readyapi4j.testserver.teststeps.datasource.datagen.DataGenerators.randomIntegerTypeProperty
import static com.smartbear.readyapi4j.testserver.teststeps.datasource.datagen.DataGenerators.sequentialIntegerTypeProperty
import static com.smartbear.readyapi4j.testserver.teststeps.datasource.datagen.DataGenerators.shortStateNameTypeProperty
import static com.smartbear.readyapi4j.testserver.teststeps.datasource.datagen.DataGenerators.ssnTypeProperty
import static com.smartbear.readyapi4j.testserver.teststeps.datasource.datagen.DataGenerators.stringTypeProperty
import static com.smartbear.readyapi4j.testserver.teststeps.datasource.datagen.DataGenerators.trueFalseBooleanTypeProperty
import static com.smartbear.readyapi4j.testserver.teststeps.datasource.datagen.DataGenerators.yesNoBooleanTypeProperty
import static groovy.lang.Closure.DELEGATE_FIRST

class DataGenDataSourceTestStepDelegate extends DataSourceTestStepDelegate<DataGenDataSourceTestStepBuilder> {
    DataGenDataSourceTestStepDelegate(String testStepName) {
        super(new DataGenDataSourceTestStepBuilder(), testStepName)
    }

    void numberOfRows(int numberOfRows) {
        dataSourceTestStepBuilder.withNumberOfRows(numberOfRows)
    }

    void cityNames(String propertyName) {
        dataSourceTestStepBuilder.withProperty(cityTypeProperty(propertyName))
    }

    void countryNames(String propertyName) {
        dataSourceTestStepBuilder.withProperty(countryTypeProperty(propertyName))
    }

    void streetAddresses(String propertyName) {
        dataSourceTestStepBuilder.withProperty(addressTypeProperty(propertyName))
    }

    void emails(String propertyName) {
        dataSourceTestStepBuilder.withProperty(emailTypeProperty(propertyName))
    }

    void guidValues(String propertyName) {
        dataSourceTestStepBuilder.withProperty(guidTypeProperty(propertyName))
    }

    void socialSecurityNumbers(String propertyName) {
        dataSourceTestStepBuilder.withProperty(ssnTypeProperty(propertyName))
    }

    void trueFalseBooleanValues(String propertyName) {
        dataSourceTestStepBuilder.withProperty(trueFalseBooleanTypeProperty(propertyName))
    }

    void yesNoBooleanValues(String propertyName) {
        dataSourceTestStepBuilder.withProperty(yesNoBooleanTypeProperty(propertyName))
    }

    void digitsBooleanValues(String propertyName) {
        dataSourceTestStepBuilder.withProperty(digitsBooleanTypeProperty(propertyName))
    }

    void ipv4ComputerAddresses(String propertyName) {
        dataSourceTestStepBuilder.withProperty(ipv4ComputerAddressTypeProperty(propertyName))
    }

    void mac48ComputerAddresses(String propertyName) {
        dataSourceTestStepBuilder.withProperty(mac48ComputerAddressTypeProperty(propertyName))
    }

    void customString(String propertyName, String customString) {
        CustomStringDataGeneratorBuilder propertyBuilder = customStringTypeProperty(propertyName)
        propertyBuilder.withValue(customString)
        dataSourceTestStepBuilder.withProperty(propertyBuilder)
    }

    void stringValues(String propertyName, @DelegatesTo(StringDataGeneratorDelegate) Closure dataGenConfig) {
        StringDataGeneratorBuilder stringDataGeneratorBuilder = stringTypeProperty(propertyName)
        StringDataGeneratorDelegate delegate = new StringDataGeneratorDelegate(stringDataGeneratorBuilder)
        addDataGenDataSource(delegate, dataGenConfig, stringDataGeneratorBuilder)
    }

    void fullStateNames(String propertyName) {
        dataSourceTestStepBuilder.withProperty(fullStateNameTypeProperty(propertyName))
    }

    void abbreviatedStateNames(String propertyName) {
        dataSourceTestStepBuilder.withProperty(shortStateNameTypeProperty(propertyName))
    }

    void dateAndTimeValues(String propertyName,
                           @DelegatesTo(DateAndTimeDataGeneratorDelegate) Closure dataGenConfig) {
        DateAndTimeDataGeneratorBuilder builder = randomDateAndTimeTypeProperty(propertyName)
        DateAndTimeDataGeneratorDelegate delegate = new DateAndTimeDataGeneratorDelegate(builder)
        addDataGenDataSource(delegate, dataGenConfig, builder)
    }

    void randomIntegers(String propertyName, @DelegatesTo(RandomIntegerDataGeneratorDelegate) Closure dataGenConfig) {
        IntegerDataGeneratorBuilder builder = randomIntegerTypeProperty(propertyName)
        RandomIntegerDataGeneratorDelegate delegate = new RandomIntegerDataGeneratorDelegate(builder)
        addDataGenDataSource(delegate, dataGenConfig, builder)
    }

    void sequentialIntegers(String propertyName,
                            @DelegatesTo(SequentialIntegerDataGeneratorDelegate) Closure dataGenConfig) {
        IntegerDataGeneratorBuilder builder = sequentialIntegerTypeProperty(propertyName)
        SequentialIntegerDataGeneratorDelegate delegate = new SequentialIntegerDataGeneratorDelegate(builder)
        addDataGenDataSource(delegate, dataGenConfig, builder)
    }

    void maleFirstNames(String propertyName) {
        dataSourceTestStepBuilder.withProperty(maleFirstNameTypeProperty(propertyName))
    }

    void maleLastNames(String propertyName) {
        dataSourceTestStepBuilder.withProperty(maleLastNameTypeProperty(propertyName))
    }

    void maleFullNames(String propertyName) {
        dataSourceTestStepBuilder.withProperty(maleFullNameTypeProperty(propertyName))
    }

    void femaleFirstlNames(String propertyName) {
        dataSourceTestStepBuilder.withProperty(femaleFirstlNames(propertyName))
    }

    void femaleLastNames(String propertyName) {
        dataSourceTestStepBuilder.withProperty(femaleLastNameTypeProperty(propertyName))
    }

    void femaleFullNames(String propertyName) {
        dataSourceTestStepBuilder.withProperty(femaleFullNameTypeProperty(propertyName))
    }

    /**
     *
     * @param propertyName
     */
    void firstNames(String propertyName) {
        dataSourceTestStepBuilder.withProperty(anyGenderFirstNameTypeProperty(propertyName))
    }

    void lastNames(String propertyName) {
        dataSourceTestStepBuilder.withProperty(anyGenderLastNameTypeProperty(propertyName))
    }

    void fullNames(String propertyName) {
        dataSourceTestStepBuilder.withProperty(anyGenderFullNameTypeProperty(propertyName))
    }

    private void addDataGenDataSource(Object delegate, Closure dataGenConfig, AbstractDataGeneratorBuilder builder) {
        dataGenConfig.delegate = delegate
        dataGenConfig.resolveStrategy = DELEGATE_FIRST
        dataGenConfig.call()
        dataSourceTestStepBuilder.withProperty(builder)
    }

}
