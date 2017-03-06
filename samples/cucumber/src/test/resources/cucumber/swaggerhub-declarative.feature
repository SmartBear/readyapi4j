Feature: SwaggerHub REST API
  Scenario: Default API Listing
    When a request to the API listing is made
    Then a list of APIs should be returned within 5200ms

  Scenario: Owner API Listing
    Given an owner named swagger-hub
    When a request to the API listing is made
    Then a list of APIs should be returned within 500ms

  Scenario: API Version Listing
    Given an owner named swagger-hub
    And an api named registry-api
    When a request to the API listing is made
    Then a list of APIs should be returned within 500ms

  Scenario: API Retrieval
    Given an owner named swagger-hub
    And an api named registry-api
    And a version named 1.0.0
    When a request to the API listing is made
    Then an API definition should be returned within 500ms

  Scenario: TestServer API Retrieval
    Given an owner named smartbear
    And an api named ready-api-testserver
    And a version named 1.0.0
    When a request to the API listing is made
    Then an API definition should be returned within 500ms
