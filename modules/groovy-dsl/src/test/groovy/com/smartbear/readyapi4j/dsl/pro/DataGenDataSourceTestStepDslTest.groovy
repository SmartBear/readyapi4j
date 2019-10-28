package com.smartbear.readyapi4j.dsl.pro

import com.smartbear.readyapi4j.TestRecipe
import com.smartbear.readyapi4j.client.model.*
import com.smartbear.readyapi4j.client.model.BooleanDataGenerator.FormatEnum
import com.smartbear.readyapi4j.client.model.NameDataGenerator.GenderEnum
import com.smartbear.readyapi4j.client.model.NameDataGenerator.NameTypeEnum
import org.junit.Ignore
import org.junit.Test

import static com.smartbear.readyapi4j.client.model.BooleanDataGenerator.FormatEnum.*
import static com.smartbear.readyapi4j.client.model.ComputerAddressDataGenerator.AddressTypeEnum.IPV4
import static com.smartbear.readyapi4j.client.model.ComputerAddressDataGenerator.AddressTypeEnum.MAC48
import static com.smartbear.readyapi4j.client.model.DateAndTimeDataGenerator.DateTimeFormatEnum.YYYY_MM_DDTHH_MM_SSZ_ISO_8601_
import static com.smartbear.readyapi4j.client.model.NameDataGenerator.GenderEnum.*
import static com.smartbear.readyapi4j.client.model.NameDataGenerator.NameTypeEnum.*
import static com.smartbear.readyapi4j.client.model.UKPostCodeDataGenerator.CodeFormatEnum.AA99_9AA
import static com.smartbear.readyapi4j.client.model.USZIPCodeDataGenerator.CodeFormatEnum.XXXXX_XXXX
import static com.smartbear.readyapi4j.client.model.ValuesFromSetDataGenerator.GenerationModeEnum.RANDOM
import static com.smartbear.readyapi4j.client.model.ValuesFromSetDataGenerator.GenerationModeEnum.SEQUENTIAL
import static com.smartbear.readyapi4j.dsl.ServerTestDsl.recipe

@Ignore
class DataGenDataSourceTestStepDslTest {
    @Test
    void createsRecipeWithDataSourceTestStepWithBasicDataGenerators() throws Exception {
        TestRecipe testRecipe = recipe {
            withGeneratedData 'CityDataGenDataSource', {
                numberOfRows 2
                cityName 'cityName'
                countryName 'countryName'
                streetAddress 'address'
                email 'email'
                guidValue 'id'
                socialSecurityNumber 'SocialSecurityNumber'

                testSteps {
                    get 'http://hostname.com'
                }
            }
        }
        DataGenDataSource dataSource = extractDataGenDataSource(testRecipe)
        assert dataSource.numberOfRows == '2'
        assert dataSource.dataGenerators.size() == 6
        verifyDataGeneratorTypeAndProperty(dataSource.dataGenerators[0], 'City', 'cityName')
        verifyDataGeneratorTypeAndProperty(dataSource.dataGenerators[1], 'Country', 'countryName')
        verifyDataGeneratorTypeAndProperty(dataSource.dataGenerators[2], 'Street Address', 'address')
        verifyDataGeneratorTypeAndProperty(dataSource.dataGenerators[3], 'E-Mail', 'email')
        verifyDataGeneratorTypeAndProperty(dataSource.dataGenerators[4], 'Guid', 'id')
        verifyDataGeneratorTypeAndProperty(dataSource.dataGenerators[5], 'Social Security Number', 'SocialSecurityNumber')
    }

    @Test
    void createsRecipeWithDataSourceTestStepWithBooleanDataGenerators() throws Exception {
        TestRecipe testRecipe = recipe {
            withGeneratedData 'BooleanDataGenDataSource', {
                yesNoBooleanValue 'YesNoBooleanValue'
                trueFalseBooleanValue 'TrueFalseBooleanValue'
                digitsBooleanValue 'ZeroOneBooleanValue'

                testSteps {
                    get 'http://somehost.com'
                }
            }
        }
        DataGenDataSource dataSource = extractDataGenDataSource(testRecipe)
        verifyBooleanDataGenValues(dataSource.dataGenerators[0], 'YesNoBooleanValue', YES_NO)
        verifyBooleanDataGenValues(dataSource.dataGenerators[1], 'TrueFalseBooleanValue', TRUE_FALSE)
        verifyBooleanDataGenValues(dataSource.dataGenerators[2], 'ZeroOneBooleanValue', _1_0)
    }

    private static void verifyBooleanDataGenValues(DataGenerator dataGenerator, String propertyName,
                                                   FormatEnum format) {
        verifyDataGeneratorTypeAndProperty(dataGenerator, 'Boolean', propertyName)
        assert ((BooleanDataGenerator) dataGenerator).format == format
    }

    @Test
    void createsRecipeWithDataSourceTestStepWithComputerAddressDataGen() throws Exception {
        TestRecipe testRecipe = recipe {
            withGeneratedData 'ComputerAddressDataGenDataSource', {
                ipv4ComputerAddress 'IPv4Adresse'
                mac48ComputerAddress 'Mac48Addresse'
            }
        }
        DataGenDataSource dataSource = extractDataGenDataSource(testRecipe)
        verifyDataGeneratorTypeAndProperty(dataSource.dataGenerators[0], 'Computer Address', 'IPv4Adresse')
        ((ComputerAddressDataGenerator) dataSource.dataGenerators[0]).addressType == IPV4
        verifyDataGeneratorTypeAndProperty(dataSource.dataGenerators[1], 'Computer Address', 'Mac48Addresse')
        ((ComputerAddressDataGenerator) dataSource.dataGenerators[1]).addressType == MAC48
    }

    @Test
    void createsRecipeWithDataSourceTestStepWithStringDataGen() throws Exception {
        TestRecipe testRecipe = recipe {
            withGeneratedData 'StringDataGenDataSource', {
                stringValue 'StringValue', {
                    minimumCharacters 8
                    maximumCharacters 20
                    withoutSpaces
                    withoutPunctuationMarks
                    withoutDigits
                }
            }
        }
        DataGenDataSource dataSource = extractDataGenDataSource(testRecipe)
        StringDataGenerator stringDataGenerator = dataSource.dataGenerators[0] as StringDataGenerator
        assert stringDataGenerator.propertyName == 'StringValue'
        assert stringDataGenerator.minimumCharacters == 8
        assert stringDataGenerator.maximumCharacters == 20
        assert !stringDataGenerator.useSpaces
        assert !stringDataGenerator.useDigits
        assert !stringDataGenerator.usePunctuationMarks
        assert stringDataGenerator.useLetters
    }

    @Test
    void createsRecipeWithDataSourceTestStepWithCustomStringDataGen() throws Exception {
        TestRecipe testRecipe = recipe {
            withGeneratedData 'CustomStringDataGenDataSource', {
                customString 'ConstantString', 'This is a custom string!'
            }
        }
        DataGenDataSource dataSource = extractDataGenDataSource(testRecipe)
        CustomStringDataGenerator customStringDataGenerator = dataSource.dataGenerators[0] as CustomStringDataGenerator
        assert customStringDataGenerator.propertyName == 'ConstantString'
        assert customStringDataGenerator.value == 'This is a custom string!'
    }

    @Test
    void createsRecipeWithDataSourceTestStepWithShortStateNameDataGen() throws Exception {
        TestRecipe testRecipe = recipe {
            withGeneratedData 'ShortStateNamesDataGenDataSource', {
                abbreviatedStateName 'StateName'
            }
        }
        DataGenDataSource dataSource = extractDataGenDataSource(testRecipe)
        StateNameDataGenerator stateNameDataGenerator = dataSource.dataGenerators[0] as StateNameDataGenerator
        assert stateNameDataGenerator.propertyName == 'StateName'
        assert stateNameDataGenerator.nameFormat == StateNameDataGenerator.NameFormatEnum.ABBREVIATED
    }

    @Test
    void createsRecipeWithDataSourceTestStepWithFullStateNameDataGen() throws Exception {
        TestRecipe testRecipe = recipe {
            withGeneratedData 'FullStateNamesDataGenDataSource', {
                fullStateName 'FullStateName'
            }
        }
        DataGenDataSource dataSource = extractDataGenDataSource(testRecipe)
        StateNameDataGenerator stateNameDataGenerator = dataSource.dataGenerators[0] as StateNameDataGenerator
        assert stateNameDataGenerator.propertyName == 'FullStateName'
        assert stateNameDataGenerator.nameFormat == StateNameDataGenerator.NameFormatEnum.FULL
    }

    @Test
    void createsRecipeWithDataSourceTestStepWithDateTimeDataGen() throws Exception {
        Date startDate = Date.parse('yyyy-MM-dd hh:mm:ss', '2014-04-03 1:23:45')
        Date endDate = Date.parse('yyyy-MM-dd hh:mm:ss', '2016-04-03 23:59:59')

        TestRecipe testRecipe = recipe {
            withGeneratedData 'FullStateNamesDataGenDataSource', {
                dateAndTimeValue 'DateAndTime', {
                    between '2014-04-03 1:23:45' and '2016-04-03 23:59:59', 'yyyy-MM-dd hh:mm:ss'
                    format 'YYYY-MM-DDTHH:mm:ssZ (ISO-8601)'
                }
            }
        }
        DataGenDataSource dataSource = extractDataGenDataSource(testRecipe)
        DateAndTimeDataGenerator dateAndTimeDataGenerator = dataSource.dataGenerators[0] as DateAndTimeDataGenerator
        assert dateAndTimeDataGenerator.propertyName == 'DateAndTime'
        assert dateAndTimeDataGenerator.dateTimeFormat == YYYY_MM_DDTHH_MM_SSZ_ISO_8601_
        assert dateAndTimeDataGenerator.minimumValue == startDate
        assert dateAndTimeDataGenerator.maximumValue == endDate
    }

    @Test
    void createsRecipeWithDataSourceTestStepWithSequentialIntegerDataGen() throws Exception {

        TestRecipe testRecipe = recipe {
            withGeneratedData 'FullStateNamesDataGenDataSource', {
                sequentialInteger 'SequentialInteger', {
                    minimumValue 1
                    maximumValue 100
                    incrementBy 2
                }
            }
        }
        DataGenDataSource dataSource = extractDataGenDataSource(testRecipe)
        IntegerDataGenerator integerDataGenerator = dataSource.dataGenerators[0] as IntegerDataGenerator
        assert integerDataGenerator.generationMode == IntegerDataGenerator.GenerationModeEnum.SEQUENTIAL
        assert integerDataGenerator.propertyName == 'SequentialInteger'
        assert integerDataGenerator.minimumValue == 1
        assert integerDataGenerator.maximumValue == 100
        assert integerDataGenerator.incrementBy == 2
    }

    @Test
    void createsRecipeWithDataSourceTestStepWithRandomIntegerDataGen() throws Exception {

        TestRecipe testRecipe = recipe {
            withGeneratedData 'FullStateNamesDataGenDataSource', {
                randomInteger 'RandomInteger', {
                    minimumValue 100
                    maximumValue 1000
                }
            }
        }
        DataGenDataSource dataSource = extractDataGenDataSource(testRecipe)
        IntegerDataGenerator integerDataGenerator = dataSource.dataGenerators[0] as IntegerDataGenerator
        assert integerDataGenerator.generationMode == IntegerDataGenerator.GenerationModeEnum.RANDOM
        assert integerDataGenerator.propertyName == 'RandomInteger'
        assert integerDataGenerator.minimumValue == 100
        assert integerDataGenerator.maximumValue == 1000
    }

    @Test
    void createsRecipeWithDataSourceTestStepWithNamesDataGen() throws Exception {
        TestRecipe testRecipe = recipe {
            withGeneratedData 'NamesDataGenDataSource', {
                firstName 'AnyGenderFirstName'
                lastName 'AnyGenderLastName'
                fullName 'AnyGenderFullName'
                maleFirstName 'MaleFirstName'
                maleLastName 'MaleLastName'
                maleFullName 'MaleFullName'
                femaleFirstName 'FemaleFirstName'
                femaleLastName 'FemaleLastName'
                femaleFullName 'FemaleFullName'
            }
        }
        DataGenDataSource dataSource = extractDataGenDataSource(testRecipe)
        verifyNamesDataGeneratorValues(dataSource.dataGenerators[0], 'AnyGenderFirstName', ANY, FIRSTNAME)
        verifyNamesDataGeneratorValues(dataSource.dataGenerators[1], 'AnyGenderLastName', ANY, LASTNAME)
        verifyNamesDataGeneratorValues(dataSource.dataGenerators[2], 'AnyGenderFullName', ANY, FULL)
        verifyNamesDataGeneratorValues(dataSource.dataGenerators[3], 'MaleFirstName', MALE, FIRSTNAME)
        verifyNamesDataGeneratorValues(dataSource.dataGenerators[4], 'MaleLastName', MALE, LASTNAME)
        verifyNamesDataGeneratorValues(dataSource.dataGenerators[5], 'MaleFullName', MALE, FULL)
        verifyNamesDataGeneratorValues(dataSource.dataGenerators[6], 'FemaleFirstName', FEMALE, FIRSTNAME)
        verifyNamesDataGeneratorValues(dataSource.dataGenerators[7], 'FemaleLastName', FEMALE, LASTNAME)
        verifyNamesDataGeneratorValues(dataSource.dataGenerators[8], 'FemaleFullName', FEMALE, FULL)
    }

    private static void verifyNamesDataGeneratorValues(DataGenerator dataGenerator, String propertyName,
                                                       GenderEnum gender, NameTypeEnum nameType) {
        verifyDataGeneratorTypeAndProperty(dataGenerator, 'Name', propertyName)
        NameDataGenerator nameDataGenerator = dataGenerator as NameDataGenerator
        assert nameDataGenerator.gender == gender
        assert nameDataGenerator.nameType == nameType
    }

    @Test
    void createsRecipeWithDataSourceTestStepWithValueFromSetDataGen() throws Exception {

        TestRecipe testRecipe = recipe {
            withGeneratedData {
                randomValueFromSet 'RandomValue', ['Value1', 'Value2'].toSet()
                sequentialValueFromSet 'SequentialValue', ['Value3', 'Value4'].toSet()
            }
        }
        DataGenDataSource dataSource = extractDataGenDataSource(testRecipe)
        verifyValueFromSetDataGenValues(dataSource.dataGenerators[0], 'RandomValue', RANDOM, ['Value1', 'Value2'])
        verifyValueFromSetDataGenValues(dataSource.dataGenerators[1], 'SequentialValue', SEQUENTIAL, ['Value3', 'Value4'])
    }

    private static void verifyValueFromSetDataGenValues(DataGenerator dataGenerator, String property,
                                                        ValuesFromSetDataGenerator.GenerationModeEnum generationMode,
                                                        List<String> values) {
        verifyDataGeneratorTypeAndProperty(dataGenerator, 'Value from Set', property)
        ValuesFromSetDataGenerator valuesFromSetDataGenerator = dataGenerator as ValuesFromSetDataGenerator
        assert valuesFromSetDataGenerator.generationMode == generationMode
        assert valuesFromSetDataGenerator.values == values
    }

    @Test
    void createsRecipeWithDataSourceTestStepWithPhoneNumberDataGen() throws Exception {

        TestRecipe testRecipe = recipe {
            withGeneratedData {
                phoneNumber 'PhoneNumber'
                phoneNumber 'PhoneNumberWithDifferentFormat', '+X XXX-XXX-XXXX'
            }
        }
        DataGenDataSource dataSource = extractDataGenDataSource(testRecipe)
        verifyPhoneNumberDataGenValues(dataSource.dataGenerators[0], 'PhoneNumber', 'XXX-XXX-XXXX')
        verifyPhoneNumberDataGenValues(dataSource.dataGenerators[1], 'PhoneNumberWithDifferentFormat', '+X XXX-XXX-XXXX')
    }

    private static void verifyPhoneNumberDataGenValues(DataGenerator dataGenerator, String property, String format) {
        verifyDataGeneratorTypeAndProperty(dataGenerator, 'Phone Number', property)
        assert ((PhoneNumberDataGenerator) dataGenerator).numberFormat == format
    }

    @Test
    void createsRecipeWithDataSourceTestStepWithUKPostCodeDataGen() throws Exception {

        TestRecipe testRecipe = recipe {
            withGeneratedData {
                ukPostCode 'UKPostCode'
                ukPostCode 'UKPostcodeWithDifferentFormat', 'AA99 9AA'
            }
        }
        DataGenDataSource dataSource = extractDataGenDataSource(testRecipe)
        verifyUkPostCodeDataGenValues(dataSource.dataGenerators[0], 'UKPostCode', UKPostCodeDataGenerator.CodeFormatEnum.ALL)
        verifyUkPostCodeDataGenValues(dataSource.dataGenerators[1], 'UKPostcodeWithDifferentFormat', AA99_9AA)
    }

    private static void verifyUkPostCodeDataGenValues(DataGenerator dataGenerator, String property,
                                                      UKPostCodeDataGenerator.CodeFormatEnum format) {
        verifyDataGeneratorTypeAndProperty(dataGenerator, 'United Kingdom Postcode', property)
        assert ((UKPostCodeDataGenerator) dataGenerator).codeFormat == format
    }

    @Test
    void createsRecipeWithDataSourceTestStepWithUSZipCodeDataGen() throws Exception {

        TestRecipe testRecipe = recipe {
            withGeneratedData {
                usZipCode 'ZipCode'
                usZipCode 'ZipCodeWithDifferentFormat', 'XXXXX-XXXX'
            }
        }
        DataGenDataSource dataSource = extractDataGenDataSource(testRecipe)
        verifyUSZipCodeDataGenValues(dataSource.dataGenerators[0], 'ZipCode', USZIPCodeDataGenerator.CodeFormatEnum.ALL)
        verifyUSZipCodeDataGenValues(dataSource.dataGenerators[1], 'ZipCodeWithDifferentFormat', XXXXX_XXXX)
    }

    private static void verifyUSZipCodeDataGenValues(DataGenerator dataGenerator, String property,
                                                     USZIPCodeDataGenerator.CodeFormatEnum format) {
        verifyDataGeneratorTypeAndProperty(dataGenerator, 'United States ZIP Code', property)
        assert ((USZIPCodeDataGenerator) dataGenerator).codeFormat == format
    }

    private static DataGenDataSource extractDataGenDataSource(TestRecipe testRecipe) {
        DataSourceTestStep testStep = testRecipe.testCase.testSteps[0] as DataSourceTestStep
        testStep.dataSource.dataGen
    }

    private static void verifyDataGeneratorTypeAndProperty(DataGenerator dataGenerator, String type,
                                                           String propertyName) {
        assert dataGenerator.type == type
        assert dataGenerator.propertyName == propertyName
    }
}
