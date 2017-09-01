# Swagger Assert4j Core Concepts

## JSON Recipes

JSON recipes are the internal representation used by swagger-assert4j to describe API tests. A very simple example:

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
properties corresponding to their type. For a quick reference of all available steps and their properties have a look at (REFERENCE.md).

## Recipe Libraries

Creating elaborate recipes with many teststeps, assertions, transfers, etc.. can be tedious - which is
why assert4j includes java, groovy and cucumber libraries to make recipe creation and execution more accessible. 
The above recipe can be created in java with:

```java
TestRecipe recipe = TestRecipeBuilder.buildRecipe(
    GET("https://api.swaggerhub.com/apis")
        .withAssertions(
            statusCodes(200)
        ) 
);
```

## Execution modes

No matter how you create JSON recipes; manually, via code, auto-generate, etc - they can be executed either locally
using the Open-Source SoapUI execution engine - or remotely using the commercial ReadyAPI TestServer product.

### Local Execution

Local execution of a recipe simply passes the generated recipe to the SoapUI test execution 
engine - executing the above recipe locally would be achieved with these two lines

```java
RecipeExecutor executor = new SoapUIRecipeExecutor();
Execution result = executor.executeRecipe(recipe);
```

## Remote Execution

To get access to extended functionality like data-driven testing, centralized execution and reporting, etc., you 
can execute your tests with [ReadyAPI TestServer](http://readyapi.smartbear.com/testserver/start) instead of running 
them locally. 

TestServer is a standalone server that exposes a REST API for executing API tests. The above test can be run executed remotely with

```java
TestServerClient testServerClient = TestServerClient.fromUrl("...").withCredentials("...", "...");
RecipeExecutorexecutor = testServerClient.createRecipeExecutor();
Execution result = executor.executeRecipe(recipe);
```

As you can see, the result of executeRecipe is the same when executing remotely, so it can be handled in the same
way as for local execution.

TestServer also provides functionality for running existing ReadyAPI/SoapUI projects, logging, etc - read more in the
[testserver module](modules/testserver)

## Execution Facade

To allow for easy switching between local and remote execution of tests you can use the execution facade provided 
by the [facade](modules/facade) module. Running the above tests with the facade looks as follows:

```java
Execution result = RecipeExecutionFacade.executeRecipe( recipe );
```

The facade will by default use the local execution engine but switch to remote execution if the following 
system properties are set:

```
testserver.endpoint=<testserver endpoin>
testserver.user=<testservrer user>
testserver.password=<testserver password>
```

## Logging of Recipes and HTTP transactions

Usage of the facade as in the above examples also enables logging of both generated recipes and HTTP transaction logs 
of executed tests (in HAR file format). Adding the following two properties:

```
swagger-assert4j.log.executions.folder=target/logs/executions
swagger-assert4j.log.recipes.folder=target/logs/recipes
```

will automatically result in the corresponding artifacts being written to the corresponding folders.

## Result handling

In all instances above we got an Execution object when executing the recipe. 

