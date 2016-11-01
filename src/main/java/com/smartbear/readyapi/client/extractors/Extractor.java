package com.smartbear.readyapi.client.extractors;

public class Extractor {
    private String path;
    private String property;
    private String source;

    private final ExtractorOperator operator;

    public Extractor(String extractorProperty, String extractorPath, ExtractorOperator operator){
        this.property = extractorProperty;
        this.path = extractorPath;
        this.operator = operator;
    }

    public Extractor(String extractorProperty, ExtractorOperator operator){
        this.property = extractorProperty;
        this.path = "";
        this.operator = operator;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path){
        this.path = path;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property){
        this.property = property;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public ExtractorOperator getOperator() {
        return operator;
    }
}
