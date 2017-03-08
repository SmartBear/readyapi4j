package com.smartbear.readyapi4j.extractor;

@FunctionalInterface
public interface ExtractorOperator {
    void extractValue(String value);
}