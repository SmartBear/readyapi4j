package com.smartbear.readyapi4j.samples.java;

import com.smartbear.readyapi.client.TestRecipe;
import org.junit.Test;

import static com.smartbear.readyapi.client.TestRecipeBuilder.newTestRecipe;
import static com.smartbear.readyapi.client.teststeps.TestSteps.dataGenDataSource;
import static com.smartbear.readyapi.client.teststeps.TestSteps.getRequest;
import static com.smartbear.readyapi.client.teststeps.datasource.datagen.DataGenerators.cityTypeProperty;
import static com.smartbear.readyapi.client.teststeps.datasource.datagen.DataGenerators.mac48ComputerAddressTypeProperty;
import static com.smartbear.readyapi.client.teststeps.datasource.datagen.DataGenerators.randomIntegerTypeProperty;

public class DataGenDataSourceTest extends ApiTestBase {

    @Test
    public void createRecipeForAllDataGenerators() throws Exception {
        TestRecipe recipe = newTestRecipe()
            .addStep(dataGenDataSource()
                .withNumberOfRows(10)
                .withProperties(
                    cityTypeProperty("cityProperty")
                        .duplicatedBy(2)
                )
                .andProperty(mac48ComputerAddressTypeProperty("computerAddressProperty"))
                .andProperty(randomIntegerTypeProperty("integerProperty")
                )
                .named("DataSourceStep")
                .addTestStep(getRequest("http://www.google.se/")
                    .addQueryParameter("a", "${DataSourceStep#cityProperty}")
                    .addQueryParameter("b", "${DataSourceStep#computerAddressProperty}")
                    .addQueryParameter("c", "${DataSourceStep#integerProperty}"))
            )
            .buildTestRecipe();

        recipe.getTestCase().discardOkResults(false);

        executeAndAssert(recipe);
    }
}
