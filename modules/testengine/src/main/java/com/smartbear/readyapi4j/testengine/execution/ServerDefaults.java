package com.smartbear.readyapi4j.testengine.execution;

/**
 * Holds constants with default server configuration for the ReadyAPI Runtime Service.
 */
public class ServerDefaults {

    public static final String VERSION_PREFIX = "/api/v1";

    private ServerDefaults() {
    }

    public static final String SERVICE_BASE_PATH = "";

    public static final int DEFAULT_PORT = 8443;
    public static final Scheme DEFAULT_SCHEME = Scheme.HTTPS;
}
