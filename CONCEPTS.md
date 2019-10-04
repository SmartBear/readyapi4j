# ReadyAPI4j Core Concepts

## JSON Recipes

JSON recipes are the external representation used by readyapi4j to describe API tests which you either
create manually or using one of our code libraries (see below). A very simple example:

````json
{
  "testSteps": [
    {
      "type": "REST Request",
      "method": "GET",
      "URI": "https://api.swaggerhub.com/apis",
      "assertions": [
        {
          "type": "Valid HTTP Status Codes",
          "validStatusCodes": [
            200
          ]
        }
      ]
    }
  ]
}
````

A recipe consists of an array of testSteps that can execute requests, transfer values, define properties, etc - each containing 
properties corresponding to their type, have a look at the [core module](modules/core/README.md) for an overview of available
TestSteps, Assertions, etc. 

## Creating recipes with code

Creating elaborate recipes in JSON with many teststeps, assertions, transfers, etc can be tedious - which is
why readyapi4j includes [java](modules/core), [groovy](modules/groovy-dsl) and [cucumber](modules/cucumber) libraries to make 
recipe creation and execution more accessible. 

For example, the above recipe can be created in java with:

```java
TestRecipe recipe = TestRecipeBuilder.buildRecipe(
    GET("https://api.swaggerhub.com/apis")
        .withAssertions(
            statusCodes(200)
        ) 
);
```

## Execution modes

No matter how you create JSON recipes - manually, via code, auto-generate, etc - they can be executed either locally
using the Open-Source SoapUI execution engine - or remotely using the commercial ReadyAPI TestEngine product. Recipes
are executed by RecipeExecutors - which can be created for either local or remote execution.

### Local Execution

Local execution of a recipe simply passes the generated recipe to the SoapUI test execution 
engine - executing the above recipe locally would be achieved with these two lines

```java
RecipeExecutor executor = new SoapUIRecipeExecutor();
Execution execution = executor.executeRecipe(recipe);
```

Read more about local execution in the [local module](modules/local).

## Remote Execution

To get access to extended functionality like data-driven testing, centralized execution and reporting, etc, you 
can execute your tests with [ReadyAPI TestEngine](http://readyapi.smartbear.com/testengine/start) instead of running 
them locally. 

TestEngine is a standalone server that exposes a REST API for executing API tests. The above test can be run executed remotely with

```java
TestEngineClient testEngineClient = TestEngineClient.fromUrl("...").withCredentials("...", "...");
RecipeExecutor executor = testEngineClient.createRecipeExecutor();
Execution execution = executor.executeRecipe(recipe);
```

The result of executeRecipe is the same when executing remotely, so it can be handled in the same
way as for local execution.

TestEngine also provides functionality for running existing ReadyAPI/SoapUI projects, logging, etc - read more in the
[testengine module](modules/testengine)

## Execution Facade

To allow for easy switching between local and remote execution of tests you can use the execution facade provided 
by the [facade](modules/facade) module. Running the above tests with the facade looks as follows:

```java
Execution execution = RecipeExecutionFacade.executeRecipe( recipe );
```

The facade will by default use the local execution engine but switch to remote execution if the following 
system properties are set:

```
testengine.endpoint=<testengine endpoint>
testengine.user=<testengine user>
testengine.password=<testengine password>
```

The facade also provides a couple of utility methods for easily executing recipes in string or file format:

```java
Execution execution = RecipeExecutionFacade.executeRecipe(new File( "..path to recipe json file"));
```
or

```java
Execution execution = RecipeExecutionFacade.executeRecipe( "..json recipe in a string.." );
```

## Logging of Recipes and HTTP transactions

Usage of the facade as in the above examples also enables logging of both generated recipes and HTTP transaction logs 
of executed tests (in HAR file format). Adding the following two properties:

```
readyapi4j.log.executions.folder=target/logs/executions
readyapi4j.log.recipes.folder=target/logs/recipes
```

will automatically result in the corresponding artifacts being written to the corresponding folders.

## Synchronous vs Asynchronous execution

The above examples all used `executeRecipe(TestRecipe)` for executing tests - this method will execute the
specified recipe and block until execution has finished. If you would rather execute tests asynchronously
you can use `submitRecipe(TestRecipe)` instead; the returned Execution object will return 
`ProjectResultReport.StatusEnum.RUNNING` until the test finishes (either passed or failed). 

## Result handling

In all instances above we got an Execution object when executing the recipe, use 
`execution.getExecutionResult()` to get a  
[RecipeExecutionResult](https://smartbear.github.io/readyapi4j/apidocs/index.html?com/smartbear/readyapi4j/result/RecipeExecutionResult.html) 
object that provides details on execution time, individual teststep results, etc. 

In a unit testing scenario the provided AssertionUtils class can be used to assert the outcome of an Execution:

```java
.. create executor ..
Execution execution = executor.executeRecipe(recipe);
AssertionUtils.assertExecutionResult( execution.getExecutionResult() );
```

See the [Results](modules/core/README.md#execution-results) documentation for more details!
