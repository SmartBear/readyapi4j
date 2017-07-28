package io.swagger.assert4j.dsl.execution

import io.swagger.assert4j.TestRecipe
import io.swagger.assert4j.TestRecipeBuilder
import io.swagger.assert4j.dsl.pro.ProDslDelegate
import io.swagger.assert4j.execution.Execution
import io.swagger.assert4j.execution.RecipeExecutor
import io.swagger.assert4j.testserver.execution.TestServerClient

class RecipeExecution {

    /**
     * Creates an instance of TestServerClient for remote execution of recipes. Can be used in set-up methods in tests.
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
     * Executes recipe locally and returns execution results.
     * It requires dependency on io.swagger.assert:assert4j-local or the corresponding jars in classpath.
     * @param executor
     * @param recipeDefinition
     * @return Execution : execution result
     */
    static Execution executeRecipe(@DelegatesTo(ProDslDelegate) recipeDefinition) {
        TestRecipe testRecipe = createTestRecipe(recipeDefinition)
        Class soapUIRecipeExecutorClass
        try {
            soapUIRecipeExecutorClass = Class.forName("io.swagger.assert4j.local.execution.SoapUIRecipeExecutor")
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException('Could not instantiate local recipe executor.' +
                    ' Please add dependency to io.swagger.assert:assert4j-local or add the jars to classpath.')
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