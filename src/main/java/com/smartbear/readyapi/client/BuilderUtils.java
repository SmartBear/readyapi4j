package com.smartbear.readyapi.client;

public class BuilderUtils {
    public static String randomName(String prefix) {
        return prefix + " " + String.valueOf(Math.random());
    }
}
