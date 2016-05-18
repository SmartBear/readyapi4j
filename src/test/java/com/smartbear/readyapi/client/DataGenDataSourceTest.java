package com.smartbear.readyapi.client;

import com.smartbear.readyapi.client.model.BooleanDataGenerator;
import com.smartbear.readyapi.client.model.DataGenDataSource;
import com.smartbear.readyapi.client.model.DataSourceTestStep;
import com.smartbear.readyapi.client.model.TestStep;
import com.smartbear.readyapi.client.teststeps.TestStepTypes;
import org.junit.Test;

import static com.smartbear.readyapi.client.TestRecipeBuilder.newTestRecipe;
import static com.smartbear.readyapi.client.teststeps.TestSteps.dataGenDataSource;
import static com.smartbear.readyapi.client.teststeps.datasource.datagen.DataGenerators.booleanTypeProperty;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DataGenDataSourceTest {

    @Test
    public void buildsRecipeWithDataSourceTestStepWithBooleanDataGenDataSource() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withNumberOfRows(34)
                        .withProperty(
                                booleanTypeProperty("property1")
                                        .duplicatedBy(1)
                                        .withYesNoFormat()
                        )
                )
                .buildTestRecipe();

        TestStep testStep = recipe.getTestCase().getTestSteps().get(0);
        assertThat(testStep.getType(), is(TestStepTypes.DATA_SOURCE.getName()));

        DataGenDataSource dataGenDataSource = ((DataSourceTestStep) testStep).getDataSource().getDataGen();
        assertThat(dataGenDataSource.getNumberOfRows(), is(34));
        BooleanDataGenerator dataGenerator = (BooleanDataGenerator) dataGenDataSource.getDataGenerators().get(0);
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
                        .withProperty(
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

}
