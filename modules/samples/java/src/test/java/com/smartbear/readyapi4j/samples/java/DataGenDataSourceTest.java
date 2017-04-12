package com.smartbear.readyapi4j.samples.java;

import com.smartbear.readyapi4j.result.RecipeExecutionResult;
import org.junit.Test;

import static com.smartbear.readyapi4j.facade.execution.RecipeExecutionFacade.executeRecipe;
import static com.smartbear.readyapi4j.support.AssertionUtils.assertExecutionResult;
import static com.smartbear.readyapi4j.testserver.teststeps.ServerTestSteps.dataGenDataSource;
import static com.smartbear.readyapi4j.testserver.teststeps.datasource.datagen.DataGenerators.cityTypeProperty;
import static com.smartbear.readyapi4j.testserver.teststeps.datasource.datagen.DataGenerators.mac48ComputerAddressTypeProperty;
import static com.smartbear.readyapi4j.testserver.teststeps.datasource.datagen.DataGenerators.randomIntegerTypeProperty;
import static com.smartbear.readyapi4j.teststeps.TestSteps.GET;

public class DataGenDataSourceTest extends ApiTestBase {

    @Test
    public void createRecipeForAllDataGenerators() throws Exception {
        RecipeExecutionResult result = executeRecipe(
            dataGenDataSource()
                .withNumberOfRows(10)
                .withProperties(
                    cityTypeProperty("cityProperty")
                        .duplicatedBy(2)
                )
                .andProperty(mac48ComputerAddressTypeProperty("computerAddressProperty"))
                .andProperty(randomIntegerTypeProperty("integerProperty")
                )
                .named("DataSourceStep")
                .addTestStep(GET("http://www.google.se/")
                    .addQueryParameter("a", "${DataSourceStep#cityProperty}")
                    .addQueryParameter("b", "${DataSourceStep#computerAddressProperty}")
                    .addQueryParameter("c", "${DataSourceStep#integerProperty}"))
        );

        assertExecutionResult(result);
    }
}
