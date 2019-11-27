## A command-line utility for ReadyAPI4j Cucumber API Tests and HipTest

This module provides a command-line utility for integrating [HipTest](https://www.hiptest.com) with ReadyAPI4j.

It currently provides commands for
* [Downloading and running feature files](#downloading-and-running-feature-files) from HipTest 
* [Creating/updating HipTest ActionWords](#importing-actionwords) for default StepDefs and from BDD4OAS x-bdd extensions in an OAS definition
* [Clearing existing ActionWords]() from a HipTest project

## hiptest.properties

You will need to provide a hiptest.properties file with at least the following properties

```properties
hiptest.token=<your hiptest token>
hiptest.uid=<your hiptest user id>
hiptest.clientid=<your hiptest clientid>
hiptest.endpoint=<optional endpoint to HipTest API - defaults to https://app.hiptest.com/api/>
hiptest.accept=<optional HipTest Accept header - defaults to "application/vnd.api+json; version=1">
``` 

The tokens and clientid are generated from your [HipTest profile page](https://app.hiptest.com/profile). 

## Downloading and Running Feature files

Run the provided jar file with the `download [options]` command:
* `-p` (required, string) - the HipTest project-id
* `-f` (required, string) - the HipTest folder-id
* `-t` (optional, folder-path) - where to save the downloaded feature
* `-args` [*] (optional) - any arguments that should be passed to the Cucumber CLI

So for example to simply download and run a feature

```shell script
java -jar target/readyapi4j-cucumber-hiptest-runner-1.0.0-SNAPSHOT.jar download -p 141233 -f 1040072 -args -p pretty
Reading Features from HipTest
Cucumber feature(s) saved to file /var/folders/jn/081qkt_j5rg8wpc4v_njq4c00000gn/T/hiptest6365702829708930675.feature

1 Scenarios (1 passed)
5 Steps (5 passed)
0m3.650s
```

For saving the scenario to a local folder of your choice and little more output, 
add the -t and -args options, the latter with the cucumber "-p pretty" arguments which are passed on to the 
Cucumber CLI together with the feature file:

```shell script
java -jar target/readyapi4j-cucumber-hiptest-runner-1.0.0-SNAPSHOT.jar download -p 141233 -f 1040072 -t target/scenarios -args -p pretty
Reading Features from HipTest
Cucumber feature(s) saved to file target/scenarios/hiptest-141233-1040072.feature
Feature: Petstore API

  Scenario: Find pet by status                                               # target/scenarios/hiptest-141233-1040072.feature:4
    Given the OAS definition at "http://petstore.swagger.io/v2/swagger.json" # OASStepDefs.theOASDefinitionAt(String)
    When a request to "findPetsByStatus" is made                             # OASStepDefs.aRequestToOperationIsMade(String)
    And status is "test"                                                     # OASStepDefs.parameterIs(String,String)
    And the request expects "json"                                           # RestStepDefs.theRequestExpects(String)
    Then a "200" response is returned within "2000"ms                        # RestStepDefs.aResponseIsReturnedWithin(String,String)

1 Scenarios (1 passed)
5 Steps (5 passed)
0m3.650s
```

You can get the project-id and folder-id from the corresponding URL in your browser; 

```
https://app.hiptest.com/projects/141233/test-plan/folders/1040072
```

corresponds to project-id 141233 and folder 1040072 (used in the examples above)

The underlying API call made to the [HipTest API](https://api-doc.hiptest.com/) is described at 
https://api-doc.hiptest.com/?http#get-feature-from-a-given-folder.

Since the runner uses the [OASBackend](../bdd4oas) internally - all related functionality (x-bdd extensions, default stepdefs)
is available for your scenarios.

## Importing ActionWords

This command will create HipTest ActionWords both for the [default ReadyAPI4j REST vocabulary](../../README.md#api-stepdefs-reference) 
and from [x-bdd extensions](../bdd4oas/README.md) in a specified OAS definition - helping you write correct scenarios in HipTest from the start. 
The command will only add ActionWords that do not already exist - so you can safely run it several times to add new ActionWords as you go along.

Run the provided jar file with the `import [options] <oas-url>` command and the following options:
* `-p` (required, string) - the HipTest project-id
* `-d` (optional, boolean) - create ActionWords for default ReadyAPI4j StepDefs 
* `-l` (optional, boolean) - list ActionWords to console instead of importing (for debugging purposes), ignores the -p options
* `oas-url` (optional) - url/path to OAS 2.0/3.X definition that contains x-bdd-when/then extension to import as ActionWords 

For example - listing the ActionWords that would be imported from an OAS definition:

```shell script
java -jar target/readyapi4j-cucumber-hiptest-runner-1.0.0-SNAPSHOT.jar import -l https://api.swaggerhub.com/apis/olensmar/registry-api-bdd/bdd4oas-demo
Listing ActionWords only - skipping import
Found ActionWord 'at least 10 items are returned' for ApiResponse: a search result
Found ActionWord 'a search result is returned' for ApiResponse: a search result
Found ActionWord 'performing a default search' for Operation: searchApisAndDomains
Found ActionWord 'searching for "owner" apis' for Operation: searchApisAndDomains
```

Just listing default ActionWords for the built-in StepDefs described at [default StepDefs](../../README.md#api-stepdefs-reference):

```shell script
java -jar target/readyapi4j-cucumber-hiptest-runner-1.0.0-SNAPSHOT.jar import -l -d                                                                    
Listing ActionWords only - skipping import
Found ActionWord 'the OAS definition at "oas-url"' for com.smartbear.readyapi4j.cucumber.OASStepDefs$theOASDefinitionAt
Found ActionWord 'a request to "operation-id" with parameters' for com.smartbear.readyapi4j.cucumber.OASStepDefs$aRequestToOperationWithParametersIsMade
Found ActionWord 'a request to "operation-id" with content' for com.smartbear.readyapi4j.cucumber.OASStepDefs$aRequestToOperationWithContentIsMade
Found ActionWord 'a request to "operation-id" is made' for com.smartbear.readyapi4j.cucumber.OASStepDefs$aRequestToOperationIsMade
Found ActionWord 'the response is "response-description"' for com.smartbear.readyapi4j.cucumber.OASStepDefs$theResponseIs
Found ActionWord '"parameter-name" is "parameter-value"' for com.smartbear.readyapi4j.cucumber.OASStepDefs$parameterIs
Found ActionWord '"parameter-name" is' for com.smartbear.readyapi4j.cucumber.OASStepDefs$parameterIsBlob
Found ActionWord 'the oAuth2 token "oauth-token"' for com.smartbear.readyapi4j.cucumber.RestStepDefs$theOauth2Token
Found ActionWord 'the API running at "api-endpoint"' for com.smartbear.readyapi4j.cucumber.RestStepDefs$theAPIRunningAt
Found ActionWord 'a "method" request to "path" is made' for com.smartbear.readyapi4j.cucumber.RestStepDefs$aRequestToPathIsMade
Found ActionWord 'a "method" request is made' for com.smartbear.readyapi4j.cucumber.RestStepDefs$aRequestIsMade
Found ActionWord 'the request body is' for com.smartbear.readyapi4j.cucumber.RestStepDefs$theRequestBodyIs
Found ActionWord 'a status code of "status-code" is returned' for com.smartbear.readyapi4j.cucumber.RestStepDefs$aStatusCodeIsReturned
Found ActionWord 'a "status-code" response is returned withing "milliseconds"ms' for com.smartbear.readyapi4j.cucumber.RestStepDefs$aResponseIsReturnedWithin
Found ActionWord 'the response body contains' for com.smartbear.readyapi4j.cucumber.RestStepDefs$theResponseBodyContains
Found ActionWord 'the response body matches' for com.smartbear.readyapi4j.cucumber.RestStepDefs$theResponseBodyMatches
Found ActionWord 'the "name" parameter is "value"' for com.smartbear.readyapi4j.cucumber.RestStepDefs$theParameterIs
Found ActionWord 'the "name" header is "value"' for com.smartbear.readyapi4j.cucumber.RestStepDefs$theHeaderIs
Found ActionWord 'the type is "request-content-type"' for com.smartbear.readyapi4j.cucumber.RestStepDefs$theTypeIs
Found ActionWord 'the request expects "expected-content-type"' for com.smartbear.readyapi4j.cucumber.RestStepDefs$theRequestExpects
Found ActionWord 'the response type is "expected-content-type"' for com.smartbear.readyapi4j.cucumber.RestStepDefs$theResponseTypeIs
Found ActionWord 'the response contains a "header-name" header' for com.smartbear.readyapi4j.cucumber.RestStepDefs$theResponseContainsHeader
Found ActionWord 'the path "json-path" equals "expected-value"' for com.smartbear.readyapi4j.cucumber.RestStepDefs$thePathEquals
Found ActionWord 'the path "json-path" equals' for com.smartbear.readyapi4j.cucumber.RestStepDefs$thePathEqualsContent
Found ActionWord 'the path "json-path" matches "regex-expression"' for com.smartbear.readyapi4j.cucumber.RestStepDefs$thePathMatches
Found ActionWord 'the path "json-path" matches' for com.smartbear.readyapi4j.cucumber.RestStepDefs$thePathMatchesContent
Found ActionWord 'the path "json-path" finds "number-of-items"' for com.smartbear.readyapi4j.cucumber.RestStepDefs$thePathFinds
Found ActionWord 'the path "json-path" exists' for com.smartbear.readyapi4j.cucumber.RestStepDefs$thePathExists
Found ActionWord 'the xpath "xpath" equals "expected-value"' for com.smartbear.readyapi4j.cucumber.RestStepDefs$theXPathMatches
Found ActionWord 'the xpath "xpath" equals' for com.smartbear.readyapi4j.cucumber.RestStepDefs$theXPathMatchesContent
Found ActionWord 'the response "response-header" header is "expected-value"' for com.smartbear.readyapi4j.cucumber.RestStepDefs$theResponseHeaderIs
Found ActionWord 'the response "response-header" header matches "regex-expression"' for com.smartbear.readyapi4j.cucumber.RestStepDefs$theResponseHeaderMatches
Found ActionWord 'the response body contains "value"' for com.smartbear.readyapi4j.cucumber.RestStepDefs$theResponseBodyContains2
Found ActionWord 'a "status-code" response is returned' for com.smartbear.readyapi4j.cucumber.RestStepDefs$aResponseIsReturned
```

For the actual import, create an empty project in HipTest and provide it's project-id without the -l option to import the ActionWords:

```shell script
java -jar target/readyapi4j-cucumber-hiptest-runner-1.0.0-SNAPSHOT.jar import -p 149337 https://api.swaggerhub.com/apis/olensmar/registry-api-bdd/bdd4oas-demo
Found 0 existing ActionWords in HipTest project
Adding ActionWord 'at least 10 items are returned' from ApiResponse: a search result
Adding ActionWord 'a search result is returned' from ApiResponse: a search result
Adding ActionWord 'performing a default search' from Operation: searchApisAndDomains
Adding ActionWord 'searching for "owner" apis' from Operation: searchApisAndDomains
Creating OAS ActionWord
Adding ActionWord 'the OAS definition at "oas-url"' from OAS definition
```

As you can see the importer automatically adds a `the OAS definition at...` ActionWord in this case since it is required for
the other ActionWords to work, the default value for the `oas-url` parameter is set to the oas-url argument value. 

## Clearing ActionWords

This is provided for convenience since HipTest does not allow you to remove all ActionWords from a project with one command.
Use it carefully since any Scenarios in HipTest using any of the removed ActionWords will become invalid.

Run by providing the `clear` command together with the project-id (-p):

```shell script
java -jar target/readyapi4j-cucumber-hiptest-runner-1.0.0-SNAPSHOT.jar clear -p 149337                                                                       
Loading ActionWords
Clearing 5 ActionWords
.....
Finished!
```



