package com.smartbear.readyapi.client.execution;

import javax.ws.rs.core.MultivaluedMap;

public class ApiException extends RuntimeException {
    private int statusCode;
    private String responseBody;
    private MultivaluedMap<String, String> headers;

    public ApiException(int statusCode, String responseBody, MultivaluedMap<String, String> headers) {
        this.statusCode = statusCode;
        this.responseBody = responseBody;
        this.headers = headers;
    }

    public ApiException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public ApiException(Exception e) {
        super(e);
    }

    @Override
    public String toString() {
        return getClass().getName() + "{" +
                "code=" + statusCode +
                ", message=" + getMessage() != null ? getMessage() : responseBody +
                ", responseHeaders=" + headers +
                ", responseBody='" + responseBody + '\'' +
                '}';
    }
}
