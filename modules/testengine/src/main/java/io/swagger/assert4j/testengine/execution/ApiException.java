package io.swagger.assert4j.testengine.execution;

import javax.ws.rs.core.MultivaluedMap;

/**
 * Exception thrown for failed TestEngine API calls
 */

public class ApiException extends RuntimeException {
    private int statusCode;
    private String responseBody;
    private MultivaluedMap<String, String> headers;

    public ApiException(int statusCode, String responseBody, MultivaluedMap<String, String> headers) {
        super(responseBody);
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
        return new StringBuilder(getClass().getName())
                .append("{")
                .append("code=").append(statusCode)
                .append(", message=").append(getMessage() != null ? getMessage() : responseBody)
                .append(", responseHeaders=").append(headers)
                .append(", responseBody='").append(responseBody).append("\'")
                .append("}").toString();
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public MultivaluedMap<String, String> getHeaders() {
        return headers;
    }
}
