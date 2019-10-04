package io.swagger.assert4j.testengine.execution;

public class RecipeExecutorTestCreator {
    public static TestEngineClient createRecipeExecutor(Scheme scheme, String host, int port, String basePath, TestEngineApi apiStub) {
        return new TestEngineClient(scheme, host, port, basePath, apiStub);
    }
}
