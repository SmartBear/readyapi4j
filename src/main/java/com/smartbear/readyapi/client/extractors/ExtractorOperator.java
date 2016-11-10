package com.smartbear.readyapi.client.extractors;

@FunctionalInterface
public interface ExtractorOperator {
    void extractValue(String value);
}
