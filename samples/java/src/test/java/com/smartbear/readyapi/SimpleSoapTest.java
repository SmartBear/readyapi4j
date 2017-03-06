package com.smartbear.readyapi;

import com.smartbear.readyapi.client.TestRecipe;
import com.smartbear.readyapi.client.teststeps.soaprequest.SoapRequestStepBuilder;
import org.junit.Test;

import java.net.URL;

import static com.smartbear.readyapi.client.TestRecipeBuilder.newTestRecipe;
import static com.smartbear.readyapi.client.teststeps.TestSteps.soapRequest;

public class SimpleSoapTest extends ApiTestBase {

    @Test
    public void simpleSoapTest() throws Exception {

        SoapRequestStepBuilder soapRequest = soapRequest(new URL("http://www.webservicex.com/globalweather.asmx?WSDL"))
            .forBinding("GlobalWeatherSoap12")
            .forOperation("GetWeather")
            .withParameter("CountryName", "Sweden")
            .withPathParameter("//*:CityName", "Stockholm")
            .assertSoapOkResponse()
            .assertSchemaCompliance();

        TestRecipe recipe = newTestRecipe().addStep(soapRequest).buildTestRecipe();
        executeAndAssert(recipe);
    }
}
