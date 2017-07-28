package io.swagger.assert4j.dsl.assertions

import io.swagger.assert4j.assertions.Assertions
import io.swagger.assert4j.assertions.DefaultSchemaComplianceAssertionBuilder
import io.swagger.assert4j.assertions.NotSoapFaultAssertionBuilder
import io.swagger.assert4j.assertions.SchemaComplianceAssertionBuilder
import io.swagger.assert4j.assertions.SoapFaultAssertionBuilder

/**
 * The delegate to respond to commands inside 'asserting' closure of SOAP request
 */
class SoapRequestAssertionsDelegate extends AbstractAssertionsDelegate {

    /**
     * Creates 'SOAP Fault' assertion.
     * @return null , it's not a real getter, but a way to call this method without parentheses in the DSL/client code.
     */
    SoapFaultAssertionBuilder getSoapFault() {
        assertionBuilders.add(Assertions.soapFault())
        //returning null since it's not a real getter but a way to use this method without parentheses in the DSL
        return null
    }

    /**
     * Creates 'Not SOAP Fault' assertion.
     * @return null , it's not a real getter, but a way to call this method without parentheses in the DSL/client code.
     */
    NotSoapFaultAssertionBuilder getNotSoapFault() {
        assertionBuilders.add(Assertions.notSoapFault())
        //returning null since it's not a real getter but a way to use this method without parentheses in the DSL
        return null
    }

    /**
     * Creates Schema Compliance Assertion.
     * @return null , it's not a real getter, but a way to call this method without parentheses in the DSL/client code.
     */
    SchemaComplianceAssertionBuilder getSchemaCompliance() {
        assertionBuilders.add(new DefaultSchemaComplianceAssertionBuilder())
        return null
    }

}
