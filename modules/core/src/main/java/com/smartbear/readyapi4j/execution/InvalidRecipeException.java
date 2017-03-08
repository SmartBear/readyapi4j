package com.smartbear.readyapi4j.execution;

/**
 * Thrown when a recipe cannot be interpreted
 */
public class InvalidRecipeException extends RuntimeException {
    public InvalidRecipeException(String message, Exception cause) {
        super(message, cause);
    }
}
