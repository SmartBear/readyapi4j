package io.swagger.assert4j.testengine.execution;

/**
 * Enumeration for HTTP schemes
 */

public enum Scheme {
    HTTP("http"),
    HTTPS("https");

    private String value;

    Scheme(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
