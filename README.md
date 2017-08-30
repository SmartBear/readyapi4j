# Swagger Assert4J - a Java library for API testing

The Assert4J library lets you test APIs through Java, Groovy or Cucumber. The library has extensive support for REST, SOAP, JDBC and JMS protocols. Under the hood the library uses the test-execution engine of [SoapUI](http://www.soapui.org).

Read on to get started
* [with Java](#getting-started-with-java) - together with any testing framework
* [with Groovy](#getting-started-with-groovy) - together with any testing framework 
* [with Cucumber](modules/cucumber) - with cucumber for java 

## Getting Started with Java

1. Add the following Maven dependency to your project:
 
	```xml
	<dependency>
		<groupId>io.swagger.assert</groupId>
		<artifactId>assert4j-facade</artifactId>
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
                         query( "specType", "API" ),
                         query( "query", "testserver" )
                     )
                     .withAssertions(
                         json("$.totalCount", "4")
                     )                 
                 );
    
            assertExecutionResult(result);
        }
	```

3. Run your test.

    Here is some sample output of the method above:
    ```
    Errors: [[JsonPath Match] Comparison failed for path [$.totalCount], expecting [1], actual was [0]] 
    ```

4. Look at the unit tests to see all the functionality available, or [Dive into the javadocs](http://smartbear.github.io/swagger-assert4j/apidocs/) to get an overview of the Java API.

### Running tests with TestServer

To get access to extended functionality like data-driven testing, centralized execution and reporting, etc., you 
need to use [ReadyAPI TestServer](http://readyapi.smartbear.com/testserver/start) for test execution. 

TestServer is a standalone server that exposes a REST API for running API tests, it receives and runs *test recipes* 
in the same underlying JSON format that is also used in the test shown above. If you're using the RecipeExecutionFacade 
(as in the example above) all you have to do is add system (or environment) variables that point the facade to a 
running TestServer instance. For example, if we add

```
testserver.endpoint=http://testserver.readyapi.io:8080
testserver.user=demoUser
testserver.password=demoPassword
```
	
as either system/env properties to our execution and then rerun the above test - those tests will be executed by the 
specified TestServer instance available at http://testserver.readyapi.io.

### Logging of Recipes and HTTP transactions

Usage of the facade as in the above examples also enables logging of both generated recipes and HTTP transaction logs 
of executed tests (in HAR file format). Adding the following two properties:

```
swagger-assert4j.log.executions.folder=target/logs/executions
swagger-assert4j.log.recipes.folder=target/logs/recipes
```

will automatically result in the corresponding artifacts being written to the corresponding folders.

## Getting Started with Groovy 

Assert4J provides a Groovy DSL to create and execute API tests locally or on TestServer. 
The following steps explain how to use this DSL in a JUnit test.

1. Add the following Maven dependency to your project:
 
	```xml
	<dependency>
		<groupId>io.swagger.assert</groupId>
		<artifactId>assert4j-groovy-dsl</artifactId>
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
                        query 'query', 'testserver'
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
   
   Similarly, you can execute the recipe on TestServer with the following:
   ```groovy
   import io.swagger.assert4j.execution.Execution
   import org.junit.Test
   
   import static io.swagger.assert4j.dsl.execution.RecipeExecution.executeRecipeOnServer
   
   class DslTestDemo {
       @Test
       void testSwaggerHubApi() {
           Execution execution = executeRecipeOnServer '<your TestServer url, e.g. http://localhost:8080>', '<your user>', '<your password>', {
               get 'https://api.swaggerhub.com/specs', {
                   parameters {
                       query 'specType', 'API'
                       query 'query', 'testserver'
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
       io.swagger.assert4j.testserver.execution.TestServerExecution@dfddc9a
```
## More samples / tutorials

Tutorial in the ReadyAPI TestServer documentation: 
[Creating Code-Based Recipes: Tutorial](http://readyapi.smartbear.com/testserver/tutorials/code_based/start)

The [samples submodule](modules/samples) here on GitHub contains a number of samples for Java, Groovy and Maven.

## Learn More about TestServer

[Try it out online!](http://testserver.readyapi.io)

[ReadyAPI TestServer](http://readyapi.smartbear.com/testserver/intro/about)

[ReadyAPI](http://readyapi.smartbear.com/start)

## License

This library is licensed under the Apache 2.0 License - copyright Smartbear Software
