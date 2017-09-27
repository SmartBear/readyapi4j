package io.swagger.assert4j.samples.java;

import io.swagger.assert4j.result.RecipeExecutionResult;
import org.junit.Test;

import java.net.URL;

import static io.swagger.assert4j.facade.execution.RecipeExecutionFacade.executeRecipe;
import static io.swagger.assert4j.support.AssertionUtils.assertExecutionResult;
import static io.swagger.assert4j.teststeps.TestSteps.soapRequest;

public class SimpleSoapTest {

    @Test
    public void simpleSoapTest() throws Exception {
        RecipeExecutionResult result = executeRecipe("Simple SOAP Test",
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
