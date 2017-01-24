package com.smartbear.readyapi4j;

import com.smartbear.readyapi.client.model.RequestAttachment;
import com.smartbear.readyapi.client.model.SoapParameter;
import com.smartbear.readyapi.client.model.SoapRequestTestStep;
import com.smartbear.readyapi4j.extractor.ExtractorData;
import com.smartbear.readyapi4j.teststeps.TestStepTypes;
import com.sun.jersey.core.util.Base64;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static com.smartbear.readyapi4j.TestRecipeBuilder.newTestRecipe;
import static com.smartbear.readyapi4j.attachments.Attachments.byteArray;
import static com.smartbear.readyapi4j.attachments.Attachments.stream;
import static com.smartbear.readyapi4j.attachments.Attachments.string;
import static com.smartbear.readyapi4j.extractor.Extractors.fromResponse;
import static com.smartbear.readyapi4j.extractor.Extractors.fromProperty;
import static com.smartbear.readyapi4j.teststeps.TestSteps.soapRequest;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class SoapRequestStepRecipeTest {

    @Test
    public void testSoapRecipe() throws Exception {
        TestRecipe recipe = newTestRecipe(
                soapRequest(new URL("http://www.webservicex.com/globalweather.asmx?WSDL"))
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


    @Test
    public void buildSoapRequestTestStepRecipeWithStreamAttachment() throws MalformedURLException {
        InputStream inputStream = new ByteArrayInputStream("Content".getBytes());
        TestRecipe recipe = newTestRecipe(
                soapRequest(new URL("http://www.webservicex.com/globalweather.asmx?WSDL"))
                        .named("Soap Rulez")
                        .forBinding("GlobalWeatherSoap12")
                        .forOperation("GetWeather")
                        .withParameter("CountryName", "Sweden")
                        .withPathParameter("//*:CityName", "Stockholm")
                        .withAttachments(stream(inputStream, "ContentType")))
                .buildTestRecipe();
        SoapRequestTestStep testStep = (SoapRequestTestStep) recipe.getTestCase().getTestSteps().get(0);
        List<RequestAttachment> attachments = testStep.getAttachments();
        assertThat(attachments.size(), is(1));
        assertRequestAttachment(attachments.get(0), null, "ContentType", null, "Content".getBytes());
    }

    @Test
    public void buildSoapRequestTestStepRecipeWithByteArrayAttachment() throws MalformedURLException {
        TestRecipe testRecipe = newTestRecipe(
                soapRequest(new URL("http://www.webservicex.com/globalweather.asmx?WSDL"))
                        .named("Soap Rulez")
                        .forBinding("GlobalWeatherSoap12")
                        .forOperation("GetWeather")
                        .withParameter("CountryName", "Sweden")
                        .withPathParameter("//*:CityName", "Stockholm")
                        .withAttachments(byteArray("Content".getBytes(), "ContentType")))
                .buildTestRecipe();
        SoapRequestTestStep testStep = (SoapRequestTestStep) testRecipe.getTestCase().getTestSteps().get(0);
        List<RequestAttachment> attachments = testStep.getAttachments();
        assertThat(attachments.size(), is(1));
        assertRequestAttachment(attachments.get(0), null, "ContentType", null, "Content".getBytes());
    }

    @Test
    public void buildSoapRequestTestStepRecipeWithStringAttachment() throws MalformedURLException {
        TestRecipe testRecipe = newTestRecipe(
                soapRequest(new URL("http://www.webservicex.com/globalweather.asmx?WSDL"))
                        .named("Soap Rulez")
                        .forBinding("GlobalWeatherSoap12")
                        .forOperation("GetWeather")
                        .withParameter("CountryName", "Sweden")
                        .withPathParameter("//*:CityName", "Stockholm")
                        .withAttachments(string("Content", "ContentType")))
                .buildTestRecipe();
        SoapRequestTestStep testStep = (SoapRequestTestStep) testRecipe.getTestCase().getTestSteps().get(0);
        List<RequestAttachment> attachments = testStep.getAttachments();
        assertThat(attachments.size(), is(1));
        assertRequestAttachment(attachments.get(0), null, "ContentType", null, "Content".getBytes());
    }

    private void assertRequestAttachment(RequestAttachment attachment, String contentId, String contentType, String name, byte[] content) {
        assertThat(attachment.getContentId(), is(contentId));
        assertThat(attachment.getContentType(), is(contentType));
        assertThat(attachment.getName(), is(name));
        assertThat(Base64.decode(attachment.getContent()), is(content));
    }

    @Test
    public void buildRestRequestTestRecipeWithPropertyExtractor() throws MalformedURLException {
        final String[] extractedProperty = {""};
        TestRecipe recipe = newTestRecipe(
                soapRequest(new URL("http://www.webservicex.com/globalweather.asmx?WSDL"))
                        .named("Soap Rulez")
                        .forBinding("GlobalWeatherSoap12")
                        .forOperation("GetWeather")
                        .withParameter("CountryName", "Sweden")
                        .withPathParameter("//*:CityName", "Stockholm")
                        .withExtractors(
                                fromProperty("Endpoint", property -> extractedProperty[0] = property)))
                .buildTestRecipe();

        // This should not be set only after building the testrecipe, it should be set after run
        assertThat(extractedProperty[0], is(""));
        assertThat(recipe.getTestCase().getProperties().size(), is(2));
        recipe.getTestCase().getProperties().forEach((key, value) -> {
            if (key.contains("Endpoint")) {
                assertThat(value, is(""));
            } else {
                assertThat(key, is(ExtractorData.EXTRACTOR_DATA_KEY));
            }
        });
    }

    @Test
    public void buildRestRequestTestRecipeWithJsonPathExtractor() throws MalformedURLException {
        final String[] extractedProperty = {""};
        TestRecipe recipe = newTestRecipe(
                soapRequest(new URL("http://www.webservicex.com/globalweather.asmx?WSDL"))
                        .named("Soap Rulez")
                        .forBinding("GlobalWeatherSoap12")
                        .forOperation("GetWeather")
                        .withParameter("CountryName", "Sweden")
                        .withPathParameter("//*:CityName", "Stockholm")
                        .withExtractors(
                                fromResponse("$[0].Endpoint", property -> extractedProperty[0] = property)))
                .buildTestRecipe();

        // This should not be set only after building the testrecipe, it should be set after run
        assertThat(extractedProperty[0], is(""));
        assertThat(recipe.getTestCase().getProperties().size(), is(2));
        recipe.getTestCase().getProperties().forEach((key, value) -> {
            if (key.contains("$[0].Endpoint")) {
                assertThat(value, is(""));
            } else {
                assertThat(key, is(ExtractorData.EXTRACTOR_DATA_KEY));
            }
        });
    }
}
