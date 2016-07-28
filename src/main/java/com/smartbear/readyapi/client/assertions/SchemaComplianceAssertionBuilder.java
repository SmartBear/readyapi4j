package com.smartbear.readyapi.client.assertions;

import com.smartbear.readyapi.client.model.SchemaComplianceAssertion;

public class SchemaComplianceAssertionBuilder extends AbstractAssertionBuilder<SchemaComplianceAssertion> {

    private final SchemaComplianceAssertion schemaComplianceAssertion = new SchemaComplianceAssertion();

    public SchemaComplianceAssertionBuilder named(String name) {
        schemaComplianceAssertion.setName(name);
        return this;
    }

    @Override
    public SchemaComplianceAssertion build() {
        schemaComplianceAssertion.setType(Assertions.SCHEMA_COMPLIANCE_TYPE);
        return schemaComplianceAssertion;
    }

    public final static SchemaComplianceAssertion create() {
        return new SchemaComplianceAssertionBuilder().build();
    }
}
