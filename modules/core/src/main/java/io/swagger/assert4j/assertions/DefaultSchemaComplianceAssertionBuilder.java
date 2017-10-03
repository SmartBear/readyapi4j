package io.swagger.assert4j.assertions;

import io.swagger.assert4j.client.model.SchemaComplianceAssertion;

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
