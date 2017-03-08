Feature: Petstore API

  Scenario: Find pet by status
    Given the API running at http://petstore.swagger.io/v2
    And an endpoint of http://petstore.swagger.io/v2/pet/findByStatus
    When a GET request is made
    And the status parameter is test
    And the Accepts header is application/json
    Then a 200 response is returned within 2000ms

  Scenario: Find pet by tags
    Given the API running at http://petstore.swagger.io/v2
    When a GET request to /pet/findByTags is made
    And the tags parameter is test
    And the request expects json
    Then a 200 response is returned within 2000ms

  Scenario: Create pet with parameters
    Given the API running at http://petstore.swagger.io/v2
    When a POST request to /pet is made
    And name is doggies
    And status is available
    Then a 200 response is returned within 2000ms

  Scenario: Create pet with body
    Given the API running at http://petstore.swagger.io/v2
    When a POST request to /pet is made
    And the request body is

      """
{
  "name": "doggie",
  "status": "available"
}
      """

    Then a 200 response is returned within 2000ms
    And the response body contains
    """
  "id":
    """

