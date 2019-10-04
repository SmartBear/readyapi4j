package io.swagger.assert4j.testengine.teststeps.datasource.datagen;

import io.swagger.assert4j.TestRecipe;
import io.swagger.assert4j.client.model.*;
import io.swagger.assert4j.teststeps.TestStepTypes;
import org.junit.Test;

import static io.swagger.assert4j.TestRecipeBuilder.newTestRecipe;
import static io.swagger.assert4j.testengine.teststeps.ServerTestSteps.dataGenDataSource;
import static io.swagger.assert4j.testengine.teststeps.datasource.datagen.DataGenerators.randomRealNumberTypeProperty;
import static io.swagger.assert4j.testengine.teststeps.datasource.datagen.DataGenerators.sequentialRealNumberTypeProperty;
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
        assertThat(dataGenerator.getMinimumValue(), is( (float)1));
        assertThat(dataGenerator.getMaximumValue(), is((float)100));
        assertThat(dataGenerator.getGenerationMode(), is(RealNumberDataGenerator.GenerationModeEnum.RANDOM));
        assertThat(dataGenerator.getDecimalPlaces(), is(2));
    }

    @Test
    public void buildsRecipeWithDataSourceTestStepWithIntegerDataGenDataSourceRandomValues() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                randomRealNumberTypeProperty("property1")
                                        .withMinimumValue((float) 11)
                                        .withMaximumValue((float) 111)
                                        .withDecimalPlaces(4)
                        )
                )
                .buildTestRecipe();

        RealNumberDataGenerator dataGenerator = (RealNumberDataGenerator) getDataGenerator(recipe);
        assertThat(dataGenerator.getType(), is("Real"));
        assertThat(dataGenerator.getMinimumValue(), is((float)11));
        assertThat(dataGenerator.getMaximumValue(), is((float)111));
        assertThat(dataGenerator.getGenerationMode(), is(RealNumberDataGenerator.GenerationModeEnum.RANDOM));
        assertThat(dataGenerator.getDecimalPlaces(), is(4));
    }

    @Test
    public void buildsRecipeWithDataSourceTestStepWithIntegerDataGenDataSourceSequentialValues() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                sequentialRealNumberTypeProperty("property1")
                                        .incrementBy((float) 1.3)
                        )
                )
                .buildTestRecipe();

        RealNumberDataGenerator dataGenerator = (RealNumberDataGenerator) getDataGenerator(recipe);
        assertThat(dataGenerator.getType(), is("Real"));
        assertThat(dataGenerator.getGenerationMode(), is(RealNumberDataGenerator.GenerationModeEnum.SEQUENTIAL));
        assertThat(dataGenerator.getIncrementBy(), is(Float.valueOf((float) 1.3)));
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
