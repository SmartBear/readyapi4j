package io.swagger.assert4j.testserver.teststeps.datasource.datagen;

import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import io.swagger.assert4j.client.model.DataGenDataSource;
import io.swagger.assert4j.client.model.DataGenerator;
import io.swagger.assert4j.client.model.DataSourceTestStep;
import io.swagger.assert4j.client.model.DateAndTimeDataGenerator;
import io.swagger.assert4j.client.model.TestStep;
import io.swagger.assert4j.TestRecipe;
import io.swagger.assert4j.teststeps.TestStepTypes;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.util.Date;

import static io.swagger.assert4j.client.model.DateAndTimeDataGenerator.DateTimeFormatEnum.DAYOFWEEK_D_MONTH_YYYY;
import static io.swagger.assert4j.client.model.DateAndTimeDataGenerator.DateTimeFormatEnum.D_MONTH_YYYY;
import static io.swagger.assert4j.client.model.DateAndTimeDataGenerator.DateTimeFormatEnum.HH_MM_24_HOUR_;
import static io.swagger.assert4j.client.model.DateAndTimeDataGenerator.DateTimeFormatEnum.HH_MM_AM_PM;
import static io.swagger.assert4j.client.model.DateAndTimeDataGenerator.DateTimeFormatEnum.HH_MM_SS_24_HOUR_;
import static io.swagger.assert4j.client.model.DateAndTimeDataGenerator.DateTimeFormatEnum.HH_MM_SS_AM_PM;
import static io.swagger.assert4j.client.model.DateAndTimeDataGenerator.DateTimeFormatEnum.M_D_YYYY;
import static io.swagger.assert4j.client.model.DateAndTimeDataGenerator.DateTimeFormatEnum.M_D_YYYY_HH_MM_SS_24_HOUR_;
import static io.swagger.assert4j.client.model.DateAndTimeDataGenerator.DateTimeFormatEnum.M_D_YYYY_HH_MM_SS_AM_PM;
import static io.swagger.assert4j.client.model.DateAndTimeDataGenerator.DateTimeFormatEnum.YYYY_MM_DDTHH_MM_SSZ_ISO_8601_;
import static io.swagger.assert4j.TestRecipeBuilder.newTestRecipe;
import static io.swagger.assert4j.testserver.teststeps.ServerTestSteps.dataGenDataSource;
import static io.swagger.assert4j.testserver.teststeps.datasource.datagen.DataGenerators.randomDateAndTimeTypeProperty;
import static io.swagger.assert4j.testserver.teststeps.datasource.datagen.DataGenerators.sequentialDateAndTimeTypeProperty;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

public class DateAndTimeDataGenDataSourceTest {

    @Test
    public void buildsRecipeWithDataSourceTestStepWithDateAndTimeDataGenDataSourceWithDefaultValues() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                randomDateAndTimeTypeProperty("property1")
                        )
                )
                .buildTestRecipe();

        DateAndTimeDataGenerator dataGenerator = (DateAndTimeDataGenerator) getDataGenerator(recipe);
        assertThat(dataGenerator.getType(), is("Date and Time"));
        assertThat(dataGenerator.getDateTimeFormat(), is(HH_MM_AM_PM));
        assertThat(dataGenerator.getMinimumValue(), is(new ISO8601DateFormat().parse("1984-02-12T17:26:20Z")));
        assertThat(dataGenerator.getMaximumValue(), is(not(CoreMatchers.<Date>nullValue())));
        assertThat(dataGenerator.getGenerationMode(), is(DateAndTimeDataGenerator.GenerationModeEnum.RANDOM));
        assertThat(dataGenerator.getIncrementValueDay(), is(1));
        assertThat(dataGenerator.getIncrementValueHour(), is(0));
        assertThat(dataGenerator.getIncrementValueMinute(), is(0));
        assertThat(dataGenerator.getIncrementValueSecond(), is(0));
    }

    @Test
    public void buildsRecipeWithDataSourceTestStepWithDateAndTimeDataGenDataSourceWithSequentialValues() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                sequentialDateAndTimeTypeProperty("property1")
                                        // 2 day, 3 hours, 4 minutes, 5 seconds
                                        .incrementBy(((2 * 24 * 60 * 60) + (3 * 60 * 60) + (4 * 60) + 5) * 1000)
                        )
                )
                .buildTestRecipe();

        DateAndTimeDataGenerator dataGenerator = (DateAndTimeDataGenerator) getDataGenerator(recipe);
        assertThat(dataGenerator.getType(), is("Date and Time"));
        assertThat(dataGenerator.getGenerationMode(), is(DateAndTimeDataGenerator.GenerationModeEnum.SEQUENTIAL));
        assertThat(dataGenerator.getIncrementValueDay(), is(2));
        assertThat(dataGenerator.getIncrementValueHour(), is(3));
        assertThat(dataGenerator.getIncrementValueMinute(), is(4));
        assertThat(dataGenerator.getIncrementValueSecond(), is(5));
    }

    @Test
    public void buildsRecipeWithDataSourceTestStepWithDateAndTimeDataGenDataSourceWithProvidedStartAndEndDates() throws Exception {
        Date startDate = new ISO8601DateFormat().parse("2016-01-01T17:26:20Z");
        Date endDate = new ISO8601DateFormat().parse("2020-11-21T15:16:40Z");
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                sequentialDateAndTimeTypeProperty("property1")
                                        .startingAt(startDate)
                                        .endingAt(endDate)
                        )
                )
                .buildTestRecipe();

        DateAndTimeDataGenerator dataGenerator = (DateAndTimeDataGenerator) getDataGenerator(recipe);
        assertThat(dataGenerator.getType(), is("Date and Time"));
        assertThat(dataGenerator.getMinimumValue(), is(startDate));
        assertThat(dataGenerator.getMaximumValue(), is(endDate));
    }

    @Test
    public void buildsRecipeWithDataSourceTestStepWithDateAndTimeDataGenDataSourceWithFormatHH_MM_AM_PM() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                sequentialDateAndTimeTypeProperty("property1")
                                        .withFormatHH_MM_AM_PM()
                        )
                )
                .buildTestRecipe();

        DateAndTimeDataGenerator dataGenerator = (DateAndTimeDataGenerator) getDataGenerator(recipe);
        assertThat(dataGenerator.getDateTimeFormat(), is(HH_MM_AM_PM));
    }

    @Test
    public void buildsRecipeWithDataSourceTestStepWithDateAndTimeDataGenDataSourceWithFormatHH_MM_24_HOUR() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                sequentialDateAndTimeTypeProperty("property1")
                                        .withFormatHH_MM_24_HOUR()
                        )
                )
                .buildTestRecipe();

        DateAndTimeDataGenerator dataGenerator = (DateAndTimeDataGenerator) getDataGenerator(recipe);
        assertThat(dataGenerator.getDateTimeFormat(), is(HH_MM_24_HOUR_));
    }

    @Test
    public void buildsRecipeWithDataSourceTestStepWithDateAndTimeDataGenDataSourceWithFormatHH_MM_SS_AM_PM() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                sequentialDateAndTimeTypeProperty("property1")
                                        .withFormatHH_MM_SS_AM_PM()
                        )
                )
                .buildTestRecipe();

        DateAndTimeDataGenerator dataGenerator = (DateAndTimeDataGenerator) getDataGenerator(recipe);
        assertThat(dataGenerator.getDateTimeFormat(), is(HH_MM_SS_AM_PM));
    }

    @Test
    public void buildsRecipeWithDataSourceTestStepWithDateAndTimeDataGenDataSourceWithFormatHH_MM_SS_24_HOUR() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                sequentialDateAndTimeTypeProperty("property1")
                                        .withFormatHH_MM_SS_24_HOUR()
                        )
                )
                .buildTestRecipe();

        DateAndTimeDataGenerator dataGenerator = (DateAndTimeDataGenerator) getDataGenerator(recipe);
        assertThat(dataGenerator.getDateTimeFormat(), is(HH_MM_SS_24_HOUR_));
    }

    @Test
    public void buildsRecipeWithDataSourceTestStepWithDateAndTimeDataGenDataSourceWithFormatM_D_YYYY_HH_MM_SS_AM_PM() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                sequentialDateAndTimeTypeProperty("property1")
                                        .withFormatM_D_YYYY_HH_MM_SS_AM_PM()
                        )
                )
                .buildTestRecipe();

        DateAndTimeDataGenerator dataGenerator = (DateAndTimeDataGenerator) getDataGenerator(recipe);
        assertThat(dataGenerator.getDateTimeFormat(), is(M_D_YYYY_HH_MM_SS_AM_PM));
    }

    @Test
    public void buildsRecipeWithDataSourceTestStepWithDateAndTimeDataGenDataSourceWithFormatM_D_YYYY_HH_MM_SS_24_HOUR() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                sequentialDateAndTimeTypeProperty("property1")
                                        .withFormatM_D_YYYY_HH_MM_SS_24_HOUR()
                        )
                )
                .buildTestRecipe();

        DateAndTimeDataGenerator dataGenerator = (DateAndTimeDataGenerator) getDataGenerator(recipe);
        assertThat(dataGenerator.getDateTimeFormat(), is(M_D_YYYY_HH_MM_SS_24_HOUR_));
    }

    @Test
    public void buildsRecipeWithDataSourceTestStepWithDateAndTimeDataGenDataSourceWithFormatM_D_YYYY() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                sequentialDateAndTimeTypeProperty("property1")
                                        .withFormatM_D_YYYY()
                        )
                )
                .buildTestRecipe();

        DateAndTimeDataGenerator dataGenerator = (DateAndTimeDataGenerator) getDataGenerator(recipe);
        assertThat(dataGenerator.getDateTimeFormat(), is(M_D_YYYY));
    }

    @Test
    public void buildsRecipeWithDataSourceTestStepWithDateAndTimeDataGenDataSourceWithFormatD_MONTH_YYYY() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                sequentialDateAndTimeTypeProperty("property1")
                                        .withFormatD_MONTH_YYYY()
                        )
                )
                .buildTestRecipe();

        DateAndTimeDataGenerator dataGenerator = (DateAndTimeDataGenerator) getDataGenerator(recipe);
        assertThat(dataGenerator.getDateTimeFormat(), is(D_MONTH_YYYY));
    }

    @Test
    public void buildsRecipeWithDataSourceTestStepWithDateAndTimeDataGenDataSourceWithFormatDAY_OF_WEEK_D_MONTH_YYYY() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                sequentialDateAndTimeTypeProperty("property1")
                                        .withFormatDAY_OF_WEEK_D_MONTH_YYYY()
                        )
                )
                .buildTestRecipe();

        DateAndTimeDataGenerator dataGenerator = (DateAndTimeDataGenerator) getDataGenerator(recipe);
        assertThat(dataGenerator.getDateTimeFormat(), is(DAYOFWEEK_D_MONTH_YYYY));
    }

    @Test
    public void buildsRecipeWithDataSourceTestStepWithDateAndTimeDataGenDataSourceWithFormatISO_8601() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                sequentialDateAndTimeTypeProperty("property1")
                                        .withFormatISO_8601()
                        )
                )
                .buildTestRecipe();

        DateAndTimeDataGenerator dataGenerator = (DateAndTimeDataGenerator) getDataGenerator(recipe);
        assertThat(dataGenerator.getDateTimeFormat(), is(YYYY_MM_DDTHH_MM_SSZ_ISO_8601_));
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
