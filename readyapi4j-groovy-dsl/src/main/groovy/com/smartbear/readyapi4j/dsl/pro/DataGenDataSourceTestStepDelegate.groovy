package com.smartbear.readyapi4j.dsl.pro

import com.smartbear.readyapi4j.testserver.teststeps.datasource.datagen.CustomStringDataGeneratorBuilder
import com.smartbear.readyapi4j.testserver.teststeps.datasource.datagen.DataGenDataSourceTestStepBuilder
import com.smartbear.readyapi4j.testserver.teststeps.datasource.datagen.DateAndTimeDataGeneratorBuilder
import com.smartbear.readyapi4j.testserver.teststeps.datasource.datagen.StringDataGeneratorBuilder

import static com.smartbear.readyapi4j.testserver.teststeps.datasource.datagen.DataGenerators.addressTypeProperty
import static com.smartbear.readyapi4j.testserver.teststeps.datasource.datagen.DataGenerators.cityTypeProperty
import static com.smartbear.readyapi4j.testserver.teststeps.datasource.datagen.DataGenerators.countryTypeProperty
import static com.smartbear.readyapi4j.testserver.teststeps.datasource.datagen.DataGenerators.customStringTypeProperty
import static com.smartbear.readyapi4j.testserver.teststeps.datasource.datagen.DataGenerators.digitsBooleanTypeProperty
import static com.smartbear.readyapi4j.testserver.teststeps.datasource.datagen.DataGenerators.emailTypeProperty
import static com.smartbear.readyapi4j.testserver.teststeps.datasource.datagen.DataGenerators.fullStateNameTypeProperty
import static com.smartbear.readyapi4j.testserver.teststeps.datasource.datagen.DataGenerators.guidTypeProperty
import static com.smartbear.readyapi4j.testserver.teststeps.datasource.datagen.DataGenerators.ipv4ComputerAddressTypeProperty
import static com.smartbear.readyapi4j.testserver.teststeps.datasource.datagen.DataGenerators.mac48ComputerAddressTypeProperty
import static com.smartbear.readyapi4j.testserver.teststeps.datasource.datagen.DataGenerators.randomDateAndTimeTypeProperty
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
        dataGenConfig.delegate = delegate
        dataGenConfig.resolveStrategy = DELEGATE_FIRST
        dataGenConfig.call()
        dataSourceTestStepBuilder.withProperty(stringDataGeneratorBuilder)
    }

    void fullStateNames(String propertyName) {
        dataSourceTestStepBuilder.withProperty(fullStateNameTypeProperty(propertyName))
    }

    void abbreviatedStateNames(String propertyName) {
        dataSourceTestStepBuilder.withProperty(shortStateNameTypeProperty(propertyName))
    }

    void dateAndTimeValues(String propertyName,
                           @DelegatesTo(DateAndTimeDataGeneratorDelegate) Closure dataGenConfig) {
        DateAndTimeDataGeneratorBuilder dateAndTimeDataGeneratorBuilder = randomDateAndTimeTypeProperty(propertyName)
        DateAndTimeDataGeneratorDelegate delegate = new DateAndTimeDataGeneratorDelegate(dateAndTimeDataGeneratorBuilder)
        dataGenConfig.delegate = delegate
        dataGenConfig.resolveStrategy = DELEGATE_FIRST
        dataGenConfig.call()
        dataSourceTestStepBuilder.withProperty(dateAndTimeDataGeneratorBuilder)
    }

}
