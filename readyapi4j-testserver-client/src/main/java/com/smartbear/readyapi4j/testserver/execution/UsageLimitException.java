package com.smartbear.readyapi4j.testserver.execution;

import javax.ws.rs.core.MultivaluedMap;

/**
 * Exception thrown when attempting to execute tests once the execution quota on the server has been reached
 */

public class UsageLimitException extends ApiException {
    public UsageLimitException(int statusCode, String responseBody, MultivaluedMap<String, String> headers) {
        super(statusCode, responseBody, headers);
    }
}
