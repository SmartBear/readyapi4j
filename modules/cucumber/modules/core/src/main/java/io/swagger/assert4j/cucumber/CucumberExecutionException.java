package io.swagger.assert4j.cucumber;

public class CucumberExecutionException extends RuntimeException {
    public CucumberExecutionException(String message) {
        super(message);
    }

    public CucumberExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

    public CucumberExecutionException(Throwable cause) {
        super(cause);
    }
}
