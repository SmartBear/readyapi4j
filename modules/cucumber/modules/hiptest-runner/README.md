## A command-line utility for ReadyAPI4j Cucumber API Tests and HipTest

This module provides a command-line utility for integrating [HipTest](https://www.hiptest.com) with ReadyAPI4j.

It currently provides commands for
* [Downloading and running feature files](#downloading-and-running-feature-files) from HipTest 
* Creating/updating HipTest ActionWords from BDD4OAS x-bdd extensions in an OAS definition

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

Run the provided jar file with the `download command and the following properties:
* `-p` (required) - the HipTest project-id
* `-f` (required) - the HipTest folder-id
* `-t` (optional) - where to save the downloaded feature
* `-args` [*] (optional) - any arguments that should be passed to the Cucumber CLI

So for example to simply download and run a feature

```shell script
java -jar target/readyapi4j-cucumber-hiptest-runner-1.0.0-SNAPSHOT.jar download -p 141233 -f 1040072 -args -p pretty
[main] INFO com.smartbear.readyapi4j.cucumber.hiptest.DownloadAndRunScenario - Reading Features from HipTest
[main] INFO com.smartbear.readyapi4j.cucumber.hiptest.DownloadAndRunScenario - Cucumber feature(s) saved to file /var/folders/jn/081qkt_j5rg8wpc4v_njq4c00000gn/T/hiptest6365702829708930675.feature

1 Scenarios (1 passed)
5 Steps (5 passed)
0m3.650s
```

For saving the scenario to a local folder of your choice and little more output, 
add the -t and -args options, the latter with the cucumber "-p pretty" arguments which are passed on to the 
Cucumber CLI together with the feature file:

```shell script
java -jar target/readyapi4j-cucumber-hiptest-runner-1.0.0-SNAPSHOT.jar download -p 141233 -f 1040072 -t target/scenarios -args -p pretty
[main] INFO com.smartbear.readyapi4j.cucumber.hiptest.DownloadAndRunScenario - Reading Features from HipTest
[main] INFO com.smartbear.readyapi4j.cucumber.hiptest.DownloadAndRunScenario - Cucumber feature(s) saved to file target/scenarios/hiptest-141233-1040072.feature
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

Since the runner uses the OASBackend internally - all related functionality (x-bdd extensions, default stepdefs)
is available for your scenarios.

## Importing ActionWords

When creating a new project in HipTest this command can import ActionWords both for the default
REST vocabulary and from x-bdd extensions in a specified OAS definition - helping you write correct scenarios 
in HipTest from the start.




