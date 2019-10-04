package com.smartbear.readyapi4j.assertions;

import com.smartbear.readyapi4j.client.model.GroovyScriptAssertion;

public interface GroovyScriptAssertionBuilder extends AssertionBuilder<GroovyScriptAssertion> {
    GroovyScriptAssertionBuilder named(String assertionName);
}
