package com.smartbear.readyapi4j.local.execution;

import com.eviware.soapui.support.xml.XmlUtils;
import com.smartbear.readyapi.client.model.HarResponse;
import com.smartbear.readyapi.client.model.ProjectResultReport;
import com.smartbear.readyapi.util.soap.LocalService;
import com.smartbear.readyapi4j.TestRecipe;
import com.smartbear.readyapi4j.execution.Execution;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.xml.ws.Endpoint;
import java.net.MalformedURLException;
import java.net.URL;

import static com.smartbear.readyapi.util.PortFinder.portFinder;
import static com.smartbear.readyapi4j.TestRecipeBuilder.newTestRecipe;
import static com.smartbear.readyapi4j.teststeps.TestSteps.soapRequest;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class SoapUIRecipeExecutorSOAPTest {
    private static final String SOAP_BINDING = "LocalServicePortBinding";
    private static final String WSDL_ENDING = "?WSDL";
    private static final String SOAP_OPERATION = "importantCalculation";
    private static final String PARAMETER_A = "a";
    private static final String PARAMETER_B = "b";
    private static final String VALUE_A = "1";
    private static final String VALUE_B = "2";
    private static final String XPATH_TO_ASSERT = "declare namespace ns2='http://soap.util.readyapi.smartbear.com/';\n" +
            "//ns2:importantCalculationResponse[1]/return[1]/text()";
    private static final String XPATH_RESULT = "3";

    private static String SERVICE_ADDRESS;

    private static Endpoint endpoint;

    private SoapUIRecipeExecutor executor = new SoapUIRecipeExecutor();

    @BeforeClass
    public static void setUp() {
        int port = 8080;
        port = portFinder(port);
        SERVICE_ADDRESS = "http://localhost:" + port + "/ws/calculator";
        endpoint = Endpoint.create(new LocalService());
        endpoint.publish(SERVICE_ADDRESS);
    }

    @AfterClass
    public static void cleanUp() {
        endpoint.stop();
    }

    @Test
    public void runsMinimalSoapRequest() throws MalformedURLException {
        TestRecipe testRecipe = newTestRecipe(
                soapRequest(new URL(SERVICE_ADDRESS + WSDL_ENDING))
                        .forBinding(SOAP_BINDING)
                        .forOperation(SOAP_OPERATION)
                        .withParameter(PARAMETER_A, VALUE_A)
                        .withParameter(PARAMETER_B, VALUE_B)
                        .assertSoapOkResponse()
                        .assertXPath(XPATH_TO_ASSERT, XPATH_RESULT)
        ).buildTestRecipe();
        Execution execution = executor.executeRecipe(testRecipe);
        assertThat(execution.getId(), is(not(nullValue())));
        assertThat(execution.getCurrentStatus(), is(ProjectResultReport.StatusEnum.FINISHED));

        HarResponse harResponse = execution.getExecutionResult().getTestStepResult(0).getHarEntry().getResponse();
        assertThat(harResponse, is(not(nullValue())));
        assertTrue(XmlUtils.seemsToBeXml(harResponse.getContent().getText()));
    }
}