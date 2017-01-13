package com.smartbear.readyapi4j.testserver.execution;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.smartbear.readyapi.client.model.RestTestRequestStep;
import com.smartbear.readyapi.client.model.TestStep;

import java.io.IOException;

public class TestStepDeserializerModule extends SimpleModule {
    public TestStepDeserializerModule() {
        super("TestStepDeserializerModule", new Version(0, 1, 0, "alpha"));
        this.addDeserializer(TestStep.class, new TestStepDeserializer());
    }

    public class TestStepDeserializer extends JsonDeserializer<TestStep> {
        @Override
        public TestStep deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            return (TestStep) jsonParser.readValuesAs(RestTestRequestStep.class);
        }
    }
}
