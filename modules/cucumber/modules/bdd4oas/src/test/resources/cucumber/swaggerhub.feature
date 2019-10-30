Feature: SwaggerHub REST API

  Background:
    Given the OAS definition at https://api.swaggerhub.com/apis/olensmar/registry-api-bdd/1.0.47

  Scenario: Default API Listing
#    When a request to searchApis is made
    When a search for apis is made
#    Then the response is 303 response
    Then a search result is returned

  Scenario: Owner API Listing
    When a request to getOwnerApis is made
    And owner is swagger-hub
    Then the response is a list of APIs in APIs.json format

  Scenario: API Version Listing
    When a request to getApiVersions with parameters
    """
    owner = swagger-hub
    api = registry-api
    """
    Then the response is a list of API versions in APIs.json format
    And the response body contains /apis/swagger-hub/registry-api

  Scenario Outline: API Retrieval
    When a request to getDefinition is made
    And owner is <owner>
    And api is <api>
    And version is <version>
    Then a 200 response is returned within 2000ms
    And the response type is application/json
    And the response body contains
    """
    "description":"<description>"
    """
    Examples:
    | owner       | api          | version  | description                       |
    | swagger-hub | registry-api | 1.0.10   | The registry API for SwaggerHub   |
