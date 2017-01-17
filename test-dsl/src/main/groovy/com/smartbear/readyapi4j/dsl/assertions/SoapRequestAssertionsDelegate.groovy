package com.smartbear.readyapi4j.dsl.assertions

import com.smartbear.readyapi4j.assertions.Assertions
import com.smartbear.readyapi4j.assertions.NotSoapFaultAssertionBuilder
import com.smartbear.readyapi4j.assertions.SoapFaultAssertionBuilder

class SoapRequestAssertionsDelegate extends AbstractAssertionsDelegate {
    SoapFaultAssertionBuilder getSoapFault() {
        SoapFaultAssertionBuilder soapFault = Assertions.soapFault()
        assertionBuilders.add(soapFault)
        return soapFault
    }

    NotSoapFaultAssertionBuilder getNotSoapFault() {
        NotSoapFaultAssertionBuilder notSoapFaultAssertionBuilder = Assertions.notSoapFault()
        assertionBuilders.add(notSoapFaultAssertionBuilder)
        return notSoapFaultAssertionBuilder
    }
}
