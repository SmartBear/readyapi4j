package com.smartbear.readyapi.client.execution;

public class RecipeExecutorTestCreator {
    public static RecipeExecutor createRecipeExecutor(Scheme scheme, String host, int port, String basePath, SmartestApiWrapper apiStub){
        return new RecipeExecutor(scheme, host, port, basePath, apiStub);
    }
}
