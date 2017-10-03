package io.swagger.assert4j;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Utility class for validations
 */

public class Validator {
    public static void validateNotEmpty(String value, String message) {
        if (StringUtils.isBlank(value)) {
            throw new IllegalStateException(message);
        }
    }

    public static void validateNotEmpty(Integer value, String message) {
        if (value == null) {
            throw new IllegalStateException(message);
        }
    }

    public static void validateNotEmpty(List<? extends Object> values, String message) {
        if (values == null || values.isEmpty()) {
            throw new IllegalStateException(message);
        }
    }
}
