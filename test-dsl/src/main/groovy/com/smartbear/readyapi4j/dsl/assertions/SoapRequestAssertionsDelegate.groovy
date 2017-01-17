package com.smartbear.readyapi4j.dsl.assertions

import com.smartbear.readyapi4j.assertions.Assertions


class SoapRequestAssertionsDelegate extends AbstractAssertionsDelegate {
    void soapFault() {
        assertionBuilders.add(Assertions.soapFault())
    }

    void notSoapFault() {
        assertionBuilders.add(Assertions.notSoapFault())
    }
}
