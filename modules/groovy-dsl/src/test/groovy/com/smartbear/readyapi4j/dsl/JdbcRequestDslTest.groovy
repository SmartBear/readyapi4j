package com.smartbear.readyapi4j.dsl

import com.smartbear.readyapi4j.TestRecipe
import com.smartbear.readyapi4j.assertions.AssertionNames
import com.smartbear.readyapi4j.client.model.Assertion
import com.smartbear.readyapi4j.client.model.JdbcRequestTestStep
import com.smartbear.readyapi4j.client.model.JdbcStatusAssertion
import com.smartbear.readyapi4j.client.model.JdbcTimeoutAssertion
import org.junit.Test

class JdbcRequestDslTest {
    @Test
    void createsSimpleJdbcRequest() throws Exception {
        TestRecipe recipe = TestDsl.recipe {
            jdbcRequest {
                name 'Jdbc'
                driver 'org.h2.Driver'
                connectionString 'jdbc:h2:~/.readyapi/db/readyapi'
                query 'select * from customers'
                storedProcedure false
                testStepProperties property1: 'value1', property2: 'value2'
            }
        }

        JdbcRequestTestStep jdbcRequestStep = extractJdbcRequestStep(recipe)
        assert jdbcRequestStep.name == 'Jdbc'
        assert jdbcRequestStep.driver == 'org.h2.Driver'
        assert jdbcRequestStep.connectionString == 'jdbc:h2:~/.readyapi/db/readyapi'
        assert jdbcRequestStep.sqlQuery == 'select * from customers'
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
        assert assertion.type == AssertionNames.JDBC_STATUS
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
        assert assertion.type == AssertionNames.JDBC_TIMEOUT
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
