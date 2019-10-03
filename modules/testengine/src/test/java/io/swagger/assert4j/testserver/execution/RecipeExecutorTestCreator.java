package io.swagger.assert4j.testserver.execution;

public class RecipeExecutorTestCreator {
    public static TestServerClient createRecipeExecutor(Scheme scheme, String host, int port, String basePath, TestEngineApi apiStub) {
        return new TestServerClient(scheme, host, port, basePath, apiStub);
    }
}
