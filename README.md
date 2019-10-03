# Swagger Assert4J - a Java library for API testing

The Assert4J library lets you easily test REST and SOAP APIs using Java, Groovy or Cucumber. 
Under the hood the library uses the open-source test-execution engine of [SoapUI](http://www.soapui.org).

* Quickly get started:
  * [with Java](#getting-started-with-java) - together with any testing framework - read the documentation for the core Java APIs [here](modules/core)
  * [with our Groovy DSL](#getting-started-with-groovy) - read the documentation of the DSL [here](modules/groovy-dsl)
  * [with Cucumber](modules/cucumber) - with cucumber-jvm 
  * [with the Maven plugin](modules/maven-plugin)
* [Running Tests with TestEngine](#running-tests-with-testengine)
* [Core Concepts](CONCEPTS.md) - explains swagger-assert4j core concepts
* [Modules](MODULES.md) - an overview of the included maven modules

## Getting Started with Java

1. Add the following Maven dependency to your project:
 
	```xml
	<dependency>
		<groupId>io.swagger.assert</groupId>
		<artifactId>swagger-assert4j-facade</artifactId>
		<version>1.0.0-SNAPSHOT</version>
	</dependency>
	```

2. Create and execute a simple recipe with your favorite unit-testing framework:

	```java
    @Test
    public void simpleCountTest() throws Exception {
         RecipeExecutionResult result = executeRecipe(
             GET("https://api.swaggerhub.com/specs")
                 .withParameters(
                     query("specType", "API"),
                     query("query", "testengine")
                 )
                 .withAssertions(
                     json("$.totalCount", "4")
                 )
         );
         
         assertExecutionResult(result);
    }
	```

3. Run your test and enjoy the results (or not...);

    ```
    java.lang.AssertionError: Execution failed: [[JsonPath Match] Comparison failed for path [$.totalCount], expecting [4], actual was [5]] 
    Expected :FINISHED
    Actual   :FAILED
    ```

Learn more about the Java testing vocabulary by:
- having a look at the [core module](modules/core)
- having a look at the [java samples](modules/samples/java/src/test/java/io/swagger/assert4j/samples/java)
- having a look at the [core unit tests](modules/core/src/test/java/io/swagger/assert4j)
- [browsing the javadoc](http://smartbear.github.io/swagger-assert4j/apidocs/) 

## Getting Started with Groovy 

Assert4J provides a Groovy DSL to create and execute API tests locally or on TestEngine. Documentation of the DSL
is [available here](modules/groovy-dsl).

The following steps explain how to use this DSL in a JUnit test.

1. Add the following Maven dependency to your project:
 
	```xml
	<dependency>
		<groupId>io.swagger.assert</groupId>
		<artifactId>swagger-assert4j-groovy-dsl</artifactId>
		<version>1.0.0-SNAPSHOT</version>
	</dependency>
	```

2. Create a JUnit test with a test recipe in Groovy:

  The example below shows how to create and execute a recipe with one single step locally, using the SoapUI OS engine. 
  This requires the additional dependency on io.swagger.assert:assert4j-local, but there is no need to install SoapUI. 
   ```groovy
   import io.swagger.assert4j.execution.Execution
   import org.junit.Test
   
   import static io.swagger.assert4j.dsl.execution.RecipeExecution.executeRecipe

    class DslTestDemo {
    
        @Test
        void testSwaggerHubApi() {
           //Executes recipe locally - this requires the additional dependency io.swagger.assert:assert4j-local
            Execution execution = executeRecipe {
                get 'https://api.swaggerhub.com/specs', {
                    parameters {
                        query 'specType', 'API'
                        query 'query', 'testengine'
                    }
                    asserting {
                        jsonPath '$.totalCount' occurs 0 times
                    }
                }
            }
            assert execution.errorMessages.empty
        }
    }   
   ```
   Here is sample output from this test. It shows that the assertion on the test step has failed:
   ```
   Assertion failed: 
   
   assert execution.errorMessages.empty
          |         |             |
          |         |             false
          |         [[JsonPath Count] Comparison failed for path [$.totalCount], expecting [0], actual was [1]]
          io.swagger.assert4j.local.execution.SoapUIRecipeExecution@f810c18
   ```
   
   Similarly, you can execute the recipe on TestEngine with the following:
   ```groovy
   import io.swagger.assert4j.execution.Execution
   import org.junit.Test
   
   import static io.swagger.assert4j.dsl.execution.RecipeExecution.executeRecipeOnServer
   
   class DslTestDemo {
       @Test
       void testSwaggerHubApi() {
           Execution execution = executeRecipeOnServer '<your TestEngine url, e.g. http://localhost:8080>', '<your user>', '<your password>', {
               get 'https://api.swaggerhub.com/specs', {
                   parameters {
                       query 'specType', 'API'
                       query 'query', 'testengine'
                   }
                   asserting {
                       jsonPath '$.totalCount' occurs 0 times
                   }
               }
           }
           assert execution.errorMessages.empty
       }
   }
   ```
Here is sample output from this test:
```
Assertion failed: 

assert execution.errorMessages.empty
       |         |             |
       |         |             false
       |         [TestStepName: GET request 1, messages: [JsonPath Count] Comparison failed. Path: [$.totalCount]; Expected value: [0]; Actual value: [1].]
       io.swagger.assert4j.testengine.execution.TestEngineExecution@dfddc9a
```
## More samples / tutorials

Tutorial in the ReadyAPI TestEngine documentation: 
[Creating Code-Based Recipes: Tutorial](http://readyapi.smartbear.com/testengine/tutorials/code_based/start)

The [samples submodule](modules/samples) here on GitHub contains a number of samples for Java, Groovy and Maven.

## Running tests with TestEngine

To get access to extended functionality like data-driven testing, centralized execution and reporting, etc., you 
need to execute your tests with [ReadyAPI TestEngine](http://readyapi.smartbear.com/testengine/start) instead of running 
them locally. 

TestEngine is a standalone server that exposes a REST API for running API tests, it receives and runs *test recipes* 
in the same underlying JSON format that is also used in the test shown above. If you're using the RecipeExecutionFacade 
(as in the example above) all you have to do is add system (or environment) variables that point the facade to a 
running TestEngine instance. For example, if we add

```
testengine.endpoint=http://testengine.readyapi.io:8080
testengine.user=demoUser
testengine.password=demoPassword
```
	
as either system/env properties to our execution and then rerun the above test - those tests will be executed by the 
specified TestEngine instance available at http://testengine.readyapi.io.

### Logging of Recipes and HTTP transactions

Usage of the facade as in the above examples also enables logging of both generated recipes and HTTP transaction logs 
of executed tests (in HAR file format). Adding the following two properties:

```
swagger-assert4j.log.executions.folder=target/logs/executions
swagger-assert4j.log.recipes.folder=target/logs/recipes
```

will automatically result in the corresponding artifacts being written to the corresponding folders.

## Learn More about TestEngine

[Try it out online!](http://testengine.readyapi.io)

[ReadyAPI TestEngine](http://readyapi.smartbear.com/testengine/intro/about)

[ReadyAPI](http://readyapi.smartbear.com/start)

## License

This library is licensed under the Apache 2.0 License - copyright Smartbear Software
