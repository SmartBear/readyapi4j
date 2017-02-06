package com.smartbear.readyapi4j.dsl.pro

import com.smartbear.readyapi.client.model.BooleanDataGenerator
import com.smartbear.readyapi.client.model.BooleanDataGenerator.FormatEnum
import com.smartbear.readyapi.client.model.ComputerAddressDataGenerator
import com.smartbear.readyapi.client.model.CustomStringDataGenerator
import com.smartbear.readyapi.client.model.DataGenDataSource
import com.smartbear.readyapi.client.model.DataGenerator
import com.smartbear.readyapi.client.model.DataSourceTestStep
import com.smartbear.readyapi.client.model.DateAndTimeDataGenerator
import com.smartbear.readyapi.client.model.StateNameDataGenerator
import com.smartbear.readyapi.client.model.StringDataGenerator
import com.smartbear.readyapi4j.TestRecipe
import org.junit.Test

import static com.smartbear.readyapi.client.model.BooleanDataGenerator.FormatEnum.TRUE_FALSE
import static com.smartbear.readyapi.client.model.BooleanDataGenerator.FormatEnum.YES_NO
import static com.smartbear.readyapi.client.model.BooleanDataGenerator.FormatEnum._1_0
import static com.smartbear.readyapi.client.model.ComputerAddressDataGenerator.AddressTypeEnum.IPV4
import static com.smartbear.readyapi.client.model.ComputerAddressDataGenerator.AddressTypeEnum.MAC48
import static com.smartbear.readyapi.client.model.DateAndTimeDataGenerator.DateTimeFormatEnum.YYYY_MM_DDTHH_MM_SSZ_ISO_8601_
import static com.smartbear.readyapi4j.dsl.ServerTestDsl.recipe

class DataGenDataSourceTestStepDslTest {
    @Test
    void createsRecipeWithDataSourceTestStepWithBasicDataGenerators() throws Exception {
        TestRecipe testRecipe = recipe {
            dataGenDataSource 'CityDataGenDataSource', {
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
        assertDataGenerator(dataSource.dataGenerators[0], 'City', 'cityNames')
        assertDataGenerator(dataSource.dataGenerators[1], 'Country', 'countryNames')
        assertDataGenerator(dataSource.dataGenerators[2], 'Street Address', 'addresses')
        assertDataGenerator(dataSource.dataGenerators[3], 'E-Mail', 'emails')
        assertDataGenerator(dataSource.dataGenerators[4], 'Guid', 'id')
        assertDataGenerator(dataSource.dataGenerators[5], 'Social Security Number', 'SocialSecurityNumbers')
    }

    @Test
    void createsRecipeWithDataSourceTestStepWithBooleanDataGenerators() throws Exception {
        TestRecipe testRecipe = recipe {
            dataGenDataSource 'BooleanDataGenDataSource', {
                yesNoBooleanValues 'YesNoBooleanValues'
                trueFalseBooleanValues 'TrueFalseBooleanValues'
                digitsBooleanValues 'ZeroOneBooleanValues'

                testSteps {
                    get 'http://somehost.com'
                }
            }
        }
        DataGenDataSource dataSource = extractDataGenDataSource(testRecipe)
        assertBooleanDataGen(dataSource.dataGenerators[0], 'YesNoBooleanValues', YES_NO)
        assertBooleanDataGen(dataSource.dataGenerators[1], 'TrueFalseBooleanValues', TRUE_FALSE)
        assertBooleanDataGen(dataSource.dataGenerators[2], 'ZeroOneBooleanValues', _1_0)
    }

    @Test
    void createsRecipeWithDataSourceTestStepWithComputerAddressDataGen() throws Exception {
        TestRecipe testRecipe = recipe {
            dataGenDataSource 'ComputerAddressDataGenDataSource', {
                ipv4ComputerAddresses 'IPv4Adresses'
                mac48ComputerAddresses 'Mac48Addresses'
            }
        }
        DataGenDataSource dataSource = extractDataGenDataSource(testRecipe)
        assertDataGenerator(dataSource.dataGenerators[0], 'Computer Address', 'IPv4Adresses')
        ((ComputerAddressDataGenerator) dataSource.dataGenerators[0]).addressType == IPV4
        assertDataGenerator(dataSource.dataGenerators[1], 'Computer Address', 'Mac48Addresses')
        ((ComputerAddressDataGenerator) dataSource.dataGenerators[1]).addressType == MAC48
    }

    @Test
    void createsRecipeWithDataSourceTestStepWithStringDataGen() throws Exception {
        TestRecipe testRecipe = recipe {
            dataGenDataSource 'StringDataGenDataSource', {
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
            dataGenDataSource 'CustomStringDataGenDataSource', {
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
            dataGenDataSource 'ShortStateNamesDataGenDataSource', {
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
            dataGenDataSource 'FullStateNamesDataGenDataSource', {
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
        Date startDate = Date.parse("yyyy-MM-dd hh:mm:ss", "2014-04-03 1:23:45")
        Date endDate = Date.parse("yyyy-MM-dd hh:mm:ss", "2016-04-03 23:59:59")

        TestRecipe testRecipe = recipe {
            dataGenDataSource 'FullStateNamesDataGenDataSource', {
                dateAndTimeValues 'DateAndTimes', {
                    startingAt startDate
                    endingAt endDate
                    formatISO_8601
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

    private static DataGenDataSource extractDataGenDataSource(TestRecipe testRecipe) {
        DataSourceTestStep testStep = testRecipe.testCase.testSteps[0] as DataSourceTestStep
        testStep.dataSource.dataGen
    }

    private static void assertDataGenerator(DataGenerator dataGenerator, String type, String propertyName) {
        assert dataGenerator.type == type
        assert dataGenerator.propertyName == propertyName
    }

    private static void assertBooleanDataGen(DataGenerator dataGenerator, String propertyName, FormatEnum format) {
        assertDataGenerator(dataGenerator, 'Boolean', propertyName)
        assert ((BooleanDataGenerator) dataGenerator).format == format
    }
}
