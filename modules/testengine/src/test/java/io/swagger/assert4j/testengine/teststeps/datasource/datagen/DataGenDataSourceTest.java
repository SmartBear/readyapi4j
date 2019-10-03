package io.swagger.assert4j.testengine.teststeps.datasource.datagen;

import io.swagger.assert4j.TestRecipe;
import io.swagger.assert4j.client.model.*;
import io.swagger.assert4j.teststeps.TestStepTypes;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static io.swagger.assert4j.TestRecipeBuilder.newTestRecipe;
import static io.swagger.assert4j.testengine.teststeps.ServerTestSteps.dataGenDataSource;
import static io.swagger.assert4j.testengine.teststeps.datasource.datagen.DataGenerators.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@Ignore
public class DataGenDataSourceTest {

    @Test
    public void buildsRecipeWithDataSourceTestStepWithBooleanDataGenDataSource() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withNumberOfRows(34)
                        .withProperty(
                                yesNoBooleanTypeProperty("property1")
                                        .duplicatedBy(1)
                        )
                )
                .buildTestRecipe();
        DataGenDataSource dataGenDataSource = getDataGenDataSource(recipe);
        BooleanDataGenerator dataGenerator = (BooleanDataGenerator) dataGenDataSource.getDataGenerators().get(0);

        assertThat(dataGenDataSource.getNumberOfRows(), is("34"));
        assertThat(dataGenerator.getType(), is("Boolean"));
        assertThat(dataGenerator.getFormat(), is(BooleanDataGenerator.FormatEnum.YES_NO));
        assertThat(dataGenerator.getDuplicationFactor(), is(1));
        assertThat(dataGenerator.getPropertyName(), is("property1"));
    }

    @Test
    public void buildsRecipeWithDataSourceTestStepWithBooleanDataGenWithDefaultFormat() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                trueFalseBooleanTypeProperty("property1")
                        )
                )
                .buildTestRecipe();

        TestStep testStep = recipe.getTestCase().getTestSteps().get(0);
        assertThat(testStep.getType(), is(TestStepTypes.DATA_SOURCE.getName()));

        DataGenDataSource dataGenDataSource = ((DataSourceTestStep) testStep).getDataSource().getDataGen();
        BooleanDataGenerator dataGenerator = (BooleanDataGenerator) dataGenDataSource.getDataGenerators().get(0);
        assertThat(dataGenerator.getFormat(), is(BooleanDataGenerator.FormatEnum.TRUE_FALSE));
    }

    @Test
    public void buildsRecipeWithDataSourceTestStepWithBooleanDataGenWithDigitsFormat() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                digitsBooleanTypeProperty("property1")
                        )
                )
                .buildTestRecipe();

        TestStep testStep = recipe.getTestCase().getTestSteps().get(0);
        assertThat(testStep.getType(), is(TestStepTypes.DATA_SOURCE.getName()));

        DataGenDataSource dataGenDataSource = ((DataSourceTestStep) testStep).getDataSource().getDataGen();
        BooleanDataGenerator dataGenerator = (BooleanDataGenerator) dataGenDataSource.getDataGenerators().get(0);
        assertThat(dataGenerator.getFormat(), is(BooleanDataGenerator.FormatEnum._1_0));
    }

    @Test
    public void buildsRecipeWithDataSourceTestStepWithCityDataGenDataSource() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withNumberOfRows(13)
                        .withProperty(
                                cityTypeProperty("property1")
                                        .duplicatedBy(7)
                        )
                )
                .buildTestRecipe();

        DataGenDataSource dataGenDataSource = getDataGenDataSource(recipe);
        DataGenerator dataGenerator = dataGenDataSource.getDataGenerators().get(0);

        assertThat(dataGenDataSource.getNumberOfRows(), is("13"));
        assertThat(dataGenerator.getType(), is("City"));
        assertThat(dataGenerator.getDuplicationFactor(), is(7));
        assertThat(dataGenerator.getPropertyName(), is("property1"));
    }

    @Test
    public void buildsRecipeWithDataSourceTestStepWithCountryDataGenDataSource() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                countryTypeProperty("property1")
                        )
                )
                .buildTestRecipe();

        assertThat(getDataGenerator(recipe).getType(), is("Country"));
    }

    @Test
    public void buildsRecipeWithDataSourceTestStepWithMultipleDataGenDataSources() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperties(
                                countryTypeProperty("property1"),
                                cityTypeProperty("property2")
                        )
                )
                .buildTestRecipe();

        DataGenDataSource dataGenDataSource = getDataGenDataSource(recipe);
        List<DataGenerator> dataGenerators = dataGenDataSource.getDataGenerators();
        assertThat(dataGenerators.size(), is(2));
        assertThat(dataGenerators.get(0).getType(), is("Country"));
        assertThat(dataGenerators.get(1).getType(), is("City"));
    }

    @Test
    public void buildsRecipeWithDataSourceTestStepWithStreetAddressDataGenDataSource() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                addressTypeProperty("property1")
                        )
                )
                .buildTestRecipe();

        assertThat(getDataGenerator(recipe).getType(), is("Street Address"));
    }

    @Test
    public void buildsRecipeWithDataSourceTestStepWithEMailDataGenDataSource() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                emailTypeProperty("property1")
                        )
                )
                .buildTestRecipe();

        assertThat(getDataGenerator(recipe).getType(), is("E-Mail"));
    }

    @Test
    public void buildsRecipeWithDataSourceTestStepWithGuidDataGenDataSource() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                guidTypeProperty("property1")
                        )
                )
                .buildTestRecipe();

        assertThat(getDataGenerator(recipe).getType(), is("Guid"));
    }

    @Test
    public void buildsRecipeWithDataSourceTestStepWithSSNDataGenDataSource() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                ssnTypeProperty("property1")
                        )
                )
                .buildTestRecipe();

        assertThat(getDataGenerator(recipe).getType(), is("Social Security Number"));
    }

    @Test
    public void buildsRecipeWithDataSourceTestStepWithComputerAddressDataGenWithDefaultFormat() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                ipv4ComputerAddressTypeProperty("property1")
                        )
                )
                .buildTestRecipe();

        ComputerAddressDataGenerator dataGenerator = (ComputerAddressDataGenerator) getDataGenerator(recipe);
        assertThat(dataGenerator.getType(), is("Computer Address"));
        assertThat(dataGenerator.getAddressType(), is(ComputerAddressDataGenerator.AddressTypeEnum.IPV4));
    }

    @Test
    public void buildsRecipeWithDataSourceTestStepWithComputerAddressDataGenWithIPv4Format() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                ipv4ComputerAddressTypeProperty("property1")
                        )
                )
                .buildTestRecipe();

        ComputerAddressDataGenerator dataGenerator = (ComputerAddressDataGenerator) getDataGenerator(recipe);
        assertThat(dataGenerator.getType(), is("Computer Address"));
        assertThat(dataGenerator.getAddressType(), is(ComputerAddressDataGenerator.AddressTypeEnum.IPV4));
    }

    @Test
    public void buildsRecipeWithDataSourceTestStepWithComputerAddressDataGenWithMac48Format() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                mac48ComputerAddressTypeProperty("property1")
                        )
                )
                .buildTestRecipe();

        ComputerAddressDataGenerator dataGenerator = (ComputerAddressDataGenerator) getDataGenerator(recipe);
        assertThat(dataGenerator.getType(), is("Computer Address"));
        assertThat(dataGenerator.getAddressType(), is(ComputerAddressDataGenerator.AddressTypeEnum.MAC48));
    }

    @Test
    public void buildsRecipeWithDataSourceTestStepWithStateNameDataGenWithFullNames() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                fullStateNameTypeProperty("property1")
                        )
                )
                .buildTestRecipe();

        StateNameDataGenerator dataGenerator = (StateNameDataGenerator) getDataGenerator(recipe);
        assertThat(dataGenerator.getType(), is("State"));
        assertThat(dataGenerator.getNameFormat(), is(StateNameDataGenerator.NameFormatEnum.FULL));
    }

    @Test
    public void buildsRecipeWithDataSourceTestStepWithStateNameDataGenWithAbbreviatedNames() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                shortStateNameTypeProperty("property1")
                        )
                )
                .buildTestRecipe();

        StateNameDataGenerator dataGenerator = (StateNameDataGenerator) getDataGenerator(recipe);
        assertThat(dataGenerator.getType(), is("State"));
        assertThat(dataGenerator.getNameFormat(), is(StateNameDataGenerator.NameFormatEnum.ABBREVIATED));
    }

    @Test
    public void buildsRecipeWithDataSourceTestStepWithCustomStringDataGen() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                customStringTypeProperty("property1")
                                        .withValue("Custom Value")
                        )
                )
                .buildTestRecipe();

        CustomStringDataGenerator dataGenerator = (CustomStringDataGenerator) getDataGenerator(recipe);
        assertThat(dataGenerator.getType(), is("Custom String"));
        assertThat(dataGenerator.getValue(), is("Custom Value"));
    }

    @Test
    public void buildsRecipeWithDataSourceTestStepWithStringDataGenWithDefaultValues() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                stringTypeProperty("property1")
                        )
                )
                .buildTestRecipe();

        StringDataGenerator dataGenerator = (StringDataGenerator) getDataGenerator(recipe);
        assertThat(dataGenerator.getType(), is("String"));
        assertStringDataGen(dataGenerator, 5, 10, true);
    }

    @Test
    public void buildsRecipeWithDataSourceTestStepWithStringDataGen() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                stringTypeProperty("property1")
                                        .withMinimumCharacters(3)
                                        .withMaximumCharacters(15)
                                        .withoutLetters()
                                        .withoutDigits()
                                        .withoutSpaces()
                                        .withoutPunctuationMarks()
                        )
                )
                .buildTestRecipe();

        StringDataGenerator dataGenerator = (StringDataGenerator) getDataGenerator(recipe);
        assertThat(dataGenerator.getType(), is("String"));
        assertStringDataGen(dataGenerator, 3, 15, false);
    }

    private void assertStringDataGen(StringDataGenerator dataGenerator, int minChars, int maxChars, boolean booleanValues) {
        assertThat(dataGenerator.getMinimumCharacters(), is(minChars));
        assertThat(dataGenerator.getMaximumCharacters(), is(maxChars));
        assertThat(dataGenerator.isUseLetters(), is(booleanValues));
        assertThat(dataGenerator.isUseDigits(), is(booleanValues));
        assertThat(dataGenerator.isUseSpaces(), is(booleanValues));
        assertThat(dataGenerator.isUsePunctuationMarks(), is(booleanValues));
    }


    private DataGenDataSource getDataGenDataSource(TestRecipe recipe) {
        TestStep testStep = recipe.getTestCase().getTestSteps().get(0);
        assertThat(testStep.getType(), is(TestStepTypes.DATA_SOURCE.getName()));

        return ((DataSourceTestStep) testStep).getDataSource().getDataGen();
    }

    private DataGenerator getDataGenerator(TestRecipe recipe) {
        DataGenDataSource dataGenDataSource = getDataGenDataSource(recipe);
        return dataGenDataSource.getDataGenerators().get(0);
    }
}
