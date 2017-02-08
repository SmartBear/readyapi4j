package com.smartbear.readyapi4j.dsl.pro

import com.smartbear.readyapi.client.model.BooleanDataGenerator
import com.smartbear.readyapi.client.model.BooleanDataGenerator.FormatEnum
import com.smartbear.readyapi.client.model.ComputerAddressDataGenerator
import com.smartbear.readyapi.client.model.CustomStringDataGenerator
import com.smartbear.readyapi.client.model.DataGenDataSource
import com.smartbear.readyapi.client.model.DataGenerator
import com.smartbear.readyapi.client.model.DataSourceTestStep
import com.smartbear.readyapi.client.model.DateAndTimeDataGenerator
import com.smartbear.readyapi.client.model.IntegerDataGenerator
import com.smartbear.readyapi.client.model.NameDataGenerator
import com.smartbear.readyapi.client.model.NameDataGenerator.GenderEnum
import com.smartbear.readyapi.client.model.NameDataGenerator.NameTypeEnum
import com.smartbear.readyapi.client.model.PhoneNumberDataGenerator
import com.smartbear.readyapi.client.model.StateNameDataGenerator
import com.smartbear.readyapi.client.model.StringDataGenerator
import com.smartbear.readyapi.client.model.UKPostCodeDataGenerator
import com.smartbear.readyapi.client.model.USZIPCodeDataGenerator
import com.smartbear.readyapi.client.model.ValuesFromSetDataGenerator
import com.smartbear.readyapi4j.TestRecipe
import org.junit.Test

import static com.smartbear.readyapi.client.model.BooleanDataGenerator.FormatEnum.TRUE_FALSE
import static com.smartbear.readyapi.client.model.BooleanDataGenerator.FormatEnum.YES_NO
import static com.smartbear.readyapi.client.model.BooleanDataGenerator.FormatEnum._1_0
import static com.smartbear.readyapi.client.model.ComputerAddressDataGenerator.AddressTypeEnum.IPV4
import static com.smartbear.readyapi.client.model.ComputerAddressDataGenerator.AddressTypeEnum.MAC48
import static com.smartbear.readyapi.client.model.DateAndTimeDataGenerator.DateTimeFormatEnum.YYYY_MM_DDTHH_MM_SSZ_ISO_8601_
import static com.smartbear.readyapi.client.model.NameDataGenerator.GenderEnum.ANY
import static com.smartbear.readyapi.client.model.NameDataGenerator.GenderEnum.FEMALE
import static com.smartbear.readyapi.client.model.NameDataGenerator.GenderEnum.MALE
import static com.smartbear.readyapi.client.model.NameDataGenerator.NameTypeEnum.FIRSTNAME
import static com.smartbear.readyapi.client.model.NameDataGenerator.NameTypeEnum.FULL
import static com.smartbear.readyapi.client.model.NameDataGenerator.NameTypeEnum.LASTNAME
import static com.smartbear.readyapi.client.model.UKPostCodeDataGenerator.CodeFormatEnum.AA99_9AA
import static com.smartbear.readyapi.client.model.USZIPCodeDataGenerator.CodeFormatEnum.XXXXX_XXXX
import static com.smartbear.readyapi.client.model.ValuesFromSetDataGenerator.GenerationModeEnum.RANDOM
import static com.smartbear.readyapi.client.model.ValuesFromSetDataGenerator.GenerationModeEnum.SEQUENTIAL
import static com.smartbear.readyapi4j.dsl.ServerTestDsl.recipe

class DataGenDataSourceTestStepDslTest {
    @Test
    void createsRecipeWithDataSourceTestStepWithBasicDataGenerators() throws Exception {
        TestRecipe testRecipe = recipe {
            dataGeneratorDataSource 'CityDataGenDataSource', {
                numberOfRows 2
                cityNames 'cityNames'
                countryNames 'countryNames'
                streetAddresses 'addresses'
                emails 'emails'
                guidValues 'id'
                socialSecurityNumbers 'SocialSecurityNumbers'

                testSteps {
                    get 'http://hostname.com'
                }
            }
        }
        DataGenDataSource dataSource = extractDataGenDataSource(testRecipe)
        assert dataSource.numberOfRows == '2'
        assert dataSource.dataGenerators.size() == 6
        verifyDataGeneratorTypeAndProperty(dataSource.dataGenerators[0], 'City', 'cityNames')
        verifyDataGeneratorTypeAndProperty(dataSource.dataGenerators[1], 'Country', 'countryNames')
        verifyDataGeneratorTypeAndProperty(dataSource.dataGenerators[2], 'Street Address', 'addresses')
        verifyDataGeneratorTypeAndProperty(dataSource.dataGenerators[3], 'E-Mail', 'emails')
        verifyDataGeneratorTypeAndProperty(dataSource.dataGenerators[4], 'Guid', 'id')
        verifyDataGeneratorTypeAndProperty(dataSource.dataGenerators[5], 'Social Security Number', 'SocialSecurityNumbers')
    }

    @Test
    void createsRecipeWithDataSourceTestStepWithBooleanDataGenerators() throws Exception {
        TestRecipe testRecipe = recipe {
            dataGeneratorDataSource 'BooleanDataGenDataSource', {
                yesNoBooleanValues 'YesNoBooleanValues'
                trueFalseBooleanValues 'TrueFalseBooleanValues'
                digitsBooleanValues 'ZeroOneBooleanValues'

                testSteps {
                    get 'http://somehost.com'
                }
            }
        }
        DataGenDataSource dataSource = extractDataGenDataSource(testRecipe)
        verifyBooleanDataGenValues(dataSource.dataGenerators[0], 'YesNoBooleanValues', YES_NO)
        verifyBooleanDataGenValues(dataSource.dataGenerators[1], 'TrueFalseBooleanValues', TRUE_FALSE)
        verifyBooleanDataGenValues(dataSource.dataGenerators[2], 'ZeroOneBooleanValues', _1_0)
    }

    private static void verifyBooleanDataGenValues(DataGenerator dataGenerator, String propertyName,
                                                   FormatEnum format) {
        verifyDataGeneratorTypeAndProperty(dataGenerator, 'Boolean', propertyName)
        assert ((BooleanDataGenerator) dataGenerator).format == format
    }

    @Test
    void createsRecipeWithDataSourceTestStepWithComputerAddressDataGen() throws Exception {
        TestRecipe testRecipe = recipe {
            dataGeneratorDataSource 'ComputerAddressDataGenDataSource', {
                ipv4ComputerAddresses 'IPv4Adresses'
                mac48ComputerAddresses 'Mac48Addresses'
            }
        }
        DataGenDataSource dataSource = extractDataGenDataSource(testRecipe)
        verifyDataGeneratorTypeAndProperty(dataSource.dataGenerators[0], 'Computer Address', 'IPv4Adresses')
        ((ComputerAddressDataGenerator) dataSource.dataGenerators[0]).addressType == IPV4
        verifyDataGeneratorTypeAndProperty(dataSource.dataGenerators[1], 'Computer Address', 'Mac48Addresses')
        ((ComputerAddressDataGenerator) dataSource.dataGenerators[1]).addressType == MAC48
    }

    @Test
    void createsRecipeWithDataSourceTestStepWithStringDataGen() throws Exception {
        TestRecipe testRecipe = recipe {
            dataGeneratorDataSource 'StringDataGenDataSource', {
                stringValues 'Strings', {
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
        assert stringDataGenerator.propertyName == 'Strings'
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
            dataGeneratorDataSource 'CustomStringDataGenDataSource', {
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
            dataGeneratorDataSource 'ShortStateNamesDataGenDataSource', {
                abbreviatedStateNames 'StateNames'
            }
        }
        DataGenDataSource dataSource = extractDataGenDataSource(testRecipe)
        StateNameDataGenerator stateNameDataGenerator = dataSource.dataGenerators[0] as StateNameDataGenerator
        assert stateNameDataGenerator.propertyName == 'StateNames'
        assert stateNameDataGenerator.nameFormat == StateNameDataGenerator.NameFormatEnum.ABBREVIATED
    }

    @Test
    void createsRecipeWithDataSourceTestStepWithFullStateNameDataGen() throws Exception {
        TestRecipe testRecipe = recipe {
            dataGeneratorDataSource 'FullStateNamesDataGenDataSource', {
                fullStateNames 'FullStateNames'
            }
        }
        DataGenDataSource dataSource = extractDataGenDataSource(testRecipe)
        StateNameDataGenerator stateNameDataGenerator = dataSource.dataGenerators[0] as StateNameDataGenerator
        assert stateNameDataGenerator.propertyName == 'FullStateNames'
        assert stateNameDataGenerator.nameFormat == StateNameDataGenerator.NameFormatEnum.FULL
    }

    @Test
    void createsRecipeWithDataSourceTestStepWithDateTimeDataGen() throws Exception {
        Date startDate = Date.parse('yyyy-MM-dd hh:mm:ss', '2014-04-03 1:23:45')
        Date endDate = Date.parse('yyyy-MM-dd hh:mm:ss', '2016-04-03 23:59:59')

        TestRecipe testRecipe = recipe {
            dataGeneratorDataSource 'FullStateNamesDataGenDataSource', {
                dateAndTimeValues 'DateAndTimes', {
                    between '2014-04-03 1:23:45' and '2016-04-03 23:59:59', 'yyyy-MM-dd hh:mm:ss'
                    format 'YYYY-MM-DDTHH:mm:ssZ (ISO-8601)'
                }
            }
        }
        DataGenDataSource dataSource = extractDataGenDataSource(testRecipe)
        DateAndTimeDataGenerator dateAndTimeDataGenerator = dataSource.dataGenerators[0] as DateAndTimeDataGenerator
        assert dateAndTimeDataGenerator.propertyName == 'DateAndTimes'
        assert dateAndTimeDataGenerator.dateTimeFormat == YYYY_MM_DDTHH_MM_SSZ_ISO_8601_
        assert dateAndTimeDataGenerator.minimumValue == startDate
        assert dateAndTimeDataGenerator.maximumValue == endDate
    }

    @Test
    void createsRecipeWithDataSourceTestStepWithSequentialIntegerDataGen() throws Exception {

        TestRecipe testRecipe = recipe {
            dataGeneratorDataSource 'FullStateNamesDataGenDataSource', {
                sequentialIntegers 'SequentialIntegers', {
                    minimumValue 1
                    maximumValue 100
                    incrementBy 2
                }
            }
        }
        DataGenDataSource dataSource = extractDataGenDataSource(testRecipe)
        IntegerDataGenerator integerDataGenerator = dataSource.dataGenerators[0] as IntegerDataGenerator
        assert integerDataGenerator.generationMode == IntegerDataGenerator.GenerationModeEnum.SEQUENTIAL
        assert integerDataGenerator.propertyName == 'SequentialIntegers'
        assert integerDataGenerator.minimumValue == 1
        assert integerDataGenerator.maximumValue == 100
        assert integerDataGenerator.incrementBy == 2
    }

    @Test
    void createsRecipeWithDataSourceTestStepWithRandomIntegerDataGen() throws Exception {

        TestRecipe testRecipe = recipe {
            dataGeneratorDataSource 'FullStateNamesDataGenDataSource', {
                randomIntegers 'RandomIntegers', {
                    minimumValue 100
                    maximumValue 1000
                }
            }
        }
        DataGenDataSource dataSource = extractDataGenDataSource(testRecipe)
        IntegerDataGenerator integerDataGenerator = dataSource.dataGenerators[0] as IntegerDataGenerator
        assert integerDataGenerator.generationMode == IntegerDataGenerator.GenerationModeEnum.RANDOM
        assert integerDataGenerator.propertyName == 'RandomIntegers'
        assert integerDataGenerator.minimumValue == 100
        assert integerDataGenerator.maximumValue == 1000
    }

    @Test
    void createsRecipeWithDataSourceTestStepWithNamesDataGen() throws Exception {
        TestRecipe testRecipe = recipe {
            dataGeneratorDataSource 'NamesDataGenDataSource', {
                firstNames 'AnyGenderFirstNames'
                lastNames 'AnyGenderLastNames'
                fullNames 'AnyGenderFullNames'
                maleFirstNames 'MaleFirstNames'
                maleLastNames 'MaleLastNames'
                maleFullNames 'MaleFullNames'
                femaleFirstNames 'FemaleFirstNames'
                femaleLastNames 'FemaleLastNames'
                femaleFullNames 'FemaleFullNames'
            }
        }
        DataGenDataSource dataSource = extractDataGenDataSource(testRecipe)
        verifyNamesDataGeneratorValues(dataSource.dataGenerators[0], 'AnyGenderFirstNames', ANY, FIRSTNAME)
        verifyNamesDataGeneratorValues(dataSource.dataGenerators[1], 'AnyGenderLastNames', ANY, LASTNAME)
        verifyNamesDataGeneratorValues(dataSource.dataGenerators[2], 'AnyGenderFullNames', ANY, FULL)
        verifyNamesDataGeneratorValues(dataSource.dataGenerators[3], 'MaleFirstNames', MALE, FIRSTNAME)
        verifyNamesDataGeneratorValues(dataSource.dataGenerators[4], 'MaleLastNames', MALE, LASTNAME)
        verifyNamesDataGeneratorValues(dataSource.dataGenerators[5], 'MaleFullNames', MALE, FULL)
        verifyNamesDataGeneratorValues(dataSource.dataGenerators[6], 'FemaleFirstNames', FEMALE, FIRSTNAME)
        verifyNamesDataGeneratorValues(dataSource.dataGenerators[7], 'FemaleLastNames', FEMALE, LASTNAME)
        verifyNamesDataGeneratorValues(dataSource.dataGenerators[8], 'FemaleFullNames', FEMALE, FULL)
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
            dataGeneratorDataSource {
                randomValueFromSet 'RandomValues', ['Value1', 'Value2'].toSet()
                sequentialValueFromSet 'SequentialValues', ['Value3', 'Value4'].toSet()
            }
        }
        DataGenDataSource dataSource = extractDataGenDataSource(testRecipe)
        verifyValueFromSetDataGenValues(dataSource.dataGenerators[0], 'RandomValues', RANDOM, ['Value1', 'Value2'])
        verifyValueFromSetDataGenValues(dataSource.dataGenerators[1], 'SequentialValues', SEQUENTIAL, ['Value3', 'Value4'])
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
            dataGeneratorDataSource {
                phoneNumbers 'PhoneNumbers'
                phoneNumbers 'PhonesWithDifferentFormat', '+X XXX-XXX-XXXX'
            }
        }
        DataGenDataSource dataSource = extractDataGenDataSource(testRecipe)
        verifyPhoneNumberDataGenValues(dataSource.dataGenerators[0], 'PhoneNumbers', 'XXX-XXX-XXXX')
        verifyPhoneNumberDataGenValues(dataSource.dataGenerators[1], 'PhonesWithDifferentFormat', '+X XXX-XXX-XXXX')
    }

    private static void verifyPhoneNumberDataGenValues(DataGenerator dataGenerator, String property, String format) {
        verifyDataGeneratorTypeAndProperty(dataGenerator, 'Phone Number', property)
        assert ((PhoneNumberDataGenerator) dataGenerator).numberFormat == format
    }

    @Test
    void createsRecipeWithDataSourceTestStepWithUKPostCodeDataGen() throws Exception {

        TestRecipe testRecipe = recipe {
            dataGeneratorDataSource {
                ukPostCodes 'UKPostCodes'
                ukPostCodes 'UKPostcodesWithDifferentFormat', 'AA99 9AA'
            }
        }
        DataGenDataSource dataSource = extractDataGenDataSource(testRecipe)
        verifyUkPostCodeDataGenValues(dataSource.dataGenerators[0], 'UKPostCodes', UKPostCodeDataGenerator.CodeFormatEnum.ALL)
        verifyUkPostCodeDataGenValues(dataSource.dataGenerators[1], 'UKPostcodesWithDifferentFormat', AA99_9AA)
    }

    private static void verifyUkPostCodeDataGenValues(DataGenerator dataGenerator, String property,
                                                      UKPostCodeDataGenerator.CodeFormatEnum format) {
        verifyDataGeneratorTypeAndProperty(dataGenerator, 'United Kingdom Postcode', property)
        assert ((UKPostCodeDataGenerator) dataGenerator).codeFormat == format
    }

    @Test
    void createsRecipeWithDataSourceTestStepWithUSZipCodeDataGen() throws Exception {

        TestRecipe testRecipe = recipe {
            dataGeneratorDataSource {
                usZipCodes 'ZipCodes'
                usZipCodes 'ZipCodesWithDifferentFormat', 'XXXXX-XXXX'
            }
        }
        DataGenDataSource dataSource = extractDataGenDataSource(testRecipe)
        verifyUSZipCodeDataGenValues(dataSource.dataGenerators[0], 'ZipCodes', USZIPCodeDataGenerator.CodeFormatEnum.ALL)
        verifyUSZipCodeDataGenValues(dataSource.dataGenerators[1], 'ZipCodesWithDifferentFormat', XXXXX_XXXX)
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
