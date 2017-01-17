package com.smartbear.readyapi4j.dsl.assertions

import com.smartbear.readyapi4j.assertions.Assertions
import com.smartbear.readyapi4j.assertions.NotSoapFaultAssertionBuilder
import com.smartbear.readyapi4j.assertions.SoapFaultAssertionBuilder

class SoapRequestAssertionsDelegate extends AbstractAssertionsDelegate {
    SoapFaultAssertionBuilder getSoapFault() {
        assertionBuilders.add(Assertions.soapFault())
        //returning null since it's not a real getter but a way to use this method without parentheses in the DSL
        return null
    }

    NotSoapFaultAssertionBuilder getNotSoapFault() {
        assertionBuilders.add(Assertions.notSoapFault())
        //returning null since it's not a real getter but a way to use this method without parentheses in the DSL
        return null
    }
}
