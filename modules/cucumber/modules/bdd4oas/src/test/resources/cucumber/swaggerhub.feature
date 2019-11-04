Feature: SwaggerHub REST API

  Background:
    Given the OAS definition at src/test/resources/swaggerhub.yaml

  Scenario: Default API Listing
    When a search for apis is made
    Then more than 10 items are returned

  Scenario: Owner API Listing
    When a search for swaggerhub apis is made
    Then a search result is returned

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
    And the path "$..description" exists
    And the path "$..description[0]" equals "The registry API for SwaggerHub"
    And the path "$..description[0]" matches "The registry API for (.*)"
    Examples:
    | owner       | api          | version  | description                       |
    | swagger-hub | registry-api | 1.0.10   | The registry API for SwaggerHub   |
