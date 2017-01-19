package com.smartbear.readyapi4j.dsl

import com.smartbear.readyapi.client.model.Assertion
import com.smartbear.readyapi.client.model.JdbcRequestTestStep
import com.smartbear.readyapi.client.model.JdbcStatusAssertion
import com.smartbear.readyapi.client.model.JdbcTimeoutAssertion
import com.smartbear.readyapi4j.TestRecipe
import org.junit.Test

import static com.smartbear.readyapi4j.assertions.Assertions.JDBC_STATUS_TYPE
import static com.smartbear.readyapi4j.assertions.Assertions.JDBC_TIMEOUT_TYPE

class JdbcRequestDslTest {
    @Test
    void createsSimpleJdbcRequest() throws Exception {
        TestRecipe recipe = TestDsl.recipe {
            jdbcRequest {
                name 'Jdbc'
                driver 'org.h2.Driver'
                connectionString 'jdbc:h2:~/.readyapi/db/readyapi'
                storedProcedure false
                testStepProperties property1: 'value1', property2: 'value2'
            }
        }

        JdbcRequestTestStep jdbcRequestStep = extractJdbcRequestStep(recipe)
        assert jdbcRequestStep.name == 'Jdbc'
        assert jdbcRequestStep.driver == 'org.h2.Driver'
        assert jdbcRequestStep.connectionString == 'jdbc:h2:~/.readyapi/db/readyapi'
        assert !jdbcRequestStep.storedProcedure
        assert jdbcRequestStep.properties == [property1: 'value1', property2: 'value2']
    }

    @Test
    void createsJdbcStatusOkAssertion() throws Exception {
        TestRecipe recipe = TestDsl.recipe {
            jdbcRequest {
                driver 'org.h2.Driver'
                connectionString 'jdbc:h2:~/.readyapi/db/readyapi'
                asserting {
                    jdbcStatusOk
                }
            }
        }

        JdbcStatusAssertion assertion = extractFirstAssertion(recipe) as JdbcStatusAssertion
        assert assertion.type == JDBC_STATUS_TYPE
    }

    @Test
    void createsJdbcTimeoutAssertion() throws Exception {
        TestRecipe recipe = TestDsl.recipe {
            jdbcRequest {
                driver 'org.h2.Driver'
                connectionString 'jdbc:h2:~/.readyapi/db/readyapi'
                asserting {
                    timeout 100 seconds
                }
            }
        }

        JdbcTimeoutAssertion assertion = extractFirstAssertion(recipe) as JdbcTimeoutAssertion
        assert assertion.type == JDBC_TIMEOUT_TYPE
        assert assertion.timeout == '100000'
    }

    private static JdbcRequestTestStep extractJdbcRequestStep(TestRecipe testRecipe) {
        return testRecipe.testCase.testSteps[0] as JdbcRequestTestStep
    }

    private static Assertion extractFirstAssertion(TestRecipe testRecipe) {
        JdbcRequestTestStep jdbcRequestTestStep = testRecipe.testCase.testSteps[0] as JdbcRequestTestStep
        return jdbcRequestTestStep.assertions[0]
    }
}
