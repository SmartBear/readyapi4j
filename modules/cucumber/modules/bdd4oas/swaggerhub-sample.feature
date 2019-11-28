Feature: SwaggerHub REST API

  Background:
    Given the OAS definition at https://api.swaggerhub.com/apis/olensmar/registry-api-bdd/bdd4oas-demo

  Scenario: Default Specs Listing
    When performing a default search
    Then at least 10 items are returned

  Scenario: SwaggerHub API listing
    When searching for swaggerhub apis
    Then a search result is returned