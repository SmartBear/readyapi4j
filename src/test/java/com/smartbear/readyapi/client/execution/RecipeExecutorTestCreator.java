package com.smartbear.readyapi.client.execution;

public class RecipeExecutorTestCreator {
    public static RecipeExecutor createRecipeExecutor(Scheme scheme, String host, int port, String basePath, TestServerApi apiStub){
        return new RecipeExecutor(scheme, host, port, basePath, apiStub);
    }
}
