package io.swagger.assert4j.dsl

import io.swagger.assert4j.client.model.SoapMockResponseTestStep
import io.swagger.assert4j.TestRecipe
import org.junit.Test

import static io.swagger.assert4j.dsl.TestDsl.recipe

class SoapMockResponseDslTest {
    private static final String WSDL_URL = 'http://somedomain.com/service?wsdl'
    private static final String BINDING = 'TheBinding'
    private static final String OPERATION = 'TheOperation'
    private static final String PATH = '/mymockservice'
    private static final int PORT = 8080
    private static final String TEST_STEP_NAME = 'SOAP Mock Response'

    @Test
    void buildsSoapMockResponseTestStep() throws Exception {
        TestRecipe recipe = recipe {
            soapMockResponse {
                name = TEST_STEP_NAME
                wsdl = WSDL_URL
                binding = BINDING
                operation = OPERATION
                path = PATH
                port = PORT
            }
        }

        SoapMockResponseTestStep testStep = recipe.testCase.testSteps[0] as SoapMockResponseTestStep
        assert testStep.name == TEST_STEP_NAME
        assert testStep.wsdl == WSDL_URL
        assert testStep.binding == BINDING
        assert testStep.operation == OPERATION
        assert testStep.path == PATH
        assert testStep.port == PORT
    }
}
