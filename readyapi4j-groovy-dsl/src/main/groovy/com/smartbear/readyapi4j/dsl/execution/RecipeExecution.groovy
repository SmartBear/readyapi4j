package com.smartbear.readyapi4j.dsl.execution

import com.smartbear.readyapi4j.TestRecipe
import com.smartbear.readyapi4j.TestRecipeBuilder
import com.smartbear.readyapi4j.dsl.pro.ProDslDelegate
import com.smartbear.readyapi4j.execution.Execution
import com.smartbear.readyapi4j.execution.RecipeExecutor
import com.smartbear.readyapi4j.testserver.execution.TestServerClient

class RecipeExecution {

    /**
     * Creates an instance of TestServerClient for remote execution of recipes.
     * @param testServerUrl https://<server hostname or ip> : <port>
     * @param username username
     * @param password password
     * @return TestServerClient
     */
    static RecipeExecutor remoteRecipeExecutor(String testServerUrl, String username, String password) {
        TestServerClient testServerClient = TestServerClient.fromUrl(testServerUrl)
        testServerClient.setCredentials(username, password)
        testServerClient.createRecipeExecutor()
    }

    /**
     * Executes recipe and returns execution results
     * @param executor
     * @param recipeDefinition
     * @return Execution : containing the execution result
     */
    static Execution executeRecipe(RecipeExecutor executor, @DelegatesTo(ProDslDelegate) recipeDefinition) {
        TestRecipe testRecipe = createTestRecipe(recipeDefinition)
        return executor.executeRecipe(testRecipe)
    }

    /**
     * Executes recipe on TestServer and returns execution results
     * @param executor
     * @param recipeDefinition
     * @return Execution : containing the execution result
     */
    static Execution executeRecipeOnServer(String testServerUrl, String username, String password,
                                           @DelegatesTo(ProDslDelegate) recipeDefinition) {
        return executeRecipe(remoteRecipeExecutor(testServerUrl, username, password), recipeDefinition)
    }

    /**
     * Asynchronous execution: submits recipe for execution and returns current state of execution.
     * @param executor
     * @param recipeDefinition
     * @return Execution : with current state of execution
     */
    static Execution submitRecipe(RecipeExecutor executor, @DelegatesTo(ProDslDelegate) recipeDefinition) {
        TestRecipe testRecipe = createTestRecipe(recipeDefinition)
        return executor.submitRecipe(testRecipe)
    }

    /**
     * Submits recipe to TestServer for asynchronous execution and returns current state of execution
     * @param executor
     * @param recipeDefinition
     * @return Execution : containing the execution result
     */
    static Execution submitRecipeToServer(String testServerUrl, String username, String password,
                                          @DelegatesTo(ProDslDelegate) recipeDefinition) {
        return submitRecipe(remoteRecipeExecutor(testServerUrl, username, password), recipeDefinition)
    }

    /**
     * Asynchronous execution: submits recipe for execution and returns current state of execution.
     * @param executor
     * @param recipeDefinition
     * @return Execution : with current state of execution
     */
    static Execution executeRecipeLocally(@DelegatesTo(ProDslDelegate) recipeDefinition) {
        TestRecipe testRecipe = createTestRecipe(recipeDefinition)
        Class soapUIRecipeExecutorClass
        try {
            soapUIRecipeExecutorClass = Class.forName("com.smartbear.readyapi4j.local.execution.SoapUIRecipeExecutor")
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException('Could not instantiate local recipe executor.' +
                    ' Please add dependency to com.smartbear.readyapi:readyapi4j-local or add the jars to classpath.')
        }
        def soapUIRecipeExecutor = soapUIRecipeExecutorClass.newInstance()
        return soapUIRecipeExecutor.executeRecipe(testRecipe)
    }

    private static TestRecipe createTestRecipe(recipeDefinition) {
        TestRecipeBuilder testRecipeBuilder = TestRecipeBuilder.newTestRecipe()
        ProDslDelegate delegate = new ProDslDelegate(testRecipeBuilder)
        recipeDefinition.delegate = delegate
        recipeDefinition.call()
        return testRecipeBuilder.buildTestRecipe()
    }
}