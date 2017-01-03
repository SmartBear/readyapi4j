package com.smartbear.readyapi4j.dsl

import com.smartbear.readyapi.client.model.RestTestRequestStep
import com.smartbear.readyapi.client.model.SimpleContainsAssertion
import com.smartbear.readyapi.client.model.ValidHttpStatusCodesAssertion
import com.smartbear.readyapi4j.TestRecipe
import org.junit.Test

import static TestDsl.recipe
import static com.smartbear.readyapi4j.dsl.DataExtractor.extractFirstTestStep

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
        assert restRequest.name == stepName
        assert restRequest.headers['Cache-Control'] == ['nocache']
        assert restRequest.followRedirects : 'Not respecting followRedirects'
        assert restRequest.entitizeParameters : 'Not respecting entitizeParameters'
        assert restRequest.postQueryString : 'Not respecting postQueryString'
        assert restRequest.timeout == '5000'
    }

    @Test
    void createsAssertions() throws Exception {
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
        assert restRequest.assertions.size() == 2
        ValidHttpStatusCodesAssertion statusAssertion = restRequest.assertions[0] as ValidHttpStatusCodesAssertion
        assert statusAssertion.validStatusCodes == ['200']
        SimpleContainsAssertion containsAssertion = restRequest.assertions[1] as SimpleContainsAssertion
        assert containsAssertion.token == 'Arrival'
        assert containsAssertion : 'Not respecting useRegexp'
    }

    private static void verifyValuesAndMethod(TestRecipe recipe, String method) {
        RestTestRequestStep restRequest = extractFirstTestStep(recipe) as RestTestRequestStep
        assert restRequest.URI == URI
        assert restRequest.method == method
    }

}
