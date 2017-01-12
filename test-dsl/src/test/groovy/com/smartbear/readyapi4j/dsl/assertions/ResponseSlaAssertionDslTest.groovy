package com.smartbear.readyapi4j.dsl.assertions

import com.smartbear.readyapi.client.model.ResponseSLAAssertion
import com.smartbear.readyapi.client.model.RestTestRequestStep
import com.smartbear.readyapi4j.TestRecipe
import org.junit.Test

import static com.smartbear.readyapi4j.dsl.DataExtractor.extractFirstTestStep
import static com.smartbear.readyapi4j.dsl.TestDsl.recipe

class ResponseSlaAssertionDslTest {
    @Test
    void buildsSlaAssertionWithMilliseconds() throws Exception {
        TestRecipe recipe = recipe {
            get '/myurl', {
                asserting {
                    responseSLA 1000 milliseconds
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
                    responseSLA 1 second
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
                    responseSLA 20 seconds
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
                    responseSLA 1 minute
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
                    responseSLA 12 minute
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
