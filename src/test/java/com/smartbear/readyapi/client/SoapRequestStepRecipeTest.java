package com.smartbear.readyapi.client;

import com.smartbear.readyapi.client.model.SoapParameter;
import com.smartbear.readyapi.client.model.SoapRequestTestStep;
import com.smartbear.readyapi.client.teststeps.TestStepTypes;
import org.junit.Test;

import java.util.List;

import static com.smartbear.readyapi.client.TestRecipeBuilder.newTestRecipe;
import static com.smartbear.readyapi.client.teststeps.TestSteps.soapRequest;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class SoapRequestStepRecipeTest {

    @Test
    public void testSoapRecipe() throws Exception {
        TestRecipe recipe = newTestRecipe()
            .addStep(soapRequest("http://www.webservicex.com/globalweather.asmx?WSDL")
                .named("Soap Rulez")
                .forBinding("GlobalWeatherSoap12")
                .forOperation("GetWeather")
                .withParameter("CountryName", "Sweden")
                .withPathParameter("//*:CityName", "Stockholm")

            )
            .buildTestRecipe();

        SoapRequestTestStep testStep = (SoapRequestTestStep) recipe.getTestCase().getTestSteps().get(0);
        assertThat(testStep.getName(), is("Soap Rulez"));
        assertThat(testStep.getType(), is(TestStepTypes.SOAP_REQUEST.getName()));
        assertThat(testStep.getWsdl(), is("http://www.webservicex.com/globalweather.asmx?WSDL"));
        assertThat(testStep.getBinding(), is("GlobalWeatherSoap12"));
        assertThat(testStep.getOperation(), is("GetWeather"));

        List<SoapParameter> parameters = testStep.getParameters();
        assertThat(parameters.size(), is(2));
        assertThat(parameters.get(0).getName(), is("CountryName"));
        assertThat(parameters.get(1).getName(), nullValue());
    }
}
