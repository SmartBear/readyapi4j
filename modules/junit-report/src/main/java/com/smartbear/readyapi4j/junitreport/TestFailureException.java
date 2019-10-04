package com.smartbear.readyapi4j.junitreport;

/**
 * Thrown when a test failure is detected.
 */
public class TestFailureException extends RuntimeException {
    public TestFailureException(String message) {
        super(message);
    }
}
