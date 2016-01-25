package com.smartbear.readyapi.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartbear.readyapi.client.execution.ApiException;
import com.smartbear.readyapi.client.model.TestCase;

public class TestRecipe {
    private final TestCase testCase;

    public TestRecipe(TestCase testCase) {
        this.testCase = testCase;
    }

    public TestCase getTestCase() {
        return testCase;
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(testCase);
        } catch (JsonProcessingException e) {
            throw new ApiException(e);
        }
    }
}
