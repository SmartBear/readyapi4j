package com.smartbear.readyapi.client.execution;

public class RecipeExecutorTestCreator {
    public static TestServerClient createRecipeExecutor(Scheme scheme, String host, int port, String basePath, TestServerApi apiStub){
        return new TestServerClient(scheme, host, port, basePath, apiStub);
    }
}
