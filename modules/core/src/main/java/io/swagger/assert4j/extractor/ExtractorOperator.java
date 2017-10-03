package io.swagger.assert4j.extractor;

@FunctionalInterface
public interface ExtractorOperator {
    void extractValue(String value);
}