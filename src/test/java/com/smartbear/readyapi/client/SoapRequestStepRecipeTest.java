package com.smartbear.readyapi.client;

import com.smartbear.readyapi.client.model.RequestAttachment;
import com.smartbear.readyapi.client.model.SoapParameter;
import com.smartbear.readyapi.client.model.SoapRequestTestStep;
import com.smartbear.readyapi.client.teststeps.TestStepTypes;
import com.smartbear.readyapi.client.teststeps.request.RequestAttachmentBuilder;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
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
            .addStep(soapRequest(new URL("http://www.webservicex.com/globalweather.asmx?WSDL"))
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
    public void buildSoapRequestTestStepRecipeWithAttachments() throws MalformedURLException {
        TestRecipe recipe = newTestRecipe()
                .addStep(soapRequest(new URL("http://www.webservicex.com/globalweather.asmx?WSDL"))
                        .named("Soap Rulez")
                        .forBinding("GlobalWeatherSoap12")
                        .forOperation("GetWeather")
                        .withParameter("CountryName", "Sweden")
                        .withPathParameter("//*:CityName", "Stockholm")
                        .withAttachments(new RequestAttachmentBuilder()
                                        .withContentId("ContentId")
                                        .withContentType("ContentType")
                                        .withName("Name")
                                        .withContent("Content".getBytes()),
                                new RequestAttachmentBuilder()
                                        .withContentId("ContentId2")
                                        .withContentType("ContentType2")
                                        .withName("Name2")
                                        .withContent("Content2".getBytes()))
                )
                .buildTestRecipe();
        SoapRequestTestStep testStep = (SoapRequestTestStep) recipe.getTestCase().getTestSteps().get(0);
        List<RequestAttachment> attachments = testStep.getAttachments();
        assertThat(attachments.size(), is(2));
        assertRequestAttachment(attachments.get(0), "ContentId", "ContentType", "Name", "Content".getBytes());
        assertRequestAttachment(attachments.get(1), "ContentId2", "ContentType2", "Name2", "Content2".getBytes());
    }

    @Test
    public void buildSoapRequestTestStepRecipeWithAddedAttachment() throws MalformedURLException {
        TestRecipe testRecipe = newTestRecipe()
                .addStep(soapRequest(new URL("http://www.webservicex.com/globalweather.asmx?WSDL"))
                        .named("Soap Rulez")
                        .forBinding("GlobalWeatherSoap12")
                        .forOperation("GetWeather")
                        .withParameter("CountryName", "Sweden")
                        .withPathParameter("//*:CityName", "Stockholm")
                        .addAttachment(new RequestAttachmentBuilder()
                                .withContentId("ContentId")
                                .withContentType("ContentType")
                                .withName("Name")
                                .withContent("Content".getBytes())))
                .buildTestRecipe();
        SoapRequestTestStep testStep = (SoapRequestTestStep) testRecipe.getTestCase().getTestSteps().get(0);
        List<RequestAttachment> attachments = testStep.getAttachments();
        assertThat(attachments.size(), is(1));
        assertRequestAttachment(attachments.get(0), "ContentId", "ContentType", "Name", "Content".getBytes());
    }

    private void assertRequestAttachment(RequestAttachment attachment, String contentId, String contentType, String name, byte[] content) {
        assertThat(attachment.getContentId(), is(contentId));
        assertThat(attachment.getContentType(), is(contentType));
        assertThat(attachment.getName(), is(name));
        assertThat(attachment.getContent(), is(content));
    }
}
