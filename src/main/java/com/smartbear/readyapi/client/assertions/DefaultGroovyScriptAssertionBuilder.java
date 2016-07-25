package com.smartbear.readyapi.client.assertions;

import com.smartbear.readyapi.client.model.GroovyScriptAssertion;

import static com.smartbear.readyapi.client.Validator.validateNotEmpty;

public class DefaultGroovyScriptAssertionBuilder extends AbstractAssertionBuilder<GroovyScriptAssertion> {
    private GroovyScriptAssertion scriptAssertion = new GroovyScriptAssertion();

    public DefaultGroovyScriptAssertionBuilder(String script) {
        scriptAssertion.setScript(script);
    }

    public DefaultGroovyScriptAssertionBuilder named(String name) {
        scriptAssertion.setName(name);
        return this;
    }

    @Override
    public GroovyScriptAssertion build() {
        validateNotEmpty(scriptAssertion.getScript(), "Missing script. Script is a mandatory parameter for ScriptAssertion");
        scriptAssertion.setType(Assertions.SCRIPT_ASSERTION_TYPE);
        return scriptAssertion;
    }

    public final static GroovyScriptAssertion create(){
        GroovyScriptAssertion assertion = new GroovyScriptAssertion();
        assertion.setType(Assertions.SCRIPT_ASSERTION_TYPE);
        return assertion;
    }
}
