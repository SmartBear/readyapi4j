package com.smartbear.readyapi4j.assertions;

import com.smartbear.readyapi.client.model.GroovyScriptAssertion;
import com.smartbear.readyapi4j.AssertionNames;

import static com.smartbear.readyapi4j.Validator.validateNotEmpty;

public class DefaultGroovyScriptAssertionBuilder implements GroovyScriptAssertionBuilder {
    private GroovyScriptAssertion scriptAssertion = new GroovyScriptAssertion();

    public DefaultGroovyScriptAssertionBuilder(String script) {
        scriptAssertion.setScript(script);
    }

    @Override
    public DefaultGroovyScriptAssertionBuilder named(String name) {
        scriptAssertion.setName(name);
        return this;
    }

    @Override
    public GroovyScriptAssertion build() {
        validateNotEmpty(scriptAssertion.getScript(), "Missing script. Script is a mandatory parameter for ScriptAssertion");
        scriptAssertion.setType(AssertionNames.GROOVY_SCRIPT);
        return scriptAssertion;
    }

    public final static GroovyScriptAssertion create() {
        GroovyScriptAssertion assertion = new GroovyScriptAssertion();
        assertion.setType(AssertionNames.GROOVY_SCRIPT);
        return assertion;
    }
}
