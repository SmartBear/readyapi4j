package com.smartbear.readyapi4j.execution;

import javax.ws.rs.core.MultivaluedMap;

public class UsageLimitException extends ApiException {
    public UsageLimitException(int statusCode, String responseBody, MultivaluedMap<String, String> headers) {
        super(statusCode, responseBody, headers);
    }
}
