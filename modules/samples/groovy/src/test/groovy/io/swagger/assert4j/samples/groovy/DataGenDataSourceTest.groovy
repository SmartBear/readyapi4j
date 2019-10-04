package io.swagger.assert4j.samples.groovy

import io.swagger.assert4j.execution.Execution
import org.junit.Test

import static io.swagger.assert4j.dsl.execution.RecipeExecution.executeRecipeOnTestEngine

class DataGenDataSourceTest {
    private static final String TEST_SERVER_URL = 'http://testengine.readyapi.io:8080'
    private static final String TEST_SERVER_USER = 'demoUser'
    private static final String TEST_SERVER_PASSWORD = 'demoPassword'

    @Test
    void dataGenDataSourceTest() throws Exception {
        //Data Source test step is a Pro test step and hence can be used with TestEngine, but not with SoapUI OS engine and local execution
        Execution execution = executeRecipeOnTestEngine(TEST_SERVER_URL, TEST_SERVER_USER, TEST_SERVER_PASSWORD, {
            withGeneratedData 'DataSourceTestStep', {
                numberOfRows 10
                cityName 'City'
                mac48ComputerAddress 'Mac48Address'
                randomInteger 'RandomInteger', {
                    minimumValue 1
                    maximumValue 32
                }
                testSteps {
                    get 'http://www.google.se/', {
                        parameters {
                            query 'a', '${DataSourceStep#City}'
                            query 'b', '${DataSourceStep#Mac48Address'
                            query 'c', '${DataSourceStep#RandomInteger'
                        }
                    }
                }
            }
        })

        assert execution.currentStatus == FINISHED
        assert execution.errorMessages.empty
    }
}
