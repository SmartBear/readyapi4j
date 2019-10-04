package com.smartbear.readyapi4j.properties;

/**
 * Utility class for creating a PropertyBuilder
 */

public class Properties {
    public static PropertyBuilder property(String key, String value) {
        return PropertyBuilder.getInstance().withKey(key).withValue(value);
    }
}
