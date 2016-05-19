package com.smartbear.readyapi.client;

import com.smartbear.readyapi.client.model.DataGenDataSource;
import com.smartbear.readyapi.client.model.DataGenerator;
import com.smartbear.readyapi.client.model.DataSourceTestStep;
import com.smartbear.readyapi.client.model.RealNumberDataGenerator;
import com.smartbear.readyapi.client.model.TestStep;
import com.smartbear.readyapi.client.teststeps.TestStepTypes;
import org.junit.Test;

import java.math.BigDecimal;

import static com.smartbear.readyapi.client.TestRecipeBuilder.newTestRecipe;
import static com.smartbear.readyapi.client.teststeps.TestSteps.dataGenDataSource;
import static com.smartbear.readyapi.client.teststeps.datasource.datagen.DataGenerators.realNumberTypeProperty;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class RealNumberDataGenDataSourceTest {

    @Test
    public void buildsRecipeWithDataSourceTestStepWithRealNumberDataGenDataSourceWithDefaultValues() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                realNumberTypeProperty("property1")
                        )
                )
                .buildTestRecipe();

        RealNumberDataGenerator dataGenerator = (RealNumberDataGenerator) getDataGenerator(recipe);
        assertThat(dataGenerator.getType(), is("Real"));
        assertThat(dataGenerator.getMinimumValue(), is(new BigDecimal(1)));
        assertThat(dataGenerator.getMaximumValue(), is(new BigDecimal(100)));
        assertThat(dataGenerator.getGenrationMode(), is(RealNumberDataGenerator.GenrationModeEnum.RANDOM));
        assertThat(dataGenerator.getDecimalPlaces(), is(2));
    }

    @Test
    public void buildsRecipeWithDataSourceTestStepWithIntegerDataGenDataSourceRandomValues() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                realNumberTypeProperty("property1")
                                        .withRandomValues()
                                        .withMinimumValue(new BigDecimal(11))
                                        .withMaximumValue(new BigDecimal(111))
                                        .withDecimalPlaces(4)
                        )
                )
                .buildTestRecipe();

        RealNumberDataGenerator dataGenerator = (RealNumberDataGenerator) getDataGenerator(recipe);
        assertThat(dataGenerator.getType(), is("Real"));
        assertThat(dataGenerator.getMinimumValue(), is(new BigDecimal(11)));
        assertThat(dataGenerator.getMaximumValue(), is(new BigDecimal(111)));
        assertThat(dataGenerator.getGenrationMode(), is(RealNumberDataGenerator.GenrationModeEnum.RANDOM));
        assertThat(dataGenerator.getDecimalPlaces(), is(4));
    }

    @Test
    public void buildsRecipeWithDataSourceTestStepWithIntegerDataGenDataSourceSequentialValues() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                realNumberTypeProperty("property1")
                                        .withSequentialValues()
                        )
                )
                .buildTestRecipe();

        RealNumberDataGenerator dataGenerator = (RealNumberDataGenerator) getDataGenerator(recipe);
        assertThat(dataGenerator.getType(), is("Real"));
        assertThat(dataGenerator.getGenrationMode(), is(RealNumberDataGenerator.GenrationModeEnum.SEQUENTIAL));
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
