package io.swagger.assert4j.samples.groovy

import io.swagger.assert4j.execution.Execution
import io.swagger.assert4j.execution.RecipeExecutor
import org.junit.Test

import static io.swagger.assert4j.dsl.execution.RecipeExecution.*

class RestRequestTest {

    private static final String TEST_SERVER_URL = 'http://testserver.readyapi.io:8080'
    private static final String TEST_SERVER_USER = 'demoUser'
    private static final String TEST_SERVER_PASSWORD = 'demoPassword'

    @Test
    void testSimpleCountUsingSoapUIEngine() throws Exception {
        //This requires the additional dependency io.swagger.assert:assert4j-local
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
        //The executor can be initialized in the setup method if used in multiple tests
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
