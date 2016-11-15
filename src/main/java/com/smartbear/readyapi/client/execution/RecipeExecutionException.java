package com.smartbear.readyapi.client.execution;

/**
 * Thrown when Test recipe execution encounters an unexpected error.
 */
public class RecipeExecutionException extends RuntimeException {
    public RecipeExecutionException(String message, Throwable cause) {
        super(message, cause);
    }
}
