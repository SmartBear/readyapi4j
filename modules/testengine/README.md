# Swagger Assert4j TestEngine executor

The testengine module provides a test execution engine that utilizes a remote TestEngine instance for executing
recipes.

See [Concepts](../../CONCEPTS.md#remote_execution) for general info and the [Core Module](../core) for an overview of
the Java API.

# TestEngine Extensions

ReadyAPI TestEngine provides a number of additional features that are not available when using swagger-assert4j with the
[local execution engine](../local), namely;

* The possibility to execute tests in existing ReadyAPI/SoapUI projects
* The possibility to validate APIs based on their Swagger definition
* The possibility to perform data-driven testing in Test Recipes 

The corresponding REST API exposed by the TestEngine for this functionality is 
[available on SwaggerHub](https://app.swaggerhub.com/apis/smartbear/ready-api-testengine)
This module provides a Java API for these extensions, as described below 

## Using the TestEngineClient

The [TestEngineClient](https://smartbear.github.io/swagger-assert4j/apidocs/index.html?io/swagger/assert4j/testengine/execution/TestEngineClient.html)
class is the main entry point to the additional features provided by TestEngine:

```java
TestEngineClient testEngineClient = TestEngineClient.fromUrl(TESTSERVER_URL)
                .withCredentials(TESTSERVER_USER, TESTSERVER_PASSWORD);
```

Executing TestRecipes on the server is done via the 
[TestEngineRecipeExecutor](https://smartbear.github.io/swagger-assert4j/apidocs/index.html?io/swagger/assert4j/testengine/execution/TestEngineRecipeExecutor.html) 
created with `TestEngineClient.createRecipeExecutor`, which is the same `Executor` created when using the [facade](../facade) for remote execution.

## Running existing projects

Use the [ProjectExecutor](https://smartbear.github.io/swagger-assert4j/apidocs/index.html?io/swagger/assert4j/testengine/execution/ProjectExecutor.html)
to execute existing SoapUI / ReadyAPI projects. 

```java
TestEngineClient testEngineClient = TestEngineClient.fromUrl(TESTSERVER_URL)
                .withCredentials(TESTSERVER_USER, TESTSERVER_PASSWORD);

ProjectExecutor projectExecutor = testEngineClient.createProjectExecutor();
```

Projects to be executed can reside either locally or remotely on the server (using the TestEngine Repository functionality), create and 
pass corresponding instances of [ProjectExecutionRequest](https://smartbear.github.io/swagger-assert4j/apidocs/index.html?io/swagger/assert4j/testengine/execution/ProjectExecutionRequest.html) 
and [RepositoryProjectExecutionRequest](https://smartbear.github.io/swagger-assert4j/apidocs/index.html?io/swagger/assert4j/testengine/execution/RepositoryProjectExecutionRequest.html) to the execute/submitProject methods.

Before executing tests you can further narrow down which TestSuites/TestCases to run, specify tags, properties, endpoints, etc. by 
setting the corresponding properties in either of these execution request classes.

Continuing from the above example:

```java
ProjectExecutionRequest request = ProjectExecutionRequest.Builder.forProjectFile(file).build();

// run all tests in entire project asynchronously
projectExecutor.submitProject( request );

// run only one test synchronously
request.setTestCaseName( "My TestCase")
projectExecutor.executeProject( request );

``` 

## Validating Swagger-defined APIs

ReadyAPI TestEngine supports "instant validation" of an API based on its Swagger definition; the server
will generate an ad-hoc TestSuite based on this Swagger definition, execute it, and return the result. 

This functionality is exposed in Java via the 
[SwaggerApiValidator](https://smartbear.github.io/swagger-assert4j/apidocs/index.html?io/swagger/assert4j/testengine/execution/SwaggerApiValidator.html) class, use as follows:

```java
TestEngineClient testEngineClient = TestEngineClient.fromUrl(TESTSERVER_URL)
                .withCredentials(TESTSERVER_USER, TESTSERVER_PASSWORD);

SwaggerApiValidator validator = testEngineClient.createApiValidator();

// validate the petstore
Execution execution = validator.validateApiSynchronously( "http://petstore.swagger.io/v2/swagger.json", null, null );

// do something with the result
RecipeExecutionResult result = execution.getExecutionResult();
...

```

## TestEngine data-driven testing

The ReadyAPI TestEngine adds a number of constructs to the JSON Recipe format to allow for data-driven testing, similar to 
what is possible with [Data Driven testing in SoapUI Pro](https://smartbear.com/product/ready-api/soapui/features/data-driven-tests/).

A [ServerTestSteps](https://smartbear.github.io/swagger-assert4j/apidocs/index.html?io/swagger/assert4j/testengine/teststeps/ServerTestSteps.html) 
class makes it easy to build the corresponding TestStep, matching the 
[TestSteps](https://smartbear.github.io/swagger-assert4j/apidocs/index.html?io/swagger/assert4j/teststeps/TestSteps.html) class in core.

A quick example of what's possible with dataGen DataSources, taken from the samples module:

```java
TestRecipe recipe = TestRecipeBuilder.buildRecipe(
        dataGenDataSource()
                .withNumberOfRows(10)
                .withProperties(
                    cityTypeProperty("cityProperty").duplicatedBy(2),
                    mac48ComputerAddressTypeProperty("computerAddressProperty"),
                    randomIntegerTypeProperty("integerProperty")
                )
                .named("DataSourceStep")
                .withTestSteps(
                    GET("http://www.google.se/")
                        .withParameters(
                            query("a", "${DataSourceStep#cityProperty}"),
                            query("b", "${DataSourceStep#computerAddressProperty}"),
                            query("c", "${DataSourceStep#integerProperty}")
                        )
                )
        );
```

Here we create a recipe that runs a Google search 10 times, each with different values retrieved
from the DataSource using PropertyExpansion syntax in the query parameters. 