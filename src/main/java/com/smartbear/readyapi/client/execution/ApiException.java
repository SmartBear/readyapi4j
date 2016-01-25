package com.smartbear.readyapi.client.execution;

import com.fasterxml.jackson.core.JsonProcessingException;

import javax.ws.rs.core.MultivaluedMap;

public class ApiException extends RuntimeException {
    private String message;
    private int statusCode;
    private String responseBody;
    private MultivaluedMap<String, String> headers;

    public ApiException(int statusCode, String responseBody, MultivaluedMap<String, String> headers) {
        this.statusCode = statusCode;
        this.responseBody = responseBody;
        this.headers = headers;
    }

    public ApiException(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public ApiException(Exception e) {
        super(e);
    }

    @Override
    public String toString() {
        return getClass().getName() + "{" +
                "code=" + statusCode +
                ", message=" + message != null ? message : responseBody +
                ", responseHeaders=" + headers +
                ", responseBody='" + responseBody + '\'' +
                '}';
    }
}
