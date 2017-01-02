package com.smartbear.readyapi4j.dsl

import com.smartbear.readyapi.client.model.SoapRequestTestStep
import com.smartbear.readyapi.client.model.TestStep
import com.smartbear.readyapi4j.TestRecipe
import org.junit.Test

import static com.smartbear.readyapi4j.dsl.DataExtractor.extractFirstTestStep
import static com.smartbear.readyapi4j.dsl.TestDsl.recipe
import static org.junit.Assert.assertThat
import static org.hamcrest.CoreMatchers.is

class SoapTestStepDslTest {


    public static final String WSDL_URL = 'http://somedomain.com/service?wsdl'
    public static final String BINDING = 'TheBinding'
    public static final String OPERATION = 'TheOperation'

    @Test
    void buildsSoapRequestWithUrlString() throws Exception {
        TestRecipe recipe = recipe {
            soapRequest  {
                wsdl = WSDL_URL
                binding = BINDING
                operation = OPERATION
            }
        }

        SoapRequestTestStep testStep = extractSoapTestStep(recipe)
        assertThat(testStep.wsdl as String, is(WSDL_URL))
        assertThat(testStep.binding, is(BINDING))
        assertThat(testStep.operation, is(OPERATION))
    }

    @Test
    void buildsSoapRequestWithPathParameters() throws Exception {
        TestRecipe recipe = recipe {
            soapRequest  {
                wsdl = WSDL_URL
                binding = BINDING
                operation = OPERATION
                pathParam '/the/path', 12
            }
        }

        SoapRequestTestStep testStep = extractSoapTestStep(recipe)
        assertThat(testStep.parameters.find({ it.path = '/the/path'})?.value, is('12'))
    }

    @Test
    void buildsSoapRequestWithNamedParameters() throws Exception {
        TestRecipe recipe = recipe {
            soapRequest  {
                wsdl = WSDL_URL
                binding = BINDING
                operation = OPERATION
                namedParam 'ParamName', 12
            }
        }

        SoapRequestTestStep testStep = extractSoapTestStep(recipe)
        assertThat(testStep.parameters.find({ it.name = 'ParamName'})?.value, is('12'))
    }

    private static SoapRequestTestStep extractSoapTestStep(TestRecipe recipe) {
        TestStep testStep = extractFirstTestStep(recipe)
        assertThat(testStep, is(SoapRequestTestStep))
        return testStep as SoapRequestTestStep
    }
}
