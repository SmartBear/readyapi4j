package io.swagger.assert4j.dsl

/**
 * Delegate to support 'pluginProvidedStep' closures within the DSL's recipe closures.
 */
class PluginTestStepDelegate {
    String testStepName;
    String type;
    Map<String, Object> configuration;

    void name(String testStepName) {
        this.testStepName = testStepName
    }

    void type(String type) {
        this.type = type
    }

    void configuration(Map<String, Object> configuration) {
        this.configuration = configuration
    }
}
