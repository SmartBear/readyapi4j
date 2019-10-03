package io.swagger.assert4j.testengine.teststeps.datasource.datagen;

import io.swagger.assert4j.TestRecipe;
import io.swagger.assert4j.client.model.*;
import io.swagger.assert4j.teststeps.TestStepTypes;
import org.junit.Ignore;
import org.junit.Test;

import static io.swagger.assert4j.TestRecipeBuilder.newTestRecipe;
import static io.swagger.assert4j.testengine.teststeps.ServerTestSteps.dataGenDataSource;
import static io.swagger.assert4j.testengine.teststeps.datasource.datagen.DataGenerators.randomIntegerTypeProperty;
import static io.swagger.assert4j.testengine.teststeps.datasource.datagen.DataGenerators.sequentialIntegerTypeProperty;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@Ignore
public class IntegerDataGenDataSourceTest {

    @Test
    public void buildsRecipeWithDataSourceTestStepWithIntegerDataGenDataSourceWithDefaultValues() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                randomIntegerTypeProperty("property1")
                        )
                )
                .buildTestRecipe();

        IntegerDataGenerator dataGenerator = (IntegerDataGenerator) getDataGenerator(recipe);
        assertThat(dataGenerator.getType(), is("Integer"));
        assertThat(dataGenerator.getMinimumValue(), is(1));
        assertThat(dataGenerator.getMaximumValue(), is(100));
        assertThat(dataGenerator.getGenerationMode(), is(IntegerDataGenerator.GenerationModeEnum.RANDOM));
    }

    @Test
    public void buildsRecipeWithDataSourceTestStepWithIntegerDataGenDataSourceRandomValues() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                randomIntegerTypeProperty("property1")
                                        .withMinimumValue(11)
                                        .withMaximumValue(111)
                        )
                )
                .buildTestRecipe();

        IntegerDataGenerator dataGenerator = (IntegerDataGenerator) getDataGenerator(recipe);
        assertThat(dataGenerator.getType(), is("Integer"));
        assertThat(dataGenerator.getMinimumValue(), is(11));
        assertThat(dataGenerator.getMaximumValue(), is(111));
        assertThat(dataGenerator.getGenerationMode(), is(IntegerDataGenerator.GenerationModeEnum.RANDOM));
    }

    @Test
    public void buildsRecipeWithDataSourceTestStepWithIntegerDataGenDataSourceSequentialValues() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                sequentialIntegerTypeProperty("property1")
                                        .incrementBy(2)
                        )
                )
                .buildTestRecipe();

        IntegerDataGenerator dataGenerator = (IntegerDataGenerator) getDataGenerator(recipe);
        assertThat(dataGenerator.getType(), is("Integer"));
        assertThat(dataGenerator.getGenerationMode(), is(IntegerDataGenerator.GenerationModeEnum.SEQUENTIAL));
        assertThat(dataGenerator.getIncrementBy(), is(2));
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
