package com.smartbear.readyapi.client.teststeps.soaprequest;

import com.smartbear.readyapi.client.assertions.NotSoapFaultAssertionBuilder;
import com.smartbear.readyapi.client.assertions.SchemaComplianceAssertionBuilder;
import com.smartbear.readyapi.client.assertions.SoapFaultAssertionBuilder;
import com.smartbear.readyapi.client.model.Parameter;
import com.smartbear.readyapi.client.model.SoapRequestTestStep;
import com.smartbear.readyapi.client.teststeps.TestStepTypes;
import com.smartbear.readyapi.client.teststeps.request.BaseRequestBuilder;

import static com.smartbear.readyapi.client.Validator.validateNotEmpty;

public class SoapRequestStepBuilder extends BaseRequestBuilder<SoapRequestStepBuilder, SoapRequestTestStep> {

    public SoapRequestStepBuilder() {
        super(new SoapRequestTestStep(), TestStepTypes.SOAP_REQUEST.getName());
    }

    @Override
    public SoapRequestTestStep build() {
        super.build();

        validateNotEmpty(testStep.getWsdl(), "Missing WSDL location");
        validateNotEmpty(testStep.getBinding(), "Missing WSDL Binding");
        validateNotEmpty(testStep.getOperation(), "Missing WSDL Operation");

        return testStep;
    }

    public SoapRequestStepBuilder withWsdl(String wsdl) {
        testStep.setWsdl(wsdl);
        return this;
    }

    public SoapRequestStepBuilder forOperation(String operation) {
        testStep.setOperation(operation);
        return this;
    }

    public SoapRequestStepBuilder forBinding(String binding) {
        testStep.setBinding(binding);
        return this;
    }

    public SoapRequestStepBuilder withEndpoint(String endpoint) {
        testStep.setURI(endpoint);
        return this;
    }

    public SoapRequestStepBuilder assertSchemaCompliance() {
        return addAssertion(new SchemaComplianceAssertionBuilder());
    }

    public SoapRequestStepBuilder assertSoapFault() {
        return addAssertion(new SoapFaultAssertionBuilder());
    }

    public SoapRequestStepBuilder assertNotSoapFault() {
        return addAssertion(new NotSoapFaultAssertionBuilder());
    }

    public SoapRequestStepBuilder withParameter(String name, String value) {
        Parameter parameter = new Parameter();
        parameter.setName(name);
        parameter.setValue(value);

        getParameters().add(parameter);
        return this;
    }

    public SoapRequestStepBuilder withPathParameter(String path, String value) {
        Parameter parameter = new Parameter();
        parameter.setPath(path);
        parameter.setValue(value);

        getParameters().add(parameter);
        return this;
    }
}
