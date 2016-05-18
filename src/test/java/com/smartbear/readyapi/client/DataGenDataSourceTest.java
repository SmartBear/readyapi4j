package com.smartbear.readyapi.client;

import com.smartbear.readyapi.client.model.BooleanDataGenerator;
import com.smartbear.readyapi.client.model.DataGenDataSource;
import com.smartbear.readyapi.client.model.DataGenerator;
import com.smartbear.readyapi.client.model.DataSourceTestStep;
import com.smartbear.readyapi.client.model.TestStep;
import com.smartbear.readyapi.client.teststeps.TestStepTypes;
import com.smartbear.readyapi.client.teststeps.datasource.datagen.BooleanDataGeneratorBuilder;
import org.junit.Test;

import static com.smartbear.readyapi.client.TestRecipeBuilder.newTestRecipe;
import static com.smartbear.readyapi.client.teststeps.TestSteps.dataGenDataSource;
import static com.smartbear.readyapi.client.teststeps.datasource.datagen.DataGenerators.addressTypeProperty;
import static com.smartbear.readyapi.client.teststeps.datasource.datagen.DataGenerators.booleanDataGenerator;
import static com.smartbear.readyapi.client.teststeps.datasource.datagen.DataGenerators.cityTypeProperty;
import static com.smartbear.readyapi.client.teststeps.datasource.datagen.DataGenerators.countryTypeProperty;
import static com.smartbear.readyapi.client.teststeps.datasource.datagen.DataGenerators.emailTypeProperty;
import static com.smartbear.readyapi.client.teststeps.datasource.datagen.DataGenerators.guidTypeProperty;
import static com.smartbear.readyapi.client.teststeps.datasource.datagen.DataGenerators.ssnTypeProperty;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DataGenDataSourceTest {

    @Test
    public void buildsRecipeWithDataSourceTestStepWithBooleanDataGenDataSource() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withNumberOfRows(34)
                        .addDataGenerator(
                                booleanTypeProperty("property1")
                                        .duplicatedBy(1)
                                        .withYesNoFormat()
                        )
                )
                .buildTestRecipe();

        DataGenDataSource dataGenDataSource = getDataGenDataSource(recipe);
        BooleanDataGenerator dataGenerator = (BooleanDataGenerator) dataGenDataSource.getDataGenerators().get(0);

        assertThat(dataGenDataSource.getNumberOfRows(), is(34));
        assertThat(dataGenerator.getType(), is("Boolean"));
        assertThat(dataGenerator.getFormat(), is(BooleanDataGenerator.FormatEnum.YES_NO));
        assertThat(dataGenerator.getDuplicationFactor(), is(1));
        assertThat(dataGenerator.getPropertyName(), is("property1"));
    }

    @Test
    public void buildsRecipeWithDataSourceTestStepWithBooleanDataGenWithDefaultFormat() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .addDataGenerator(
                                booleanTypeProperty("property1")
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
                        .addDataGenerator(
                                booleanTypeProperty("property1").withDigitsFormat()
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
                        .addDataGenerator(
                                cityTypeProperty("property1")
                                        .duplicatedBy(7)
                        )
                )
                .buildTestRecipe();

        DataGenDataSource dataGenDataSource = getDataGenDataSource(recipe);
        DataGenerator dataGenerator = dataGenDataSource.getDataGenerators().get(0);

        assertThat(dataGenDataSource.getNumberOfRows(), is(13));
        assertThat(dataGenerator.getType(), is("City"));
        assertThat(dataGenerator.getDuplicationFactor(), is(7));
        assertThat(dataGenerator.getPropertyName(), is("property1"));
    }

    @Test
    public void buildsRecipeWithDataSourceTestStepWithCountryDataGenDataSource() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .addDataGenerator(
                                countryTypeProperty("property1")
                        )
                )
                .buildTestRecipe();

        assertThat(getDataGenerator(recipe).getType(), is("Country"));
    }

    @Test
    public void buildsRecipeWithDataSourceTestStepWithStreetAddressDataGenDataSource() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .addDataGenerator(
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
                        .addDataGenerator(
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
                        .addDataGenerator(
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
                        .addDataGenerator(
                                ssnTypeProperty("property1")
                        )
                )
                .buildTestRecipe();

        assertThat(getDataGenerator(recipe).getType(), is("Social Security Number"));
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
