package com.smartbear.readyapi4j.testserver.teststeps.datasource.datagen;

import com.smartbear.readyapi.client.model.DataGenDataSource;
import com.smartbear.readyapi.client.model.DataGenerator;
import com.smartbear.readyapi.client.model.DataSourceTestStep;
import com.smartbear.readyapi.client.model.RealNumberDataGenerator;
import com.smartbear.readyapi.client.model.TestStep;
import com.smartbear.readyapi4j.TestRecipe;
import com.smartbear.readyapi4j.teststeps.TestStepTypes;
import org.junit.Test;

import java.math.BigDecimal;

import static com.smartbear.readyapi4j.TestRecipeBuilder.newTestRecipe;
import static com.smartbear.readyapi4j.testserver.teststeps.ServerTestSteps.dataGenDataSource;
import static com.smartbear.readyapi4j.testserver.teststeps.datasource.datagen.DataGenerators.randomRealNumberTypeProperty;
import static com.smartbear.readyapi4j.testserver.teststeps.datasource.datagen.DataGenerators.sequentialRealNumberTypeProperty;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class RealNumberDataGenDataSourceTest {

    @Test
    public void buildsRecipeWithDataSourceTestStepWithRealNumberDataGenDataSourceWithDefaultValues() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                randomRealNumberTypeProperty("property1")
                        )
                )
                .buildTestRecipe();

        RealNumberDataGenerator dataGenerator = (RealNumberDataGenerator) getDataGenerator(recipe);
        assertThat(dataGenerator.getType(), is("Real"));
        assertThat(dataGenerator.getMinimumValue(), is(new BigDecimal(1)));
        assertThat(dataGenerator.getMaximumValue(), is(new BigDecimal(100)));
        assertThat(dataGenerator.getGenerationMode(), is(RealNumberDataGenerator.GenerationModeEnum.RANDOM));
        assertThat(dataGenerator.getDecimalPlaces(), is(2));
    }

    @Test
    public void buildsRecipeWithDataSourceTestStepWithIntegerDataGenDataSourceRandomValues() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                randomRealNumberTypeProperty("property1")
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
        assertThat(dataGenerator.getGenerationMode(), is(RealNumberDataGenerator.GenerationModeEnum.RANDOM));
        assertThat(dataGenerator.getDecimalPlaces(), is(4));
    }

    @Test
    public void buildsRecipeWithDataSourceTestStepWithIntegerDataGenDataSourceSequentialValues() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                sequentialRealNumberTypeProperty("property1")
                                        .incrementBy(1.3)
                        )
                )
                .buildTestRecipe();

        RealNumberDataGenerator dataGenerator = (RealNumberDataGenerator) getDataGenerator(recipe);
        assertThat(dataGenerator.getType(), is("Real"));
        assertThat(dataGenerator.getGenerationMode(), is(RealNumberDataGenerator.GenerationModeEnum.SEQUENTIAL));
        assertThat(dataGenerator.getIncrementBy(), is(new BigDecimal(1.3)));
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
