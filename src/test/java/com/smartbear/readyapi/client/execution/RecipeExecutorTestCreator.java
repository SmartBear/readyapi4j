package com.smartbear.readyapi.client.execution;

public class RecipeExecutorTestCreator {
    public static TestServerRequestExecutor createRecipeExecutor(Scheme scheme, String host, int port, String basePath, TestServerApi apiStub){
        return new TestServerRequestExecutor(scheme, host, port, basePath, apiStub);
    }
}
