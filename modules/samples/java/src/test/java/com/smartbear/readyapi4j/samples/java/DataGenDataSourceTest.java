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
import static com.smartbear.readyapi4j.teststeps.restrequest.ParameterBuilder.query;

public class DataGenDataSourceTest extends ApiTestBase {

    @Test
    public void createRecipeForAllDataGenerators() throws Exception {
        RecipeExecutionResult result = executeRecipe(
            dataGenDataSource()
                .withNumberOfRows(10)
                .withProperties(
                    cityTypeProperty("cityProperty").duplicatedBy(2),
                    mac48ComputerAddressTypeProperty("computerAddressProperty"),
                    randomIntegerTypeProperty("integerProperty")
                )
                .named("DataSourceStep")
                .withTestSteps(
                    GET("http://www.google.se/")
                        .withParameters(
                            query("a", "${DataSourceStep#cityProperty}"),
                            query("b", "${DataSourceStep#computerAddressProperty}"),
                            query("c", "${DataSourceStep#integerProperty}")
                        )
                )
        );

        assertExecutionResult(result);
    }
}
