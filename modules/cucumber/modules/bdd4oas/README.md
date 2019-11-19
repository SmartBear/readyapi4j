## BDD4OAS ReadyAPI4j module

This module allows for inline definitions of when / then vocabularies inside an OAS definition, which 
can then be used to write and execute Cucumber scenarios without any further requirement for writing
any StepDef code. The vocabularies are added using custom OAS extensions and dynamically processed when
executing corresponding scenarios using the runner in this module.

### A simple example

A quick example - consider the following search operation from the SwaggerHub public REST API 
(greatly abbreviated for the sake of this example) OAS 2.0 definition - hosted on SwaggerHub at 
https://app.swaggerhub.com/apis/olensmar/registry-api-bdd/bdd4oas-demo

```yaml
paths:
  /specs:
    get:
      operationId: searchApisAndDomains
      parameters:
      - name: specType
        in: query
        type: string
        default: ANY
        enum:
        - API
        - DOMAIN
        - ANY
      - name: owner
        in: query
        type: string
      responses:
        200:
          schema:
            $ref: '#/definitions/ApisJson'
```

We might want to write a scenario to test this API as follows:

```gherkin
Feature: SwaggerHub REST API

  Scenario: Default API Listing
    When performing a default search
    Then a search result is returned
```

Traditionally this would require us to write corresponding StepDefs in our language of choice and 
execute them with cucumber. Instead, this module allows us to define the above when/then
vocabulary inside the OAS definition using x-bdd extensions:

```yaml
paths:
  /specs:
    get:
      x-bdd-when:
      - doing a default search
      operationId: searchApisAndDomains
      parameters:
      - name: specType
        in: query
        type: string
        default: ANY
        enum:
        - API
        - DOMAIN
        - ANY
      - name: owner
        in: query
        type: string
      responses:
        200:
          schema:
            $ref: '#/definitions/ApisJson'
           x-bdd-then:
           - a search result is returned
``` 

As you can see - a x-bdd-when extension has been added to the operation, and a corresponding 
x-bdd-then extension has been added to the default response. 

Now all we have to do is add a given statement pointing to the modified OAS definition:

```gherkin
Feature: SwaggerHub REST API
  
  Background:
    Given the OAS definition at https://api.swaggerhub.com/apis/olensmar/registry-api-bdd/bdd4oas-demo

  Scenario: Default API Listing
    When performing a default search
    Then a search result is returned
```

and we're ready to run our scenario using this projects docker image:

```shell script
docker run -v /Users/olensmar/features:/features smartbear/readyapi4j-bdd4oas /features/swaggerhub-sample.feature -p pretty
Feature: SwaggerHub REST API

  Background:                                                                                          # /features/swaggerhub-sample.feature:3
    Given the OAS definition at https://api.swaggerhub.com/apis/olensmar/registry-api-bdd/bdd4oas-demo # OASStepDefs.theOASDefinitionAt(String)

  Scenario: Default API Listing      # /features/swaggerhub-sample.feature:6
    When performing a default search # OASStepDefs.aRequestToOperationWithParametersIsMade(String,String)
    Then a search result is returned # OASStepDefs.theResponseIs(String)

1 Scenarios (1 passed)
3 Steps (3 passed)
0m5.706s
```

Using our extensions the running can now dynamically map our gherkin vocabulary to the corresponding operation 
and expected result - making the REST API call to the host specified in the OAS definition.

### Adding parameters and assertions to x-bdd-then vocabularies

It's pretty common to want to assert an API response for specific values or content, which is
traditionally done in code/tooling using an assertion mechanism. Fortunately this is possible here
as well:

```yaml
paths:
  /specs:
    get:
      x-bdd-when:
      - perform a default search
      - when: searching for swaggerhub apis
        parameters:
          specType: API
          owner: swaggerhub
     ...
      responses:
        200:
           ...
           x-bdd-then:
           - a search result is returned
          - then: at least 10 items are returned
            assertions:
            - type: json
              path: $.apis.length()
              value: 10
```

Here we've extended both the x-bdd-when and x-bdd-then definitions:
* added a x-bdd-when that sets the specType and owner operation parameters to API and swaggerhub respectively
* added a x-bdd-then definition asserts the response for specific content using 
a JSON-Path expression - which allows us for functional validation of the API being called (bdd4oas 
supports a number of assertions - see the entire list below).

Our updated feature is now:

```gherkin
Feature: SwaggerHub REST API
  
  Background:
    Given the OAS definition at https://api.swaggerhub.com/apis/olensmar/registry-api-bdd/bdd4oas-demo

  Scenario: Default Specs Listing
    When performing a default search
    Then at least 10 items are returned

  Scenario: SwaggerHub API listing
    When searching for swaggerhub apis
    Then a search result is returned
```

Once again no coding required - just run this feature as we did above and Bdd4OAS will dynamically make the 
corresponding calls and assert the responses.

```shell script
docker run -v /Users/olensmar/features:/features smartbear/readyapi4j-bdd4oas /features/swaggerhub-sample.feature -p pretty
Feature: SwaggerHub REST API

  Background:                                                                                          # /features/swaggerhub-sample.feature:3
    Given the OAS definition at https://api.swaggerhub.com/apis/olensmar/registry-api-bdd/bdd4oas-demo # OASStepDefs.theOASDefinitionAt(String)

  Scenario: Default Specs Listing       # /features/swaggerhub-sample.feature:6
    When performing a default search    # OASStepDefs.aRequestToOperationWithParametersIsMade(String,String)
    Then at least 10 items are returned # OASStepDefs.theResponseIs(String)

  Background:                                                                                          # /features/swaggerhub-sample.feature:3
    Given the OAS definition at https://api.swaggerhub.com/apis/olensmar/registry-api-bdd/bdd4oas-demo # OASStepDefs.theOASDefinitionAt(String)

  Scenario: SwaggerHub API listing     # /features/swaggerhub-sample.feature:10
    When searching for swaggerhub apis # OASStepDefs.aRequestToOperationWithParametersIsMade(String,String)
    Then a search result is returned   # OASStepDefs.theResponseIs(String)

2 Scenarios (2 passed)
6 Steps (6 passed)
0m7.974s
```

### Using the standard REST / OAS vocabularies

The default vocabularies for testing REST APIs provided by ReadyAPI4j are still at your disposal
when writing your scenarios - so you can intermix these with your custom definitions provided 
via x-bdd extensions. For example we could have written the above Feature using these stepdefs 
entirely without the need for any bdd extensions:

```gherkin
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
``` 

Running the above results in:

```shell script
docker run -v /Users/olensmar/features:/features smartbear/readyapi4j-bdd4oas /features/swaggerhub-sample2.feature -p pretty
Feature: SwaggerHub REST API

  Background:                                                                                          # /features/swaggerhub-sample2.feature:3
    Given the OAS definition at https://api.swaggerhub.com/apis/olensmar/registry-api-bdd/bdd4oas-demo # OASStepDefs.theOASDefinitionAt(String)

  Scenario: Default Specs Listing                  # /features/swaggerhub-sample2.feature:6
    When a request to searchApisAndDomains is made # OASStepDefs.aRequestToOperationIsMade(String)
    Then a 200 response is returned                # RestStepDefs.aResponseIsReturned(String)
    And the path $.apis.length() equals 10         # RestStepDefs.thePathEquals(String,String)

  Background:                                                                                          # /features/swaggerhub-sample2.feature:3
    Given the OAS definition at https://api.swaggerhub.com/apis/olensmar/registry-api-bdd/bdd4oas-demo # OASStepDefs.theOASDefinitionAt(String)

  Scenario: SwaggerHub API listing                 # /features/swaggerhub-sample2.feature:11
    When a request to searchApisAndDomains is made # OASStepDefs.aRequestToOperationIsMade(String)
    And type is API                                # OASStepDefs.parameterIs(String,String)
    And owner is SwaggerHub                        # OASStepDefs.parameterIs(String,String)
    Then a 200 response is returned                # RestStepDefs.aResponseIsReturned(String)

2 Scenarios (2 passed)
9 Steps (9 passed)
0m5.658s
```
As you can see this calls the existing step definitions defined in the OASStepDefs class.

## Assertion reference



### json assertions


### xml assertions


### content assertions