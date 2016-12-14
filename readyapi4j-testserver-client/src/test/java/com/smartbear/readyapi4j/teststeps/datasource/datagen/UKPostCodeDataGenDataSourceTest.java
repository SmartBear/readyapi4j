package com.smartbear.readyapi4j.teststeps.datasource.datagen;

import com.smartbear.readyapi.client.model.DataGenDataSource;
import com.smartbear.readyapi.client.model.DataGenerator;
import com.smartbear.readyapi.client.model.DataSourceTestStep;
import com.smartbear.readyapi.client.model.TestStep;
import com.smartbear.readyapi.client.model.UKPostCodeDataGenerator;
import com.smartbear.readyapi4j.TestRecipe;
import com.smartbear.readyapi4j.teststeps.TestStepTypes;
import org.junit.Test;

import static com.smartbear.readyapi4j.TestRecipeBuilder.newTestRecipe;
import static com.smartbear.readyapi4j.teststeps.ServerTestSteps.dataGenDataSource;
import static com.smartbear.readyapi4j.teststeps.datasource.datagen.DataGenerators.ukPostCodeTypeProperty;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class UKPostCodeDataGenDataSourceTest {

    @Test
    public void buildsRecipeWithDataSourceTestStepWithIntegerDataGenDataSourceWithDefaultValues() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                ukPostCodeTypeProperty("property1")
                        )
                )
                .buildTestRecipe();

        UKPostCodeDataGenerator dataGenerator = (UKPostCodeDataGenerator) getDataGenerator(recipe);
        assertThat(dataGenerator.getType(), is("United Kingdom Postcode"));
        assertThat(dataGenerator.getCodeFormat(), is(UKPostCodeDataGenerator.CodeFormatEnum.ALL));
    }

    @Test
    public void buildsRecipeWithDataSourceTestStepWithIntegerDataGenDataSourceWithFormatA9_9AA() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                ukPostCodeTypeProperty("property1")
                                        .withFormatA9_9AA()
                        )
                )
                .buildTestRecipe();

        UKPostCodeDataGenerator dataGenerator = (UKPostCodeDataGenerator) getDataGenerator(recipe);
        assertThat(dataGenerator.getType(), is("United Kingdom Postcode"));
        assertThat(dataGenerator.getCodeFormat(), is(UKPostCodeDataGenerator.CodeFormatEnum.A9_9AA));
    }

    @Test
    public void buildsRecipeWithDataSourceTestStepWithIntegerDataGenDataSourceWithFormatA99_9AA() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                ukPostCodeTypeProperty("property1")
                                        .withFormatA99_9AA()
                        )
                )
                .buildTestRecipe();

        UKPostCodeDataGenerator dataGenerator = (UKPostCodeDataGenerator) getDataGenerator(recipe);
        assertThat(dataGenerator.getType(), is("United Kingdom Postcode"));
        assertThat(dataGenerator.getCodeFormat(), is(UKPostCodeDataGenerator.CodeFormatEnum.A99_9AA));
    }

    @Test
    public void buildsRecipeWithDataSourceTestStepWithIntegerDataGenDataSourceWithFormatAA9_9AA() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                ukPostCodeTypeProperty("property1")
                                        .withFormatAA9_9AA()
                        )
                )
                .buildTestRecipe();

        UKPostCodeDataGenerator dataGenerator = (UKPostCodeDataGenerator) getDataGenerator(recipe);
        assertThat(dataGenerator.getType(), is("United Kingdom Postcode"));
        assertThat(dataGenerator.getCodeFormat(), is(UKPostCodeDataGenerator.CodeFormatEnum.AA9_9AA));
    }

    @Test
    public void buildsRecipeWithDataSourceTestStepWithIntegerDataGenDataSourceWithFormatA9A_9AA() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                ukPostCodeTypeProperty("property1")
                                        .withFormatA9A_9AA()
                        )
                )
                .buildTestRecipe();

        UKPostCodeDataGenerator dataGenerator = (UKPostCodeDataGenerator) getDataGenerator(recipe);
        assertThat(dataGenerator.getType(), is("United Kingdom Postcode"));
        assertThat(dataGenerator.getCodeFormat(), is(UKPostCodeDataGenerator.CodeFormatEnum.A9A_9AA));
    }

    @Test
    public void buildsRecipeWithDataSourceTestStepWithIntegerDataGenDataSourceWithFormatAA99_9AA() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                ukPostCodeTypeProperty("property1")
                                        .withFormatAA99_9AA()
                        )
                )
                .buildTestRecipe();

        UKPostCodeDataGenerator dataGenerator = (UKPostCodeDataGenerator) getDataGenerator(recipe);
        assertThat(dataGenerator.getType(), is("United Kingdom Postcode"));
        assertThat(dataGenerator.getCodeFormat(), is(UKPostCodeDataGenerator.CodeFormatEnum.AA99_9AA));
    }

    @Test
    public void buildsRecipeWithDataSourceTestStepWithIntegerDataGenDataSourceWithFormatAA9A_9AA() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                ukPostCodeTypeProperty("property1")
                                        .withFormatAA9A_9AA()
                        )
                )
                .buildTestRecipe();

        UKPostCodeDataGenerator dataGenerator = (UKPostCodeDataGenerator) getDataGenerator(recipe);
        assertThat(dataGenerator.getType(), is("United Kingdom Postcode"));
        assertThat(dataGenerator.getCodeFormat(), is(UKPostCodeDataGenerator.CodeFormatEnum.AA9A_9AA));
    }


    @Test
    public void buildsRecipeWithDataSourceTestStepWithIntegerDataGenDataSourceWithFormatAll() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                ukPostCodeTypeProperty("property1")
                                        .withFormatAll()
                        )
                )
                .buildTestRecipe();

        UKPostCodeDataGenerator dataGenerator = (UKPostCodeDataGenerator) getDataGenerator(recipe);
        assertThat(dataGenerator.getType(), is("United Kingdom Postcode"));
        assertThat(dataGenerator.getCodeFormat(), is(UKPostCodeDataGenerator.CodeFormatEnum.ALL));
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
