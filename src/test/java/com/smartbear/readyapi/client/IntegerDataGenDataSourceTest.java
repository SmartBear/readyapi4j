package com.smartbear.readyapi.client;

import com.smartbear.readyapi.client.model.DataGenDataSource;
import com.smartbear.readyapi.client.model.DataGenerator;
import com.smartbear.readyapi.client.model.DataSourceTestStep;
import com.smartbear.readyapi.client.model.IntegerDataGenerator;
import com.smartbear.readyapi.client.model.TestStep;
import com.smartbear.readyapi.client.teststeps.TestStepTypes;
import org.junit.Test;

import static com.smartbear.readyapi.client.TestRecipeBuilder.newTestRecipe;
import static com.smartbear.readyapi.client.teststeps.TestSteps.dataGenDataSource;
import static com.smartbear.readyapi.client.teststeps.datasource.datagen.DataGenerators.integerTypeProperty;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class IntegerDataGenDataSourceTest {

    @Test
    public void buildsRecipeWithDataSourceTestStepWithIntegerDataGenDataSourceWithDefaultValues() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                integerTypeProperty("property1")
                        )
                )
                .buildTestRecipe();

        IntegerDataGenerator dataGenerator = (IntegerDataGenerator) getDataGenerator(recipe);
        assertThat(dataGenerator.getType(), is("Integer"));
        assertThat(dataGenerator.getMinimumValue(), is(1));
        assertThat(dataGenerator.getMaximumValue(), is(100));
        assertThat(dataGenerator.getGenrationMode(), is(IntegerDataGenerator.GenrationModeEnum.RANDOM));
    }

    @Test
    public void buildsRecipeWithDataSourceTestStepWithIntegerDataGenDataSourceRandomValues() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                integerTypeProperty("property1")
                                        .withRandomValues()
                                        .withMinimumValue(11)
                                        .withMaximumValue(111)
                        )
                )
                .buildTestRecipe();

        IntegerDataGenerator dataGenerator = (IntegerDataGenerator) getDataGenerator(recipe);
        assertThat(dataGenerator.getType(), is("Integer"));
        assertThat(dataGenerator.getMinimumValue(), is(11));
        assertThat(dataGenerator.getMaximumValue(), is(111));
        assertThat(dataGenerator.getGenrationMode(), is(IntegerDataGenerator.GenrationModeEnum.RANDOM));
    }

    @Test
    public void buildsRecipeWithDataSourceTestStepWithIntegerDataGenDataSourceSequentialValues() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                integerTypeProperty("property1")
                                        .withSequentialValues()
                        )
                )
                .buildTestRecipe();

        IntegerDataGenerator dataGenerator = (IntegerDataGenerator) getDataGenerator(recipe);
        assertThat(dataGenerator.getType(), is("Integer"));
        assertThat(dataGenerator.getGenrationMode(), is(IntegerDataGenerator.GenrationModeEnum.SEQUENTIAL));
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
