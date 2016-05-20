package com.smartbear.readyapi.client;

import com.smartbear.readyapi.client.model.DataGenDataSource;
import com.smartbear.readyapi.client.model.DataGenerator;
import com.smartbear.readyapi.client.model.DataSourceTestStep;
import com.smartbear.readyapi.client.model.TestStep;
import com.smartbear.readyapi.client.model.ValuesFromSetDataGenerator;
import com.smartbear.readyapi.client.teststeps.TestStepTypes;
import org.junit.Test;

import java.util.Arrays;

import static com.smartbear.readyapi.client.TestRecipeBuilder.newTestRecipe;
import static com.smartbear.readyapi.client.teststeps.TestSteps.dataGenDataSource;
import static com.smartbear.readyapi.client.teststeps.datasource.datagen.DataGenerators.randomValueFromSetTypeProperty;
import static com.smartbear.readyapi.client.teststeps.datasource.datagen.DataGenerators.sequentialValueFromSetTypeProperty;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ValuesFromSetDataGenDataSourceTest {

    @Test
    public void buildsRecipeWithDataSourceTestStepWithValueFromSetDataGenDataSourceWithDefaultValues() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                randomValueFromSetTypeProperty("property1")
                        )
                )
                .buildTestRecipe();

        ValuesFromSetDataGenerator dataGenerator = (ValuesFromSetDataGenerator) getDataGenerator(recipe);
        assertThat(dataGenerator.getType(), is("Value from Set"));
        assertThat(dataGenerator.getGenrationMode(), is(ValuesFromSetDataGenerator.GenrationModeEnum.RANDOM));
        assertThat(dataGenerator.getValues().size(), is(0));
    }

    @Test
    public void buildsRecipeWithDataSourceTestStepWithValueFromSetDataGenDataSourceWithListValues() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                sequentialValueFromSetTypeProperty("property1")
                                        .withValues(Arrays.asList("Value 1", "Value 2"))
                        )
                )
                .buildTestRecipe();

        ValuesFromSetDataGenerator dataGenerator = (ValuesFromSetDataGenerator) getDataGenerator(recipe);
        assertThat(dataGenerator.getType(), is("Value from Set"));
        assertThat(dataGenerator.getGenrationMode(), is(ValuesFromSetDataGenerator.GenrationModeEnum.SEQUENTIAL));
        assertThat(dataGenerator.getValues().size(), is(2));
    }

    @Test
    public void buildsRecipeWithDataSourceTestStepWithValueFromSetDataGenDataSourceWithArrayValues() throws
            Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                randomValueFromSetTypeProperty("property1")
                                        .withValues("Value 1", "Value 2")
                        )
                )
                .buildTestRecipe();

        ValuesFromSetDataGenerator dataGenerator = (ValuesFromSetDataGenerator) getDataGenerator(recipe);
        assertThat(dataGenerator.getType(), is("Value from Set"));
        assertThat(dataGenerator.getGenrationMode(), is(ValuesFromSetDataGenerator.GenrationModeEnum.RANDOM));
        assertThat(dataGenerator.getValues().size(), is(2));
    }

    @Test
    public void buildsRecipeWithDataSourceTestStepWithValueFromSetDataGenDataSourceWithAddedValues() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                randomValueFromSetTypeProperty("property1")
                                        .addValue("Value 1")
                                        .addValue("Value 2")
                        )
                )
                .buildTestRecipe();

        ValuesFromSetDataGenerator dataGenerator = (ValuesFromSetDataGenerator) getDataGenerator(recipe);
        assertThat(dataGenerator.getType(), is("Value from Set"));
        assertThat(dataGenerator.getValues().size(), is(2));
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
