package com.smartbear.readyapi.client.execution;

import javax.ws.rs.core.MultivaluedMap;

public class UsageLimitException extends ApiException {
    public UsageLimitException(int statusCode, String responseBody, MultivaluedMap<String, String> headers) {
        super(statusCode, responseBody, headers);
    }
}
