Feature: SwaggerHub REST API

  Background:
    Given the OAS definition at https://api.swaggerhub.com/apis/olensmar/registry-api-bdd/bdd4oas-demo

  Scenario: Default Specs Listing
    When a request to searchApisAndDomains is made
    Then a 200 response is returned
     And the path $.apis.length() equals 10

  Scenario: SwaggerHub API listing
    When a request to searchApisAndDomains is made
     And type is API
     And owner is SwaggerHub
    Then a 200 response is returned
