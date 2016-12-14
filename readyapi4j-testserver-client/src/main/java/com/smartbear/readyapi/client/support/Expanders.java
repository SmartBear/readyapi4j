package com.smartbear.readyapi.client.support;

/**
 * Utility methods to build property-expansion strings
 */

public class Expanders {
    public static final String DEFAULT_DATASOURCE_TESTSTEP_NAME = "DataSource 1";

    public static String expand(String testStep, String property, String path) {
        return "${" + testStep + "#" + property + "#" + path + "}";
    }

    public static String fromResponse(String testStep, String path) {
        return expand(testStep, "Response", path);
    }

    public static String script(String script) {
        return "${=" + script + "}";
    }

    public static String dataSourceProperty(String property) {
        return expand(DEFAULT_DATASOURCE_TESTSTEP_NAME, property);
    }

    public static String expand(String testStep, String property) {
        return "${" + testStep + "#" + property + "}";
    }
}
