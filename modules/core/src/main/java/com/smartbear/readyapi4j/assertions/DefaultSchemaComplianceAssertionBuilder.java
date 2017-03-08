package com.smartbear.readyapi4j.assertions;

import com.smartbear.readyapi.client.model.SchemaComplianceAssertion;
import com.smartbear.readyapi4j.AssertionNames;

public class DefaultSchemaComplianceAssertionBuilder
        implements SchemaComplianceAssertionBuilder {

    private final SchemaComplianceAssertion schemaComplianceAssertion = new SchemaComplianceAssertion();

    @Override
    public DefaultSchemaComplianceAssertionBuilder named(String name) {
        schemaComplianceAssertion.setName(name);
        return this;
    }

    @Override
    public SchemaComplianceAssertion build() {
        schemaComplianceAssertion.setType(AssertionNames.SCHEMA_COMPLIANCE);
        return schemaComplianceAssertion;
    }

    public final static SchemaComplianceAssertion create() {
        return new DefaultSchemaComplianceAssertionBuilder().build();
    }
}
