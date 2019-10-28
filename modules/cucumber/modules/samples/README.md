## Cucumber Samples for readyapi4j-cucumber

This project currently contains 3 example feature files in the 
[src/test/resources/cucumber](src/test/resources/cucumber) folder.

- [petstore-imperative.feature](src/test/resources/cucumber/petstore-imperative.feature) - "bare-bones" feature for defining 
the peststore API
- [petstore-swagger.feature](src/test/resources/cucumber/petstore-imperative.feature) - same feature for defining 
the peststore API using some of the Swagger-specific vocabulary
- [swaggerhub.feature](src/test/resources/cucumber/petstore-imperative.feature) - feature for the SwaggerHub
REST API showing usage of Swagger constructs and Examples tables.

Run them with 
```
mvn test
```

or if you have TestEngine running locally with its default account:
```
mvn test -Dtestengine.user=admin -Dtestengine.password=testengine -Dtestengine.endpoint=http://localhost:8080
```





which will use JUnit to run them via the CucumberTest runner, output should be something in the line of
```gherkin
[INFO] --- maven-surefire-plugin:2.19.1:test (default-test) @ readyapi4j-cucumber-samples ---

-------------------------------------------------------
 T E S T S
-------------------------------------------------------
Running CucumberTest
Feature: Petstore API

  Scenario: Find pet by status                             # petstore-imperative.feature:3
    Given the API running at http://petstore.swagger.io/v2 # GenericRestStepDefs.theAPIRunningAt(String)
    When a GET request to /pet/findByStatus is made        # GenericRestStepDefs.aRequestToPathIsMade(String,String)
    And the status parameter is test                       # GenericRestStepDefs.theParameterIs(String,String)
    And the Accepts header is application/json             # GenericRestStepDefs.theHeaderIs(String,String)
    Then a 200 response is returned within 50ms            # GenericRestStepDefs.aResponseIsReturnedWithin(int,int)

  Scenario: Find pet by tags                               # petstore-imperative.feature:10
    Given the API running at http://petstore.swagger.io/v2 # GenericRestStepDefs.theAPIRunningAt(String)
    When a GET request to /pet/findByTags is made          # GenericRestStepDefs.aRequestToPathIsMade(String,String)
    And the tags parameter is test                         # GenericRestStepDefs.theParameterIs(String,String)
    And the request expects json                           # GenericRestStepDefs.theRequestExpects(String)
    Then a 200 response is returned within 50ms            # GenericRestStepDefs.aResponseIsReturnedWithin(int,int)

  Scenario: Create pet with parameters                     # petstore-imperative.feature:17
    Given the API running at http://petstore.swagger.io/v2 # GenericRestStepDefs.theAPIRunningAt(String)
    When a POST request to /pet is made                    # GenericRestStepDefs.aRequestToPathIsMade(String,String)
    And name is doggies                                    # GenericRestStepDefs.parameterIs(String,String)
    And status is available                                # GenericRestStepDefs.parameterIs(String,String)
    Then a 200 response is returned within 50ms            # GenericRestStepDefs.aResponseIsReturnedWithin(int,int)

  Scenario: Create pet with body                           # petstore-imperative.feature:24
    Given the API running at http://petstore.swagger.io/v2 # GenericRestStepDefs.theAPIRunningAt(String)
    When a POST request to /pet is made                    # GenericRestStepDefs.aRequestToPathIsMade(String,String)
    And the request body is                                # GenericRestStepDefs.theRequestBodyIs(String)
      """
      {
      "name": "doggie",
      "status": "available"
      }
      """
    Then a 200 response is returned within 50ms            # GenericRestStepDefs.aResponseIsReturnedWithin(int,int)
    And the response body contains                         # GenericRestStepDefs.theResponseBodyContains(String)
      """
      "id":
      """
Feature: Petstore API
reading from http://petstore.swagger.io/v2/swagger.json

  Scenario: Find pet by status                                                 # petstore-swagger.feature:3
    Given the Swagger definition at http://petstore.swagger.io/v2/swagger.json # GenericRestStepDefs.theSwaggerDefinitionAt(String)
    When a request to findPetsByStatus is made                                 # GenericRestStepDefs.aRequestToOperationIsMade(String)
    And status is test                                                         # GenericRestStepDefs.parameterIs(String,String)
    And the request expects json                                               # GenericRestStepDefs.theRequestExpects(String)
    Then a 200 response is returned within 500ms                               # GenericRestStepDefs.aResponseIsReturnedWithin(int,int)

  Scenario: Find pet by tags                                                   # petstore-swagger.feature:10
    Given the Swagger definition at http://petstore.swagger.io/v2/swagger.json # GenericRestStepDefs.theSwaggerDefinitionAt(String)
    When a request to findPetsByTags is made                                   # GenericRestStepDefs.aRequestToOperationIsMade(String)
    And tags is test                                                           # GenericRestStepDefs.parameterIs(String,String)
    And the request expects json                                               # GenericRestStepDefs.theRequestExpects(String)
    Then a 200 response is returned within 500ms                               # GenericRestStepDefs.aResponseIsReturnedWithin(int,int)

  Scenario: Create pet with parameters                                         # petstore-swagger.feature:17
    Given the Swagger definition at http://petstore.swagger.io/v2/swagger.json # GenericRestStepDefs.theSwaggerDefinitionAt(String)
    When a request to addPet is made                                           # GenericRestStepDefs.aRequestToOperationIsMade(String)
    And name is doggies                                                        # GenericRestStepDefs.parameterIs(String,String)
    And status is available                                                    # GenericRestStepDefs.parameterIs(String,String)
    Then a 200 response is returned within 500ms                               # GenericRestStepDefs.aResponseIsReturnedWithin(int,int)

  Scenario: Get pet by ID                                                      # petstore-swagger.feature:24
    Given the Swagger definition at http://petstore.swagger.io/v2/swagger.json # GenericRestStepDefs.theSwaggerDefinitionAt(String)
    When a request to getPetById is made                                       # GenericRestStepDefs.aRequestToOperationIsMade(String)
    And id is 1234                                                             # GenericRestStepDefs.parameterIs(String,String)
    And the request expects json                                               # GenericRestStepDefs.theRequestExpects(String)
    Then a 404 response is returned within 500ms                               # GenericRestStepDefs.aResponseIsReturnedWithin(int,int)
    And the response type is json                                              # GenericRestStepDefs.theResponseTypeIs(String)
    And the response contains a Server header                                  # GenericRestStepDefs.theResponseContainsHeader(String)
Feature: SwaggerHub REST API
reading from https://api.swaggerhub.com/apis/swagger-hub/registry-api/1.0.10

  Background:                                                                                       # swaggerhub.feature:3
    Given the Swagger definition at https://api.swaggerhub.com/apis/swagger-hub/registry-api/1.0.10 # GenericRestStepDefs.theSwaggerDefinitionAt(String)

  Scenario: Default API Listing                             # swaggerhub.feature:6
    When a request to searchApis is made                    # GenericRestStepDefs.aRequestToOperationIsMade(String)
    Then the response is a list of APIs in APIs.json format # GenericRestStepDefs.theResponseIs(String)

  Background:                                                                                       # swaggerhub.feature:3
    Given the Swagger definition at https://api.swaggerhub.com/apis/swagger-hub/registry-api/1.0.10 # GenericRestStepDefs.theSwaggerDefinitionAt(String)

  Scenario: Owner API Listing                               # swaggerhub.feature:10
    When a request to getOwnerApis is made                  # GenericRestStepDefs.aRequestToOperationIsMade(String)
    And owner is swagger-hub                                # GenericRestStepDefs.parameterIs(String,String)
    Then the response is a list of APIs in APIs.json format # GenericRestStepDefs.theResponseIs(String)

  Background:                                                                                       # swaggerhub.feature:3
    Given the Swagger definition at https://api.swaggerhub.com/apis/swagger-hub/registry-api/1.0.10 # GenericRestStepDefs.theSwaggerDefinitionAt(String)

  Scenario: API Version Listing                                     # swaggerhub.feature:15
    When a request to getApiVersions is made                        # GenericRestStepDefs.aRequestToOperationIsMade(String)
    And owner is swagger-hub                                        # GenericRestStepDefs.parameterIs(String,String)
    And api is registry-api                                         # GenericRestStepDefs.parameterIs(String,String)
    Then the response is a list of API versions in APIs.json format # GenericRestStepDefs.theResponseIs(String)
    And the response body contains                                  # GenericRestStepDefs.theResponseBodyContains(String)
      """
      "url":"/apis/swagger-hub/registry-api"
      """

  Scenario Outline: API Retrieval                # swaggerhub.feature:25
    When a request to getDefinition is made
    And owner is <owner>
    And api is <api>
    And version is <version>
    Then a 200 response is returned within 500ms
    And the response type is json
    And the response body contains
      """
      "description":"<description>"
      """

    Examples:

  Background:                                                                                       # swaggerhub.feature:3
    Given the Swagger definition at https://api.swaggerhub.com/apis/swagger-hub/registry-api/1.0.10 # GenericRestStepDefs.theSwaggerDefinitionAt(String)

  Scenario Outline: API Retrieval                # swaggerhub.feature:38
    When a request to getDefinition is made      # GenericRestStepDefs.aRequestToOperationIsMade(String)
    And owner is swagger-hub                     # GenericRestStepDefs.parameterIs(String,String)
    And api is registry-api                      # GenericRestStepDefs.parameterIs(String,String)
    And version is 1.0.10                        # GenericRestStepDefs.parameterIs(String,String)
    Then a 200 response is returned within 500ms # GenericRestStepDefs.aResponseIsReturnedWithin(int,int)
    And the response type is json                # GenericRestStepDefs.theResponseTypeIs(String)
    And the response body contains               # GenericRestStepDefs.theResponseBodyContains(String)
      """
      "description":"The registry API for SwaggerHub"
      """

  Background:                                                                                       # swaggerhub.feature:3
    Given the Swagger definition at https://api.swaggerhub.com/apis/swagger-hub/registry-api/1.0.10 # GenericRestStepDefs.theSwaggerDefinitionAt(String)

  Scenario Outline: API Retrieval                # swaggerhub.feature:39
    When a request to getDefinition is made      # GenericRestStepDefs.aRequestToOperationIsMade(String)
    And owner is fehguy                          # GenericRestStepDefs.parameterIs(String,String)
    And api is sonos-api                         # GenericRestStepDefs.parameterIs(String,String)
    And version is 1.0.0                         # GenericRestStepDefs.parameterIs(String,String)
    Then a 200 response is returned within 500ms # GenericRestStepDefs.aResponseIsReturnedWithin(int,int)
    And the response type is json                # GenericRestStepDefs.theResponseTypeIs(String)
    And the response body contains               # GenericRestStepDefs.theResponseBodyContains(String)
      """
      "description":"A REST API for the Sonos platform"
      """

13 Scenarios (13 passed)
71 Steps (71 passed)
0m6.197s

Tests run: 84, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 6.776 sec - in CucumberTest

Results :

Tests run: 84, Failures: 0, Errors: 0, Skipped: 0
```