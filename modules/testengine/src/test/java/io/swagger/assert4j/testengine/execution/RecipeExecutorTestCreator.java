package io.swagger.assert4j.testengine.execution;

import io.swagger.assert4j.testengine.execution.Scheme;
import io.swagger.assert4j.testengine.execution.TestEngineApi;
import io.swagger.assert4j.testengine.execution.TestEngineClient;

public class RecipeExecutorTestCreator {
    public static TestEngineClient createRecipeExecutor(Scheme scheme, String host, int port, String basePath, TestEngineApi apiStub) {
        return new TestEngineClient(scheme, host, port, basePath, apiStub);
    }
}
