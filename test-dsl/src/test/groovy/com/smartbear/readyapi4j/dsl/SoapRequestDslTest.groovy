package com.smartbear.readyapi4j.dsl

import com.smartbear.readyapi.client.model.SoapRequestTestStep
import com.smartbear.readyapi4j.TestRecipe
import org.junit.Test

import static com.smartbear.readyapi4j.dsl.DataExtractor.extractFirstTestStep
import static com.smartbear.readyapi4j.dsl.TestDsl.recipe

class SoapRequestDslTest {

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
        assert testStep.wsdl == WSDL_URL
        assert testStep.binding == BINDING
        assert testStep.operation == OPERATION
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
        assert testStep.parameters.find({ it.path = '/the/path'})?.value == '12'
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
        assert testStep.parameters.find({ it.name = 'ParamName'})?.value == '12'
    }

    private static SoapRequestTestStep extractSoapTestStep(TestRecipe recipe) {
        SoapRequestTestStep testStep = extractFirstTestStep(recipe) as SoapRequestTestStep
        return testStep as SoapRequestTestStep
    }
}
