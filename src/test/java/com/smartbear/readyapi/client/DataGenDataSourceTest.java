package com.smartbear.readyapi.client;

import com.smartbear.readyapi.client.model.BooleanDataGenerator;
import com.smartbear.readyapi.client.model.DataGenDataSource;
import com.smartbear.readyapi.client.model.DataSourceTestStep;
import com.smartbear.readyapi.client.model.TestStep;
import com.smartbear.readyapi.client.teststeps.TestStepTypes;
import com.smartbear.readyapi.client.teststeps.datasource.datagen.BooleanDataGeneratorBuilder;
import org.junit.Test;

import static com.smartbear.readyapi.client.TestRecipeBuilder.newTestRecipe;
import static com.smartbear.readyapi.client.teststeps.TestSteps.dataGenDataSource;
import static com.smartbear.readyapi.client.teststeps.datasource.datagen.DataGenerators.booleanDataGenerator;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DataGenDataSourceTest {

    @Test
    public void buildsRecipeWithDataSourceTestStepWithBooleanDataGenDataSource() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .setNumberOfRows(34)
                        .addDataGenerator(
                                booleanDataGenerator("property1")
                                        .setDuplicationFactor(1)
                                        .setFormat(BooleanDataGeneratorBuilder.Format.YES_NO)
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

}
