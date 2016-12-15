package com.smartbear.readyapi4j;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartbear.readyapi.client.model.TestCase;
import com.smartbear.readyapi4j.extractor.ExtractorData;

public class TestRecipe {
    private final TestCase testCase;
    private ExtractorData extractorData;

    public TestRecipe(TestCase testCase, ExtractorData extractorData) {
        this.testCase = testCase;
        this.extractorData = extractorData;
    }

    public TestCase getTestCase() {
        return testCase;
    }

    public ExtractorData getExtractorData() {
        return extractorData;
    }

    @Override
    public String toString() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
            return objectMapper.writeValueAsString(testCase);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
