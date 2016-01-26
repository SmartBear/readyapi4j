# Java Client Library for Ready! API TestServer

This repository contains source file for the Java Client library for [Ready! API TestServer](http://readyapi.smartbear.com/testserver/start) <html>&ndash;</html> a UI-less tool that run API tests. 

TestServer receives and runs *test recipes* <html>&ndash;</html> special JSON requests that describe API test actions to be executed. This **Java Client** library lets you create and run test recipes from within your Java code, without installing Ready! API or any other API testing tool on your computer.

## Quick Guide

1. Add the following Maven dependency to your project:
 
		```xml
		<dependency>
			<groupId>com.smartbear.readyapi</groupId>
			<artifactId>ready-api-testserver-client</artifactId>
			<version>1.0.1</version>
		</dependency>
		```

2. Create a test recipe in Java:

		```java
		@Test
		public void dumpsRecipe() throws Exception {
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

## Using the Java Client Library
<html>&rArr;</html> **[Creating Code-Based Recipes: Tutorial](http://readyapi.smartbear.com/testserver/tutorials/code_based/start)** <html><span style="color : #555;">(in Ready! API TestServer documentation)</span></html>


### Learn More
[Ready! API TestServer](http://readyapi.smartbear.com/testserver/intro/about)

[Ready! API](http://readyapi.smartbear.com/start)

- - - 
<html>&copy;</html> 2016 SmartBear Software





