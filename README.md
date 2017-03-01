# ReadyApi4J - a Java library for API tests

The ReadyApi4J library lets Java developers use the API testing functionality in SoapUI from Java code. In spite of its somewhat unfortunate name, which is there for historical reasons, SoapUI supports many protocols in addition to SOAP, notably REST, JDBC and JMS.

ReadyApi4J builds *test recipes*, which describe API tests to be executed.

Under the hood ReadyApi4J uses a JSON format, but there's no need to learn it, because you typically create and run test recipes using a fluent Java API or the Groovy DSL. Nor do you need to install SoapUI or any other software to be able to execute recipes.

## Running local SoapUI tests on your machine

The support for local SoapUI tests is ready to use but is not released yet. Therefore, until the upcoming 2.0.0 release, there are no public artefacts uploaded to the Maven repos. However if you build ReadyApi4J locally, with Maven, you can start using it in the following way.

1. Add the following Maven dependency to your project:
 
	```xml
	<dependency>
		<groupId>com.smartbear.readyapi</groupId>
		<artifactId>readyapi4j-facade</artifactId>
		<version>2.0.0-SNAPSHOT</version>
	</dependency>
	```

2. Create a test recipe and execute it in Java:

	```java
	import static com.smartbear.readyapi4j.facade.execution.RecipeExecutionFacade.executeRecipe;
	import static com.smartbear.readyapi4j.teststeps.TestSteps.GET;
	import com.smartbear.readyapi4j.result.RecipeExecutionResult;
		
	TestRecipe recipe = newTestRecipe(
		       GET("https://api.swaggerhub.com/apis") /* Add a test step (REST Request) */
					.addQueryParameter("query", "testserver") /* Specify request parameters */
					.assertJsonContent("$.totalCount", "1" ) /* assert the contents using JSONPath */
				);
	RecipeExecutionResult result = RecipeExecutionFacade.executeRecipe(recipe);
	System.out.println("Errors: " + result.getErrorMessages());
	```


3. Run your code.

    Here is some sample output of the method above:
    ```
    Errors: [[JsonPath Match] Comparison failed for path [$.totalCount], expecting [1], actual was [0]] 
    ```

4. Look at the unit tests to see all the functionality available, or [Dive into the javadocs](http://smartbear.github.io/readyapi4j/apidocs/) to get an overview of the Java API.

## Running tests on the Ready! API TestServer

To get access to the most important functionality in SoapUI's commercial sibling, Ready! API, you need to use
[Ready! API TestServer](http://readyapi.smartbear.com/testserver/start), a standalone server that exposes a 
REST API for running API tests. 

TestServer receives and runs *test recipes* in the underlying JSON format. 

ReadyApi4J library lets you create and run test recipes from within your Java code, without installing 
Ready! API or any other API testing tool on your computer.

### Quick Guide

1. Add the following Maven dependency to your project:
 
	```xml
	<dependency>
		<groupId>com.smartbear.readyapi</groupId>
		<artifactId>ready-api-testserver-client</artifactId>
		<version>1.2.1</version>
	</dependency>
	```

2. Create a test recipe in Java:

	```java
	@Test
	public void testSwaggerHubApi() throws Exception {
		TestRecipe recipe = newTestRecipe(
		       GET("https://api.swaggerhub.com/apis") /* Add a test step (REST Request) */
					.addQueryParameter("query", "testserver") /* Specify request parameters */
					.assertJsonContent("$.totalCount", "1" ) /* assert the contents using JSONPath */
				)
			.buildTestRecipe(); /* Generate the recipe */
		
		/* Create the recipe executor for your TestServer */
		RecipeExecutor executor = new RecipeExecutor( "<your TestServer hostname>" );
		
		/* User credentials for connecting to the TestServer */
		executor.setCredentials("<your user>", "<your password>");
		
		/* Run the recipe */
		Execution execution = executor.executeRecipe(recipe);
	
		/* Checks the response */
		assertEquals(Arrays.toString( execution.getErrorMessages().toArray()),
		    ProjectResultReport.StatusEnum.FINISHED, execution.getCurrentStatus());
	}
	```
	You can set up the executor in a setup method.


3. Run your code.

    Here is a sample output of the method above:
    
    ```
    java.lang.AssertionError: [[JsonPath Match] Comparison failed for path [$.totalCount], expecting [1], actual was [0]] 
    Expected :FINISHED
    Actual   :FAILED
    ```

4. [Dive into the javadocs](http://smartbear.github.io/readyapi4j/apidocs/) to get an overview of the Java API

## Groovy DSL for creating and executing API tests
ReadyApi4J provides a Groovy DSL to create and execute API tests locally or on TestServer. 
The following steps explain how to use this DSL in a JUnit test.

1. Add the following Maven dependency to your project:
 
	```xml
	<dependency>
		<groupId>com.smartbear.readyapi</groupId>
		<artifactId>readyapi4j-groovy-dsl</artifactId>
		<version>2.0.0-SNAPSHOT</version>
	</dependency>
	```

2. Create a JUnit test with a test recipe in Groovy:

  The example below shows how to create and execute a recipe with one single step locally, using the SoapUI OS engine. 
  This requires the additional dependency on com.smartbear.readyapi:readyapi4j-local, but there is no need to install SoapUI. 
   ```groovy
   import com.smartbear.readyapi4j.execution.Execution
   import org.junit.Test
   
   import static com.smartbear.readyapi4j.dsl.TestDsl.recipe
   import static com.smartbear.readyapi4j.dsl.execution.RecipeExecution.executeRecipe

    class DslTestDemo {
    
        @Test
        void testSwaggerHubApi() {
           //Executes recipe locally - this requires the additional dependency com.smartbear.readyapi:readyapi4j-local
            Execution execution = executeRecipe {
                get 'https://api.swaggerhub.com/apis', {
                    parameters {
                        query 'query', 'testserver'
                    }
                    asserting {
                        jsonPath '$.totalCount' occurs 0 times
                    }
                }
            }
            println execution.errorMessages
        }
    }   
   ```
   Here is sample output from this test:
   ```
   [[JsonPath Count] Comparison failed for path [$.totalCount], expecting [0], actual was [1]]
   ```
   
   Similarly, you can execute the recipe on TestServer with the following:
   ```groovy
   import com.smartbear.readyapi4j.execution.Execution
   import org.junit.Test
   
   import static com.smartbear.readyapi4j.dsl.TestDsl.recipe
   import static com.smartbear.readyapi4j.dsl.execution.RecipeExecution.executeRecipeOnServer
   
   class DslTestDemo {
       @Test
       void testSwaggerHubApi() {
           Execution execution = executeRecipeOnServer '<your TestServer url, e.g. http://localhost:8080>', '<your user>', '<your password>', {
               get 'https://api.swaggerhub.com/apis', {
                   parameters {
                       query 'query', 'testserver'
                   }
                   asserting {
                       jsonPath '$.totalCount' occurs 0 times
                   }
               }
           }
           println execution.errorMessages
       }
   }
   ```
Here is sample output from this test:
```
[TestStepName: GET request 1, messages: [JsonPath Count] Comparison failed. Path: [$.totalCount]; Expected value: [0]; Actual value: [1].]
```
## More samples / tutorials

Tutorial in the Ready! API TestServer documentation: 
[Creating Code-Based Recipes: Tutorial](http://readyapi.smartbear.com/testserver/tutorials/code_based/start)

The [TestServer Samples Project](https://github.com/SmartBear/ready-api-testserver-samples) here on GitHub contains a 
number of JUnit and CucumberJVM samples using this library.

## Learn More about TestServer

[Try it out online!](http://testserver.readyapi.io)

[Ready! API TestServer](http://readyapi.smartbear.com/testserver/intro/about)

[Ready! API](http://readyapi.smartbear.com/start)

## License

This library is licensed under the Apache 2.0 License - copyright Smartbear Software