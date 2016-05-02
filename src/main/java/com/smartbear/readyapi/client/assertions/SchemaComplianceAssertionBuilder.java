package com.smartbear.readyapi.client.assertions;

import com.smartbear.readyapi.client.model.SchemaComplianceAssertion;

public class SchemaComplianceAssertionBuilder extends AbstractAssertionBuilder<SchemaComplianceAssertion> {

    @Override
    public SchemaComplianceAssertion build() {
        SchemaComplianceAssertion assertion = new SchemaComplianceAssertion();
        assertion.setType(Assertions.SCHEMA_COMPLIANCE_TYPE);
        return assertion;
    }

    public final static SchemaComplianceAssertion create() {
        return new SchemaComplianceAssertionBuilder().build();
    }
}
