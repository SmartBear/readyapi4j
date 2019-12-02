## BDD OAS/Swagger Extensions

This module allows for inline definitions of when / then vocabularies inside an OAS definition using OAS extensions, 
which  can then be used to write and execute Cucumber scenarios without any further requirement for writing
any StepDef code. The vocabularies are added using custom OAS extensions and dynamically processed when
executing corresponding scenarios using the runner in this module.

Contents:
* [A simple x-bdd-when/x-bdd-then example](#a-simple-x-bdd-whenx-bdd-then-example)
* [Adding parameters and assertions](#adding-parameters-and-assertions)
* [Parameterizing x-bdd-when statements](#parameterizing-x-bdd-when-statements)
* [Using the standard OAS / REST Vocabulary](#using-the-standard-rest--oas-vocabularies)
* [x-bdd-when refernece](#x-bdd-when-reference)
* [x-bdd-then refernece](#x-bdd-then-reference)
* [Assertion Reference](#assertion-reference)
  * [JSON Assertion](#json-assertion)
  * [Content Assertion](#content-assertion)
  * [Header Assertion](#header-assertion)
  * [XML Assertion](#xml-assertion)
* [Using the OASBackend](#using-the-oasbackend)

### A simple x-bdd-when/x-bdd-then example

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
      - performing a default search
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

As you can see - a `x-bdd-when` extension has been added to the operation, and a corresponding 
`x-bdd-then` extension has been added to the default response. 

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

Using our extensions the runner can now dynamically map our gherkin vocabulary to the corresponding operation 
and expected result - making the REST API call to the host specified in the OAS definition. No coding required!

### Adding parameters and assertions

It's pretty common to want to assert an API response for specific values or content, which is
traditionally done in code/tooling using an assertion mechanism. Fortunately this is possible here
as well:

```yaml
paths:
  /specs:
    get:
      x-bdd-when:
      - performing a default search
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

Once again no coding required - just run this feature as we did above and BDD4OAS will dynamically make the 
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

### Parameterizing x-bdd-when statements

In our example we had hard-coded the owner in the x-bdd-when statement to "swaggerhub" - but what if we want the writer
of the scenario to define that value themselves? We can do this by adding arguments to our x-bdd-when statements:

```yaml
paths:
  /specs:
    get:
      x-bdd-when:
      - performing a default search
      - when: searching for {owner} apis
        parameters:
          specType: API
     ...
```

Using the curly-bracket syntax with an existing parameter name we can now specify any owner in our scenario - which will
be used as the value for the owner parameter in the underlying request.

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

  Scenario: SmartBear API listing
    When searching for smartbear apis
    Then a search result is returned
```

Surround values with spaces with quotes (escape quotes with a backslash) if needed.

### Using the standard REST / OAS vocabularies

The [default vocabularies](../../README.md#api-stepdefs-reference) for testing REST APIs provided by ReadyAPI4j are still at your disposal
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
As you can see this calls the existing step definitions defined in the OASStepDefs/RestStepDefs classes (as did the above examples).

## x-bdd-when reference

`x-bdd-when` statements can be one of the following:
* a single string value that can optionally contain parameterizations (see above)
* an array containing single string values or `when` objects containing the following properties
  * a `when` single string value optionally containing parameterizations
  * a `parameters` map containing `name:value` pairs where name is the name of an existing parameter for the containing 
    operation and value is the value it should be assigned 

See examples of both of these under [Adding Parameters and Assertions](#adding-parameters-and-assertions) above.

## x-bdd-then reference 

`x-bdd-then` statements can be one of the following:
* a single string value 
* an array containing single string values or `then` objects containing the following properties
  * a `then` single string value
  * an `assertions` array containing `assertion` objects as described below

See examples of both of these under [Adding Parameters and Assertions](#adding-parameters-and-assertions) above.

## Assertion reference

As described above it is possible to add assertions to `x-bdd-then` extensions, the below assertions are 
currently available. All assertions require a `type` property as described below.

### json assertion

The following properties are available for the json assertion
* `type: json` 
* `path`: a json-path expression to apply to the response body (mandatory)
* one of the following
  * `value`: an expected value returned by the json-path expression
  * `regex`: a regular expression that must match the value returned by the json-path expression
  * `count`: the expected number of items returned by the json-path expression
* if none of these three is specified the assertion checks that any value for the json-path expression exists

### content assertion

The following properties are available for the content assertion
* `type: contains`
* one of the following
  * `content`: an expected value contained in the response
  * `regex`: a regular expression that must match the entire response

### header assertion

The following properties are available for the header assertion
* `type: header` 
* `name`: the name of the header to assert
* optionally one of the following
  * `value`: the expected value of the header
  * `regex`: a regular expression that must match the header value
* if neither of these is specified the assertion checks that the header exists in the response, ignoring its value

### xml assertion

The following properties are available for the xml assertion
* `type: xpath` 
* `path`: an xpath expression to apply to the response body (required)
* `value`: the expected value to be returned by the xpath expression (required)

## Using the OASBackend

Internally this module provides a custom [cucumber-jvm](https://github.com/cucumber/cucumber-jvm) Backend that:
- Loads the [default REST StepDefs](../../README.md#api-stepdefs-reference) provided by ReadyAPI4j
- Dynamically maps the x-bdd-when/then statements in a given OAS definition to the default StepDefs
- Loads any other StepDefs provided to the cucumber runtime as normal
 
As shown above the OASBackend can be run either via its docker image available on dockerhub at 
https://hub.docker.com/repository/docker/smartbear/readyapi4j-bdd4oas or via the jar file built by module.

Running the docker image requires one to map a local volume containing feature file(s) into a volume of the container 
and then specifying that volume (or a file in it) as the feature-file argument to the cucumber runtime. Since the 
docker image simple runs the existing Cucumber CLI all corresponding command-line options apply.

The jar file can be used similarly (without the requirement to map a volume of course). It's main class is set to the 
Cucumber CLI (io.cucumber.core.cli.Main) - for example the last above command could be run with:

```shell script
java -jar target/readyapi4j-bdd4oas-1.0.0-SNAPSHOT.jar /Users/olensmar/features/swaggerhub-sample2.feature -p pretty
Feature: SwaggerHub REST API

  Background:                                                                                          # /Users/olensmar/features/swaggerhub-sample2.feature:3
    Given the OAS definition at https://api.swaggerhub.com/apis/olensmar/registry-api-bdd/bdd4oas-demo # OASStepDefs.theOASDefinitionAt(String)

  Scenario: Default Specs Listing                  # /Users/olensmar/features/swaggerhub-sample2.feature:6
    When a request to searchApisAndDomains is made # OASStepDefs.aRequestToOperationIsMade(String)
    Then a 200 response is returned                # RestStepDefs.aResponseIsReturned(String)
    And the path $.apis.length() equals 10         # RestStepDefs.thePathEquals(String,String)

  Background:                                                                                          # /Users/olensmar/features/swaggerhub-sample2.feature:3
    Given the OAS definition at https://api.swaggerhub.com/apis/olensmar/registry-api-bdd/bdd4oas-demo # OASStepDefs.theOASDefinitionAt(String)

  Scenario: SwaggerHub API listing                 # /Users/olensmar/features/swaggerhub-sample2.feature:11
    When a request to searchApisAndDomains is made # OASStepDefs.aRequestToOperationIsMade(String)
    And type is API                                # OASStepDefs.parameterIs(String,String)
    And owner is SwaggerHub                        # OASStepDefs.parameterIs(String,String)
    Then a 200 response is returned                # RestStepDefs.aResponseIsReturned(String)

2 Scenarios (2 passed)
9 Steps (9 passed)
0m4.121s
```
