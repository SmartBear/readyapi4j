package com.smartbear.readyapi.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartbear.readyapi.client.execution.ApiException;
import com.smartbear.readyapi.client.extractors.ExtractorData;
import com.smartbear.readyapi.client.model.TestCase;

import java.util.Optional;

public class TestRecipe {
    private final TestCase testCase;
    private ExtractorData extractorData;

    public TestRecipe(TestCase testCase, ExtractorData extractorData) {
        this.testCase = testCase;
        this.extractorData = extractorData;
    }

    public TestRecipe(TestCase testCase){
        this.testCase = testCase;
    }

    public TestCase getTestCase() {
        return testCase;
    }

    public ExtractorData getExtractorData(){
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
            throw new ApiException(e);
        }
    }
}
