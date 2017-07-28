package io.swagger.assert4j.assertions;

import io.swagger.assert4j.client.model.GroovyScriptAssertion;

public interface GroovyScriptAssertionBuilder extends AssertionBuilder<GroovyScriptAssertion> {
    GroovyScriptAssertionBuilder named(String assertionName);
}
