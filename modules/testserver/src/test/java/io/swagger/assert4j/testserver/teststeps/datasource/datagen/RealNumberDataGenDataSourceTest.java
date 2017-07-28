package io.swagger.assert4j.testserver.teststeps.datasource.datagen;

import io.swagger.assert4j.client.model.DataGenDataSource;
import io.swagger.assert4j.client.model.DataGenerator;
import io.swagger.assert4j.client.model.DataSourceTestStep;
import io.swagger.assert4j.client.model.RealNumberDataGenerator;
import io.swagger.assert4j.client.model.TestStep;
import io.swagger.assert4j.TestRecipe;
import io.swagger.assert4j.teststeps.TestStepTypes;
import org.junit.Test;

import java.math.BigDecimal;

import static io.swagger.assert4j.TestRecipeBuilder.newTestRecipe;
import static io.swagger.assert4j.testserver.teststeps.ServerTestSteps.dataGenDataSource;
import static io.swagger.assert4j.testserver.teststeps.datasource.datagen.DataGenerators.randomRealNumberTypeProperty;
import static io.swagger.assert4j.testserver.teststeps.datasource.datagen.DataGenerators.sequentialRealNumberTypeProperty;
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
        assertThat(dataGenerator.getIncrementBy(), is(BigDecimal.valueOf(1.3)));
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
