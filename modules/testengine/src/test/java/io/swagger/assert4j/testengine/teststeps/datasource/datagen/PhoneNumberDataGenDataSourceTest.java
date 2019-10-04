package io.swagger.assert4j.testengine.teststeps.datasource.datagen;

import io.swagger.assert4j.TestRecipe;
import io.swagger.assert4j.client.model.*;
import io.swagger.assert4j.teststeps.TestStepTypes;
import org.junit.Test;

import static io.swagger.assert4j.TestRecipeBuilder.newTestRecipe;
import static io.swagger.assert4j.testengine.teststeps.ServerTestSteps.dataGenDataSource;
import static io.swagger.assert4j.testengine.teststeps.datasource.datagen.DataGenerators.phoneNumberTypeProperty;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class PhoneNumberDataGenDataSourceTest {

    @Test
    public void buildsRecipeWithDataSourceTestStepWithPhoneNumberDataGenDataSourceWithDefaultValues() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                phoneNumberTypeProperty("property1")
                        )
                )
                .buildTestRecipe();

        PhoneNumberDataGenerator dataGenerator = (PhoneNumberDataGenerator) getDataGenerator(recipe);
        assertThat(dataGenerator.getType(), is("Phone Number"));
        assertThat(dataGenerator.getNumberFormat(), is("XXX-XXX-XXXX"));
    }

    @Test
    public void buildsRecipeWithDataSourceTestStepWithPhoneNumberDataGenDataSourceWithProvidednumberFormat() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                phoneNumberTypeProperty("property1")
                                        .withNumberFormat("+X XXX-XXX-XXXX")
                        )
                )
                .buildTestRecipe();

        PhoneNumberDataGenerator dataGenerator = (PhoneNumberDataGenerator) getDataGenerator(recipe);
        assertThat(dataGenerator.getType(), is("Phone Number"));
        assertThat(dataGenerator.getNumberFormat(), is("+X XXX-XXX-XXXX"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsExceptionIfProvidednumberFormatIsNotSupported() throws Exception {
        newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                phoneNumberTypeProperty("property1")
                                        .withNumberFormat("ABC")
                        )
                )
                .buildTestRecipe();
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
