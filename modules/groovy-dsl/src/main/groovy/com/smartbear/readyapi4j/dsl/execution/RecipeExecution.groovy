package com.smartbear.readyapi4j.dsl.execution

import com.smartbear.readyapi4j.TestRecipe
import com.smartbear.readyapi4j.TestRecipeBuilder
import com.smartbear.readyapi4j.dsl.pro.ProDslDelegate
import com.smartbear.readyapi4j.execution.Execution
import com.smartbear.readyapi4j.execution.RecipeExecutor
import com.smartbear.readyapi4j.testengine.execution.TestEngineClient

class RecipeExecution {

    /**
     * Creates an instance of TestEngineClient for remote execution of recipes. Can be used in set-up methods in tests.
     * @param testEngineUrl https://<server hostname or ip> : <port>
     * @param username username
     * @param password password
     * @return TestEngineClient
     */
    static RecipeExecutor remoteRecipeExecutor(String testEngineUrl, String username, String password) {
        TestEngineClient testServerClient = TestEngineClient.fromUrl(testEngineUrl)
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
     * Executes recipe on TestEngine and returns execution results
     * @param executor
     * @param recipeDefinition
     * @return Execution : containing the execution result
     */
    static Execution executeRecipeOnTestEngine(String testEngineUrl, String username, String password,
                                               @DelegatesTo(ProDslDelegate) recipeDefinition) {
        return executeRecipe(remoteRecipeExecutor(testEngineUrl, username, password), recipeDefinition)
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
     * Submits recipe to TestEngine for asynchronous execution and returns current state of execution
     * @param executor
     * @param recipeDefinition
     * @return Execution : containing the execution result
     */
    static Execution submitRecipeToTestEngine(String testEngineUrl, String username, String password,
                                              @DelegatesTo(ProDslDelegate) recipeDefinition) {
        return submitRecipe(remoteRecipeExecutor(testEngineUrl, username, password), recipeDefinition)
    }

    /**
     * Executes recipe locally and returns execution results.
     * It requires dependency on com.smartbear.readyapi:readyapi4j-local or the corresponding jars in classpath.
     * @param executor
     * @param recipeDefinition
     * @return Execution : execution result
     */
    static Execution executeRecipe(@DelegatesTo(ProDslDelegate) recipeDefinition) {
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