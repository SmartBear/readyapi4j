package com.smartbear.readyapi.client.execution;

/**
 * Thrown when a recipe cannot be interpreted
 */
public class InvalidRecipeException extends RuntimeException {
    public InvalidRecipeException(String message, Exception cause) {
        super(message, cause);
    }
}
