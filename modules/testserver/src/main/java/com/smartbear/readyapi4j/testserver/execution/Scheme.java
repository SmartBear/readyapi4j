package com.smartbear.readyapi4j.testserver.execution;

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
