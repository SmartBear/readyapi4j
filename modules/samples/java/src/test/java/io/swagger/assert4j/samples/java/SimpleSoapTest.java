package io.swagger.assert4j.samples.java;

import io.swagger.assert4j.result.RecipeExecutionResult;
import org.junit.Test;

import java.net.URL;

import static io.swagger.assert4j.assertions.Assertions.notSoapFault;
import static io.swagger.assert4j.assertions.Assertions.schemaCompliance;
import static io.swagger.assert4j.extractor.Extractors.fromResponse;
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
                        .withAssertions(
                                schemaCompliance(),
                                notSoapFault()
                        )
        );

        assertExecutionResult(result);
    }

    @Test
    public void simpleSoapWithExtractorTest() throws Exception {
        RecipeExecutionResult result = executeRecipe("Simple SOAP Test",
                soapRequest(new URL("http://www.webservicex.com/CurrencyConvertor.asmx?wsdl"))
                        .forBinding("CurrencyConvertorSoap")
                        .forOperation("ConversionRate")
                        .named("GetConversionRate")
                        .withParameter("FromCurrency", "USD")
                        .withParameter("ToCurrency", "SEK")
                        .withAssertions(
                                schemaCompliance(),
                                notSoapFault()
                        )
                        .withExtractors(
                                fromResponse("//*:ConversionRateResult/text()", v -> {
                                    System.out.println("Result is [" + v + "]");
                                })
                        )
        );

        assertExecutionResult(result);
    }
}
