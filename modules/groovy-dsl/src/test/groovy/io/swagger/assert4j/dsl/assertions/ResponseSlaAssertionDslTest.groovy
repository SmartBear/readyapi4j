package io.swagger.assert4j.dsl.assertions

import io.swagger.assert4j.TestRecipe
import io.swagger.assert4j.client.model.ResponseSLAAssertion
import io.swagger.assert4j.client.model.RestTestRequestStep
import org.junit.Test

import static io.swagger.assert4j.dsl.DataExtractor.extractFirstTestStep
import static io.swagger.assert4j.dsl.TestDsl.recipe

class ResponseSlaAssertionDslTest {
    @Test
    void buildsSlaAssertionWithMilliseconds() throws Exception {
        TestRecipe recipe = recipe {
            get '/myurl', {
                asserting {
                    maxResponseTime 1000 milliseconds
                }
            }
        }
        ResponseSLAAssertion assertion = extractResponseSlaAssertion(recipe)
        assert assertion.maxResponseTime == '1000'
    }

    @Test
    void buildsSlaAssertionWithMs() throws Exception {
        TestRecipe recipe = recipe {
            get '/myurl', {
                asserting {
                    maxResponseTime 1000 ms
                }
            }
        }
        ResponseSLAAssertion assertion = extractResponseSlaAssertion(recipe)
        assert assertion.maxResponseTime == '1000'
    }

    @Test
    void buildsSlaAssertionWithOneSecond() throws Exception {
        TestRecipe recipe = recipe {
            get '/myurl', {
                asserting {
                    maxResponseTime 1 second
                }
            }
        }
        ResponseSLAAssertion assertion = extractResponseSlaAssertion(recipe)
        assert assertion.maxResponseTime == '1000'
    }

    @Test
    void buildsSlaAssertionWithSeconds() throws Exception {
        TestRecipe recipe = recipe {
            get '/myurl', {
                asserting {
                    maxResponseTime 20 seconds
                }
            }
        }
        ResponseSLAAssertion assertion = extractResponseSlaAssertion(recipe)
        assert assertion.maxResponseTime == '20000'
    }

    @Test
    void buildsSlaAssertionWithOneMinute() throws Exception {
        TestRecipe recipe = recipe {
            get '/myurl', {
                asserting {
                    maxResponseTime 1 minute
                }
            }
        }
        ResponseSLAAssertion assertion = extractResponseSlaAssertion(recipe)
        assert assertion.maxResponseTime == '60000'
    }

    @Test
    void buildsSlaAssertionWithMinutes() throws Exception {
        TestRecipe recipe = recipe {
            get '/myurl', {
                asserting {
                    maxResponseTime 12 minute
                }
            }
        }
        ResponseSLAAssertion assertion = extractResponseSlaAssertion(recipe)
        assert assertion.maxResponseTime == '720000'
    }

    private static ResponseSLAAssertion extractResponseSlaAssertion(TestRecipe recipe) {
        RestTestRequestStep testRequestStep = extractFirstTestStep(recipe) as RestTestRequestStep
        return testRequestStep.assertions[0] as ResponseSLAAssertion
    }
}
