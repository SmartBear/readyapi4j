# Java Client Library for Ready! API TestServer

This repository contains source file for the Java Client library for [Ready! API TestServer](http://readyapi.smartbear.com/testserver/start), a standalone server that exposes a REST API for running API tests. 

TestServer receives and runs *test recipes*; special JSON requests that describe API tests to be executed. 
This **Java Client** library lets you create and run test recipes from within your Java code, without installing 
Ready! API or any other API testing tool on your computer.

## Quick Guide

1. Add the following Maven dependency to your project:
 
	```xml
	<dependency>
		<groupId>com.smartbear.readyapi</groupId>
		<artifactId>ready-api-testserver-client</artifactId>
		<version>1.1.0</version>
	</dependency>
	```

2. Create a test recipe in Java:

	```java
	@Test
	public void testSwaggerHubApi() throws Exception {
		TestRecipe recipe = newTestRecipe()  /* Create a new recipe */
			.addStep(
				getRequest("https://api.swaggerhub.com/apis") /* Add a test step (REST Request) */
					.addQueryParameter("query", "testserver") /* Specify request parameters */
					.assertJsonContent("$.totalCount", "1" )
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
