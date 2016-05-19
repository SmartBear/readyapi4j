package com.smartbear.readyapi.client;

import com.smartbear.readyapi.client.model.DataGenDataSource;
import com.smartbear.readyapi.client.model.DataGenerator;
import com.smartbear.readyapi.client.model.DataSourceTestStep;
import com.smartbear.readyapi.client.model.TestStep;
import com.smartbear.readyapi.client.model.USZIPCodeDataGenerator;
import com.smartbear.readyapi.client.teststeps.TestStepTypes;
import org.junit.Test;

import static com.smartbear.readyapi.client.TestRecipeBuilder.newTestRecipe;
import static com.smartbear.readyapi.client.teststeps.TestSteps.dataGenDataSource;
import static com.smartbear.readyapi.client.teststeps.datasource.datagen.DataGenerators.usZipCodeTypeProperty;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class USZipCodeDataGenDataSourceTest {

    @Test
    public void buildsRecipeWithDataSourceTestStepWithIntegerDataGenDataSourceWithDefaultValues() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                usZipCodeTypeProperty("property1")
                        )
                )
                .buildTestRecipe();

        USZIPCodeDataGenerator dataGenerator = (USZIPCodeDataGenerator) getDataGenerator(recipe);
        assertThat(dataGenerator.getType(), is("United States ZIP Code"));
        assertThat(dataGenerator.getCodeFormat(), is(USZIPCodeDataGenerator.CodeFormatEnum.ALL));
    }

    @Test
    public void buildsRecipeWithDataSourceTestStepWithIntegerDataGenDataSourceWithFormatAll() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                usZipCodeTypeProperty("property1")
                                        .withFormatAll()
                        )
                )
                .buildTestRecipe();

        USZIPCodeDataGenerator dataGenerator = (USZIPCodeDataGenerator) getDataGenerator(recipe);
        assertThat(dataGenerator.getType(), is("United States ZIP Code"));
        assertThat(dataGenerator.getCodeFormat(), is(USZIPCodeDataGenerator.CodeFormatEnum.ALL));
    }

    @Test
    public void buildsRecipeWithDataSourceTestStepWithIntegerDataGenDataSourceWithFormatXXXXX() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                usZipCodeTypeProperty("property1")
                                        .withFormatXXXXX()
                        )
                )
                .buildTestRecipe();

        USZIPCodeDataGenerator dataGenerator = (USZIPCodeDataGenerator) getDataGenerator(recipe);
        assertThat(dataGenerator.getType(), is("United States ZIP Code"));
        assertThat(dataGenerator.getCodeFormat(), is(USZIPCodeDataGenerator.CodeFormatEnum.XXXXX));
    }

    @Test
    public void buildsRecipeWithDataSourceTestStepWithIntegerDataGenDataSourceWithFormatXXXXX_XXXX() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                usZipCodeTypeProperty("property1")
                                        .withFormatXXXXX_XXXX()
                        )
                )
                .buildTestRecipe();

        USZIPCodeDataGenerator dataGenerator = (USZIPCodeDataGenerator) getDataGenerator(recipe);
        assertThat(dataGenerator.getType(), is("United States ZIP Code"));
        assertThat(dataGenerator.getCodeFormat(), is(USZIPCodeDataGenerator.CodeFormatEnum.XXXXX_XXXX));
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
