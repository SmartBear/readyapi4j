package io.swagger.assert4j.assertions;

import io.swagger.assert4j.client.model.SchemaComplianceAssertion;

public interface SchemaComplianceAssertionBuilder extends AssertionBuilder<SchemaComplianceAssertion> {
    SchemaComplianceAssertionBuilder named(String assertionName);
}
