package com.smartbear.readyapi.client.support;

/**
 * Utility methods to build property-expansion strings
 */

public class Expanders {
    public static final String DEFAULT_DATASOURCE_TESTSTEP_NAME = "DataSource";

    public static String expand(String property, String testStep, String path) {
        return "${" + testStep + "#" + property + "#" + path + "}";
    }

    public static String fromResponse(String testStep, String path) {
        return expand("Response", testStep, path);
    }

    public static String dataSourceProperty(String property) {
        return expand(property, DEFAULT_DATASOURCE_TESTSTEP_NAME);
    }

    public static String expand(String property, String testStep) {
        return "${" + testStep + "#" + property + "}";
    }
}
