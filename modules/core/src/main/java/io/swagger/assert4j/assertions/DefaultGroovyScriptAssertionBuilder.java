package io.swagger.assert4j.assertions;

import io.swagger.assert4j.client.model.GroovyScriptAssertion;

import static io.swagger.assert4j.support.Validations.validateNotEmpty;

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
