package com.smartbear.readyapi4j.testengine.teststeps.datasource.datagen;

import com.smartbear.readyapi4j.TestRecipe;
import com.smartbear.readyapi4j.client.model.*;
import com.smartbear.readyapi4j.client.model.NameDataGenerator.GenderEnum;
import com.smartbear.readyapi4j.client.model.NameDataGenerator.NameTypeEnum;
import com.smartbear.readyapi4j.teststeps.TestStepTypes;
import org.junit.Test;

import static com.smartbear.readyapi4j.TestRecipeBuilder.newTestRecipe;
import static com.smartbear.readyapi4j.testengine.teststeps.ServerTestSteps.dataGenDataSource;
import static com.smartbear.readyapi4j.testengine.teststeps.datasource.datagen.DataGenerators.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class NameDataGenDataSourceTest {

    @Test
    public void buildsRecipeWithDataSourceTestStepWithNameDataGenDataSourceWithGenderAnyAndFullNames() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                anyGenderFullNameTypeProperty("property1")
                        )
                )
                .buildTestRecipe();

        assertNameDataGenerator((NameDataGenerator) getDataGenerator(recipe), GenderEnum.ANY, NameTypeEnum.FULL);
    }

    @Test
    public void buildsRecipeWithDataSourceTestStepWithNameDataGenDataSourceWithGenderAnyAndFirstNames() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                anyGenderFirstNameTypeProperty("property1")
                        )
                )
                .buildTestRecipe();

        assertNameDataGenerator((NameDataGenerator) getDataGenerator(recipe), GenderEnum.ANY, NameTypeEnum.FIRSTNAME);
    }

    @Test
    public void buildsRecipeWithDataSourceTestStepWithNameDataGenDataSourceWithGenderAnyAndLastNames() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                anyGenderLastNameTypeProperty("property1")
                        )
                )
                .buildTestRecipe();

        assertNameDataGenerator((NameDataGenerator) getDataGenerator(recipe), GenderEnum.ANY, NameTypeEnum.LASTNAME);
    }

    @Test
    public void buildsRecipeWithDataSourceTestStepWithNameDataGenDataSourceWithMaleFullNames() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                maleFullNameTypeProperty("property1")
                        )
                )
                .buildTestRecipe();

        assertNameDataGenerator((NameDataGenerator) getDataGenerator(recipe), GenderEnum.MALE, NameTypeEnum.FULL);
    }

    @Test
    public void buildsRecipeWithDataSourceTestStepWithNameDataGenDataSourceWithMaleFirstNames() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                maleFirstNameTypeProperty("property1")
                        )
                )
                .buildTestRecipe();

        assertNameDataGenerator((NameDataGenerator) getDataGenerator(recipe), GenderEnum.MALE, NameTypeEnum.FIRSTNAME);
    }

    @Test
    public void buildsRecipeWithDataSourceTestStepWithNameDataGenDataSourceWithMaleLastNames() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                maleLastNameTypeProperty("property1")
                        )
                )
                .buildTestRecipe();

        assertNameDataGenerator((NameDataGenerator) getDataGenerator(recipe), GenderEnum.MALE, NameTypeEnum.LASTNAME);
    }

    @Test
    public void buildsRecipeWithDataSourceTestStepWithNameDataGenDataSourceWithFemaleFullNames() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                femaleFullNameTypeProperty("property1")
                        )
                )
                .buildTestRecipe();

        assertNameDataGenerator((NameDataGenerator) getDataGenerator(recipe), GenderEnum.FEMALE, NameTypeEnum.FULL);
    }

    @Test
    public void buildsRecipeWithDataSourceTestStepWithNameDataGenDataSourceWithFemaleFirstNames() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                femaleFirstNameTypeProperty("property1")
                        )
                )
                .buildTestRecipe();

        assertNameDataGenerator((NameDataGenerator) getDataGenerator(recipe), GenderEnum.FEMALE, NameTypeEnum.FIRSTNAME);
    }

    @Test
    public void buildsRecipeWithDataSourceTestStepWithNameDataGenDataSourceWithFemaleLastNames() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withProperty(
                                femaleLastNameTypeProperty("property1")
                        )
                )
                .buildTestRecipe();

        assertNameDataGenerator((NameDataGenerator) getDataGenerator(recipe), GenderEnum.FEMALE, NameTypeEnum.LASTNAME);
    }

    private void assertNameDataGenerator(NameDataGenerator dataGenerator, GenderEnum gender, NameTypeEnum nameType) {
        assertThat(dataGenerator.getType(), is("Name"));
        assertThat(dataGenerator.getGender(), is(gender));
        assertThat(dataGenerator.getNameType(), is(nameType));
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
