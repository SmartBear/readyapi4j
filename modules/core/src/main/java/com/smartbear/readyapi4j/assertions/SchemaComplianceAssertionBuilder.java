package com.smartbear.readyapi4j.assertions;

import com.smartbear.readyapi4j.client.model.SchemaComplianceAssertion;

public interface SchemaComplianceAssertionBuilder extends AssertionBuilder<SchemaComplianceAssertion> {
    SchemaComplianceAssertionBuilder named(String assertionName);
}
