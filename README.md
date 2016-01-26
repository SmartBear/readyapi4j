# Java Client Library for Ready! API TestServer

This repository contains source file for the Java Client library for [Ready! API TestServer](http://readyapi.smartbear.com/testserver/start) <html>&ndash;</html> a UI-less tool that run API tests. 

TestServer receives and runs *test recipes* <html>&ndash;</html> special JSON requests that describe API test actions to be executed. This **Java Client** library lets you create and run test recipes from within your Java code, without installing Ready! API or any other API testing tool on your computer.

### Using the Library
<html>&rArr;</html> **[Using the Java Client](http://readyapi.smartbear.com/testserver/tutorials/code_based/start)** <html><span style="color : #555;">(in Ready! API TestServer documentation)</span></html>

## Quick Start

Here comes a quick guide to get you started:

1. Add the maven dependency

```xml
<dependency>
    <groupId>com.smartbear.readyapi</groupId>
    <artifactId>ready-api-testserver-client</artifactId>
    <version>1.0.1</version>
</dependency>

```

2. Create a simple request recipe in a test

```java
    @Test
    public void dumpsRecipe() throws Exception {
        TestRecipe recipe = newTestRecipe()
            .addStep(
                getRequest("https://api.swaggerhub.com/apis")
                    .addQueryParameter("query", "testserver")
                    .assertJsonContent("$.totalCount", "1" )
            )
            .buildTestRecipe();

        RecipeExecutor executor = new RecipeExecutor( "<your testserver hostname>" );
        executor.setCredentials("<your user>", "<your password>");
        Execution execution = executor.executeRecipe(recipe);

        assertEquals(Arrays.toString( execution.getErrorMessages().toArray()),
            ProjectResultReport.StatusEnum.FINISHED, execution.getCurrentStatus());
    }
```

(you could set up the executor in a setup method)

3. Run it!

The above will at the time of writing result in 

```
java.lang.AssertionError: [[JsonPath Match] Comparison failed for path [$.totalCount], expecting [1], actual was [0]] 
Expected :FINISHED
Actual   :FAILED
```

### Learn More
[Ready! API TestServer](http://readyapi.smartbear.com/testserver/intro/about)

[Ready! API](http://readyapi.smartbear.com/start)

- - - 
<html>&copy;</html> 2016 SmartBear Software





