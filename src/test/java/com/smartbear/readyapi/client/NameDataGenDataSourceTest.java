package com.smartbear.readyapi.client;

import com.smartbear.readyapi.client.model.DataGenDataSource;
import com.smartbear.readyapi.client.model.DataGenerator;
import com.smartbear.readyapi.client.model.DataSourceTestStep;
import com.smartbear.readyapi.client.model.NameDataGenerator;
import com.smartbear.readyapi.client.model.TestStep;
import com.smartbear.readyapi.client.teststeps.TestStepTypes;
import org.junit.Test;

import static com.smartbear.readyapi.client.TestRecipeBuilder.newTestRecipe;
import static com.smartbear.readyapi.client.teststeps.TestSteps.dataGenDataSource;
import static com.smartbear.readyapi.client.teststeps.datasource.datagen.DataGenerators.nameTypeProperty;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class NameDataGenDataSourceTest {

    @Test
    public void buildsRecipeWithDataSourceTestStepWithNameDataGenDataSourceWithDefaultValues() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                nameTypeProperty("property1")
                        )
                )
                .buildTestRecipe();

        NameDataGenerator dataGenerator = (NameDataGenerator) getDataGenerator(recipe);
        assertThat(dataGenerator.getType(), is("Name"));
        assertThat(dataGenerator.getGender(), is(NameDataGenerator.GenderEnum.ANY));
        assertThat(dataGenerator.getNameType(), is(NameDataGenerator.NameTypeEnum.FULL));
    }

    @Test
    public void buildsRecipeWithDataSourceTestStepWithNameDataGenDataSourceWithGenderAnyAndFullNames() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                nameTypeProperty("property1")
                                .withGenderAny()
                                .withFullNames()
                        )
                )
                .buildTestRecipe();

        NameDataGenerator dataGenerator = (NameDataGenerator) getDataGenerator(recipe);
        assertThat(dataGenerator.getType(), is("Name"));
        assertThat(dataGenerator.getGender(), is(NameDataGenerator.GenderEnum.ANY));
        assertThat(dataGenerator.getNameType(), is(NameDataGenerator.NameTypeEnum.FULL));
    }

    @Test
    public void buildsRecipeWithDataSourceTestStepWithNameDataGenDataSourceWithGenderMaleAndFirstNames() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                nameTypeProperty("property1")
                                .withGenderMale()
                                .withFirstNames()
                        )
                )
                .buildTestRecipe();

        NameDataGenerator dataGenerator = (NameDataGenerator) getDataGenerator(recipe);
        assertThat(dataGenerator.getType(), is("Name"));
        assertThat(dataGenerator.getGender(), is(NameDataGenerator.GenderEnum.MALE));
        assertThat(dataGenerator.getNameType(), is(NameDataGenerator.NameTypeEnum.FIRSTNAME));
    }


    @Test
    public void buildsRecipeWithDataSourceTestStepWithNameDataGenDataSourceWithGenderFemaleAndLastNames() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                nameTypeProperty("property1")
                                .withGenderFemale()
                                .withLatsNames()
                        )
                )
                .buildTestRecipe();

        NameDataGenerator dataGenerator = (NameDataGenerator) getDataGenerator(recipe);
        assertThat(dataGenerator.getType(), is("Name"));
        assertThat(dataGenerator.getGender(), is(NameDataGenerator.GenderEnum.FEMALE));
        assertThat(dataGenerator.getNameType(), is(NameDataGenerator.NameTypeEnum.LASTNAME));
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
