# ReadyApi4J - a Java library for API tests

The ReadyApi4J library lets Java developers use the API testing functionality in SoapUI from Java code. In spite of its somewhat unfortunate name, which is there for historical reasons, SoapUI supports many protocols in addition to SOAP, notably REST, JDBC and JMS.

ReadyApi4J builds *test recipes*, which describe API tests to be executed.

Under the hood ReadyApi4J uses a JSON format, but there's no need to learn it, because you typically create and run test recipes using a fluent Java API. Nor do you need to install SoapUI or any other software to be able to execute recipes.

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
