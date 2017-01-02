package com.smartbear.readyapi4j.dsl

import com.smartbear.readyapi.client.model.Assertion
import com.smartbear.readyapi.client.model.RestTestRequestStep
import com.smartbear.readyapi.client.model.SimpleContainsAssertion
import com.smartbear.readyapi.client.model.ValidHttpStatusCodesAssertion
import com.smartbear.readyapi4j.TestRecipe
import org.junit.Test

import static TestDsl.recipe
import static com.smartbear.readyapi4j.dsl.DataExtractor.extractFirstTestStep
import static org.hamcrest.CoreMatchers.is
import static org.junit.Assert.assertThat
import static org.junit.Assert.assertTrue

class RestRequestDslTest {

    public static final String URI = "/uri_to_get"

    @Test
    void buildsRecipeWithGET() throws Exception {
        TestRecipe recipe = recipe {
            GET(URI)
        }

        verifyValuesAndMethod(recipe, 'GET')
    }

    @Test
    void buildsRecipeWithPOST() throws Exception {
        TestRecipe recipe = recipe {
            POST(URI)
        }

        verifyValuesAndMethod(recipe, 'POST')
    }

    @Test
    void buildsRecipeWithPUT() throws Exception {
        TestRecipe recipe = recipe {
            PUT(URI)
        }

        verifyValuesAndMethod(recipe, 'PUT')
    }

    @Test
    void buildsRecipeWithDELETE() throws Exception {
        TestRecipe recipe = recipe {
            DELETE(URI)
        }

        verifyValuesAndMethod(recipe, 'DELETE')
    }

    @Test
    void parameterizesRestRequest() throws Exception {
        String stepName = 'theGET'
        TestRecipe recipe = recipe {
            //Bug in the IntelliJ Groovyc - need parentheses here to make it compile!
            GET ('/some_uri', {
                name stepName
                headers (['Cache-Control': 'nocache'])
                followRedirects true
                entitizeParameters true
                postQueryString true
                timeout 5000
            })
        }

        RestTestRequestStep restRequest = extractFirstTestStep(recipe) as RestTestRequestStep
        assertThat(restRequest.name, is(stepName))
        assertThat(restRequest.headers['Cache-Control'] as List<String>, is(['nocache']))
        assertTrue('Not respecting followRedirects', restRequest.followRedirects)
        assertTrue('Not respecting entitizeParameters', restRequest.entitizeParameters)
        assertTrue('Not respecting postQueryString', restRequest.postQueryString)
        assertThat(restRequest.timeout, is ('5000'))
    }

    @Test
    void createsAssertions() throws Exception {
        String stepName = 'theGET'
        TestRecipe recipe = recipe {
            //Bug in the IntelliJ Groovyc - need parentheses here to make it compile!
            GET ('/some_uri', {
                assertions {
                    status 200
                    responseContains 'Arrival', useRegexp: true
                }
            })
        }

        RestTestRequestStep restRequest = extractFirstTestStep(recipe) as RestTestRequestStep
        assertThat(restRequest.assertions.size(), is(2))
        ValidHttpStatusCodesAssertion statusAssertion = restRequest.assertions[0] as ValidHttpStatusCodesAssertion
        assertThat(statusAssertion.validStatusCodes, is (['200']))
        SimpleContainsAssertion containsAssertion = restRequest.assertions[1] as SimpleContainsAssertion
        assertThat(containsAssertion.token, is('Arrival'))
        assertTrue(containsAssertion.useRegexp)
    }


    private static void verifyValuesAndMethod(TestRecipe recipe, String method) {
        RestTestRequestStep restRequest = extractFirstTestStep(recipe) as RestTestRequestStep
        assertThat(restRequest.URI, is(URI))
        assertThat(restRequest.method, is(method))
    }

}
