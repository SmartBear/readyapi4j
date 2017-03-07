package com.smartbear.readyapi4j.samples.groovy

import com.smartbear.readyapi4j.execution.Execution
import com.smartbear.readyapi4j.execution.RecipeExecutor
import org.junit.Test

import static com.smartbear.readyapi.client.model.ProjectResultReport.StatusEnum.FINISHED
import static com.smartbear.readyapi4j.dsl.execution.RecipeExecution.executeRecipe
import static com.smartbear.readyapi4j.dsl.execution.RecipeExecution.executeRecipeOnServer
import static com.smartbear.readyapi4j.dsl.execution.RecipeExecution.remoteRecipeExecutor

class RestRequestTest {

    private static final String TEST_SERVER_URL = 'http://testserver.readyapi.io:8080'
    private static final String TEST_SERVER_USER = 'demoUser'
    private static final String TEST_SERVER_PASSWORD = 'demoPassword'

    @Test
    void testSimpleCountUsingSoapUIEngine() throws Exception {
        //This requires additional dependency com.smartbear.readyapi:readyapi4j-local
        Execution execution = executeRecipe {
            get 'https://api.swaggerhub.com/apis', {
                parameters {
                    query 'query', 'testserver'
                }
                asserting {
                    status 200
                    jsonPath '$.totalCount' occurs 1 times
                }
            }
        }
        assert execution.currentStatus == FINISHED
        assert execution.errorMessages.empty
    }

    @Test
    void testSimpleCountByRunningRecipeOnTestServer() throws Exception {
        Execution execution = executeRecipeOnServer TEST_SERVER_URL, TEST_SERVER_USER, TEST_SERVER_PASSWORD, {
            get 'https://api.swaggerhub.com/apis', {
                parameters {
                    query 'query', 'testserver'
                }
                asserting {
                    status 200
                    jsonPath '$.totalCount' occurs 1 times
                }
            }
        }
        assert execution.currentStatus == FINISHED
        assert execution.errorMessages.empty
    }

    @Test
    void testSimpleCountByRunningWithTheProvidedExecutor() throws Exception {
        //The executor can be initialized in set-up method if used in multiple tests
        RecipeExecutor executor = remoteRecipeExecutor(TEST_SERVER_URL, TEST_SERVER_USER, TEST_SERVER_PASSWORD)

        Execution execution = executeRecipe executor, {
            get 'https://api.swaggerhub.com/apis', {
                parameters {
                    query 'query', 'testserver'
                }
                asserting {
                    status 200
                    jsonPath '$.totalCount' occurs 1 times
                }
            }
        }
        assert execution.currentStatus == FINISHED
        assert execution.errorMessages.empty
    }
}
