package com.smartbear.readyapi4j.dsl

import com.smartbear.readyapi.client.model.Assertion
import com.smartbear.readyapi.client.model.GroovyScriptAssertion
import com.smartbear.readyapi.client.model.InvalidHttpStatusCodesAssertion
import com.smartbear.readyapi.client.model.JsonPathContentAssertion
import com.smartbear.readyapi.client.model.JsonPathCountAssertion
import com.smartbear.readyapi.client.model.JsonPathExistenceAssertion
import com.smartbear.readyapi.client.model.RestParameter
import com.smartbear.readyapi.client.model.RestTestRequestStep
import com.smartbear.readyapi.client.model.SimpleContainsAssertion
import com.smartbear.readyapi.client.model.ValidHttpStatusCodesAssertion
import com.smartbear.readyapi.client.model.XPathContainsAssertion
import com.smartbear.readyapi.client.model.XQueryContainsAssertion
import com.smartbear.readyapi4j.TestRecipe
import com.smartbear.readyapi4j.extractor.ExtractorData
import org.junit.Test

import static DataExtractor.extractFirstTestStep
import static TestDsl.recipe
import static com.smartbear.readyapi4j.extractor.Extractors.fromResponse

class RestRequestDslTest {

    public static final String URI = "/uri_to_get"

    @Test
    void buildsRecipeWithName() throws Exception {
        String recipeName = 'DemoRecipe'
        TestRecipe recipe = recipe {
            name recipeName
        }
        assert recipe.name == recipeName
    }

    @Test
    void buildsRecipeWithGET() throws Exception {
        TestRecipe recipe = recipe {
            get URI
        }

        verifyValuesAndMethod(recipe, 'GET')
    }

    @Test
    void buildsRecipeWithPOST() throws Exception {
        TestRecipe recipe = recipe {
            post URI
        }

        verifyValuesAndMethod(recipe, 'POST')
    }

    @Test
    void buildsRecipeWithPUT() throws Exception {
        TestRecipe recipe = recipe {
            put URI
        }

        verifyValuesAndMethod(recipe, 'PUT')
    }

    @Test
    void buildsRecipeWithDELETE() throws Exception {
        TestRecipe recipe = recipe {
            delete URI
        }

        verifyValuesAndMethod(recipe, 'DELETE')
    }

    @Test
    void parameterizesRestRequest() throws Exception {
        String stepName = 'theGET'
        TestRecipe recipe = recipe {
            get '/some_uri', {
                name stepName
                followRedirects true
                entitizeParameters true
                postQueryString true
                timeout 5000
            }
        }

        RestTestRequestStep restRequest = extractFirstTestStep(recipe) as RestTestRequestStep
        assert restRequest.name == stepName
        assert restRequest.followRedirects: 'Not respecting followRedirects'
        assert restRequest.entitizeParameters: 'Not respecting entitizeParameters'
        assert restRequest.postQueryString: 'Not respecting postQueryString'
        assert restRequest.timeout == '5000'
    }

    @Test
    void addsHeaders() throws Exception {
        TestRecipe recipe = recipe {
            get '/some_uri', {
                headers(['Cache-Control': 'nocache'])
                header 'Expires', '0'
                header 'Accepts', ['text/plain', 'text/xml']
            }
        }

        RestTestRequestStep restRequest = extractFirstTestStep(recipe) as RestTestRequestStep
        assert restRequest.headers['Cache-Control'] == ['nocache']
        assert restRequest.headers['Expires'] == ['0']
        assert restRequest.headers['Accepts'] == ['text/plain', 'text/xml']
    }

    @Test
    void createsParameters() throws Exception {
        TestRecipe recipe = recipe {
            get '/users/{pathparam}/profile', {
                parameters {
                    path 'pathparam', 'bosse'
                    query 'q', 'Bo+Ek'
                    matrix 'SESSION', 'abc'
                    headerParam 'X-My-Header', 'headervalue'
                }
            }
        }

        RestTestRequestStep restRequest = extractFirstTestStep(recipe) as RestTestRequestStep
        List<RestParameter> parameters = restRequest.parameters
        assert restRequest.parameters.size() == 4
        assert parameters[0].type == RestParameter.TypeEnum.PATH
        assert parameters[0].name == 'pathparam'
        assert parameters[0].value == 'bosse'
        assert parameters[1].type == RestParameter.TypeEnum.QUERY
        assert parameters[1].name == 'q'
        assert parameters[1].value == 'Bo+Ek'
        assert parameters[2].type == RestParameter.TypeEnum.MATRIX
        assert parameters[2].name == 'SESSION'
        assert parameters[2].value == 'abc'
        assert parameters[3].type == RestParameter.TypeEnum.HEADER
        assert parameters[3].name == 'X-My-Header'
        assert parameters[3].value == 'headervalue'
    }

    @Test
    void createsStatusAssertions() throws Exception {
        TestRecipe recipe = recipe {
            get '/some_uri', {
                asserting {
                    status 200
                    statusNotIn 401, 404
                }
            }
        }

        RestTestRequestStep restRequest = extractFirstTestStep(recipe) as RestTestRequestStep
        assert restRequest.assertions.size() == 2
        ValidHttpStatusCodesAssertion statusAssertion = restRequest.assertions[0] as ValidHttpStatusCodesAssertion
        assert statusAssertion.validStatusCodes == ['200']
        InvalidHttpStatusCodesAssertion invalidStatusesAssertion = restRequest.assertions[1] as InvalidHttpStatusCodesAssertion
        assert invalidStatusesAssertion.invalidStatusCodes == ['401', '404']
    }

    @Test
    void createsSimpleContainsAssertions() throws Exception {
        TestRecipe recipe = recipe {
            get '/some_uri', {
                asserting {
                    responseContains 'Arrival', useRegexp: true, ignoreCase: true
                    responseDoesNotContain 'E.T', useRegexp: true, ignoreCase: true
                }
            }
        }

        RestTestRequestStep restRequest = extractFirstTestStep(recipe) as RestTestRequestStep
        assert restRequest.assertions.size() == 2
        SimpleContainsAssertion containsAssertion = restRequest.assertions[0] as SimpleContainsAssertion
        assert containsAssertion.token == 'Arrival'
        assert containsAssertion.useRegexp: 'Not respecting useRegexp'
        assert containsAssertion.ignoreCase: 'Not respecting ignoreCase'
        assert containsAssertion.type == 'Contains'
        SimpleContainsAssertion notContainsAssertion = restRequest.assertions[1] as SimpleContainsAssertion
        assert notContainsAssertion.token == 'E.T'
        assert notContainsAssertion.useRegexp: 'Not respecting useRegexp'
        assert notContainsAssertion.ignoreCase: 'Not respecting ignoreCase'
        assert notContainsAssertion.type == 'Not Contains'
    }

    @Test
    void createsJsonPathAssertions() throws Exception {
        TestRecipe recipe = recipe {
            get '/some_uri', {
                asserting {
                    jsonPath '$.customer.address' contains 'Storgatan 1'
                    jsonPath '$.customer.order' occurs 3 times
                }
            }
        }

        RestTestRequestStep restRequest = extractFirstTestStep(recipe) as RestTestRequestStep
        assert restRequest.assertions.size() == 2
        JsonPathContentAssertion containsAssertion = restRequest.assertions[0] as JsonPathContentAssertion
        assert containsAssertion.jsonPath == '$.customer.address'
        assert containsAssertion.expectedContent == 'Storgatan 1'
        JsonPathCountAssertion countAssertion = restRequest.assertions[1] as JsonPathCountAssertion
        assert countAssertion.jsonPath == '$.customer.order'
        assert countAssertion.expectedCount == '3'
    }

    @Test
    void createsJsonPathExistenceAssertions() throws Exception {
        TestRecipe recipe = recipe {
            get '/some_uri', {
                asserting {
                    jsonExists '$.customer.address'
                    jsonNotExists '$.owner'
                    jsonExistence '$.customer.address', '#Project#shouldAddressBePresent'
                }
            }
        }
        RestTestRequestStep restTestRequestStep = extractFirstTestStep(recipe) as RestTestRequestStep
        List<Assertion> assertions = restTestRequestStep.getAssertions()
        verifyJsonPathExistsAssertion(assertions.get(0), '$.customer.address', 'true')
        verifyJsonPathExistsAssertion(assertions.get(1), '$.owner', 'false')
        verifyJsonPathExistsAssertion(assertions.get(2), '$.customer.address', '#Project#shouldAddressBePresent')
    }

    private static void verifyJsonPathExistsAssertion(Assertion assertion, String jsonPath, String shouldExist) {
        JsonPathExistenceAssertion existenceAssertion = assertion as JsonPathExistenceAssertion
        assert existenceAssertion.jsonPath == jsonPath
        assert existenceAssertion.expectedContent == shouldExist
    }

    @Test
    void createsXPathAssertions() throws Exception {
        TestRecipe recipe = recipe {
            get '/some_uri', {
                asserting {
                    xpath '/customer/address' contains 'Storgatan 1'
                    xpath '/customer/order' occurs 3 times
                }
            }
        }

        RestTestRequestStep restRequest = extractFirstTestStep(recipe) as RestTestRequestStep
        assert restRequest.assertions.size() == 2
        XPathContainsAssertion containsAssertion = restRequest.assertions[0] as XPathContainsAssertion
        assert containsAssertion.xpath == '/customer/address'
        assert containsAssertion.expectedContent == 'Storgatan 1'
        XPathContainsAssertion countAssertion = restRequest.assertions[1] as XPathContainsAssertion
        assert countAssertion.xpath == 'count(/customer/order)'
        assert countAssertion.expectedContent == '3'

    }

    @Test
    void createsXQueryMatchAssertion() throws Exception {
        TestRecipe recipe = recipe {
            get '/some_uri', {
                asserting {
                    xQuery '/customer/address' contains 'Storgatan 1'
                }
            }
        }
        XQueryContainsAssertion assertion = extractFirstAssertion(recipe) as XQueryContainsAssertion
        assert assertion.xquery == '/customer/address'
        assert assertion.expectedContent == 'Storgatan 1'
    }

    @Test
    void createsGroovyScriptAssertions() throws Exception {
        TestRecipe recipe = recipe {
            get '/some_uri', {
                asserting {
                    script "assert response.contentType == 'text/xml'"
                }
            }
        }

        RestTestRequestStep restRequest = extractFirstTestStep(recipe) as RestTestRequestStep
        assert restRequest.assertions.size() == 1
        GroovyScriptAssertion scriptAssertion = restRequest.assertions[0] as GroovyScriptAssertion
        assert scriptAssertion.script == "assert response.contentType == 'text/xml'"
    }

    @Test
    void createsContentTypeAssertions() throws Exception {
        TestRecipe recipe = recipe {
            get '/some_uri', {
                asserting {
                    contentType 'text/xml'
                }
            }
        }

        GroovyScriptAssertion assertion = extractFirstAssertion(recipe) as GroovyScriptAssertion
        assert assertion.script == 'assert messageExchange.responseHeaders["Content-Type"].contains( "text/xml")'
    }

    @Test
    void addsExtractor() {
        def values = []
        TestRecipe recipe = recipe {
            get '/some_uri', {
                name 'RestRequest'
                extractors fromResponse('$[0].Endpoint', { value -> values.add(value) })
            }
        }
        assert recipe.testCase.properties.size() == 2
        recipe.testCase.properties.each {
            key, value ->
                if (key.contains('$[0].Endpoint')) {
                    assert value == ''
                } else {
                    assert key == ExtractorData.EXTRACTOR_DATA_KEY
                }
        }
    }

    private static void verifyValuesAndMethod(TestRecipe recipe, String method) {
        RestTestRequestStep restRequest = extractFirstTestStep(recipe) as RestTestRequestStep
        assert restRequest.URI == URI
        assert restRequest.method == method
    }

    private static Assertion extractFirstAssertion(TestRecipe recipe) {
        RestTestRequestStep testStep = extractFirstTestStep(recipe) as RestTestRequestStep
        return testStep.assertions[0]
    }

}
