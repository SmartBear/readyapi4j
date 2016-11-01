package com.smartbear.readyapi.client.extractors;

public class Extractors {

    public static Extractor extractorProperty(String property, ExtractorOperator operator) {
        return new Extractor(property, operator);
    }

    public static Extractor extractorPath(String property, String path, ExtractorOperator operator) {
        return new Extractor(property, path, operator);
    }
}
