package com.smartbear.readyapi4j.samples.java;

import com.smartbear.readyapi4j.result.RecipeExecutionResult;
import org.junit.Test;

import java.net.URL;

import static com.smartbear.readyapi4j.facade.execution.RecipeExecutionFacade.executeRecipe;
import static com.smartbear.readyapi4j.support.AssertionUtils.assertExecutionResult;
import static com.smartbear.readyapi4j.teststeps.TestSteps.soapRequest;

public class SimpleSoapTest extends ApiTestBase {

    @Test
    public void simpleSoapTest() throws Exception {
        RecipeExecutionResult result = executeRecipe(
            soapRequest(new URL("http://www.webservicex.com/globalweather.asmx?WSDL"))
                .forBinding("GlobalWeatherSoap12")
                .forOperation("GetWeather")
                .withParameter("CountryName", "Sweden")
                .withPathParameter("//*:CityName", "Stockholm")
                .assertSoapOkResponse()
                .assertSchemaCompliance()
        );

        assertExecutionResult(result);
    }
}
