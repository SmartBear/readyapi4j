# Swagger Assert4j Core 

This modules provides the core Java APIs for creating and executing Test recipes which can then be executed locally or
remotely as described in the [Concepts](../../CONCEPTS.md) document. 

* [Fluent vs Pojo API](#fluent-vs-pojo-api)
* [Recipes](#recipes)
* [Test Steps](#test-steps)
  * [REST Requests](#rest-requests)
    * [Parameters](#parameters) 
    * [Content](#content)
    * [Authentication](#authentication)
    * [Attachments](#attachments)
  * [SOAP Requests](#soap-requests)
  * [Property Transfers](#property-transfers)
  * [Delay](#delay)
  * [Script](#script)
  * [MockResponse](#mockresponse)
  * [JDBC Request](#jdbc-request)
  * [Properties](#properties)
  * [Plugin](#plugin)
* [Assertions](#assertions)
  * [HTTP Assertions](#http-assertions)
  * [Content Assertions](#content-assertions)
    * [JSON](#json)
    * [XML](#xml)
  * [Script](#script)
* [Extractors](#extractors)
* [Executing Recipes](#executing-recipes)
  * [Execution Listeners](#execution-listeners)
  * [Recipe Filters](#recipe-filters)
* [Results](#results)
  * [Transaction Logs](#transaction-logs)
  
  
# Fluent vs Pojo API

To simplify the creation of Test Recipes POJOs that get serialized to the corresponding JSON Recipes before execution this
module provides a fluent API layer that attempts to provide a more "verbal" way of expressing Recipes through code. All
the examples below will use this fluent API - but if you're interested in the underlying POJO classes please have a look 
at the source or core javadocs. The [java samples module](../samples/java) contains examples using both the fluent and
pojo approach. 

# Recipes

Assert4j expresses tests as "recipes" - which are an ordered list of steps that are executed one after the other when
executed. There are a fair number of built-in test steps (outlined below) - and there is an underlying extension 
mechansim for providing custom steps also.

Building a recipe with the fluent API is easiest done with the [TestRecipeBuilder](https://smartbear.github.io/swagger-assert4j/apidocs/index.html?io/swagger/assert4j/TestRecipeBuilder.html)
class:

```java
TestRecipe recipe = TestRecipeBuilder.buildRecipe( ...list of TestStepBuilder objects... ):

// optionally add some more teststeps
recipe.addStep( ...another TestStepBuilder object... );

// execute the test!
RecipeExecutionResult result = RecipeExecutorBuilder.buildDefault().executeRecipe( recipe );
``` 

# Test Steps

Test steps represent the actual actions performed during the execution of a test, the 
[TestSteps](https://smartbear.github.io/swagger-assert4j/apidocs/index.html?io/swagger/assert4j/teststeps/TestSteps.html) class provides factory
methods for creating TestStepBuilders for each supported test step type - as you will see below. 

## REST Requests

The TestSteps class provides convenience methods for the 
common HTTP Verbs:

```java
TestRecipe recipe = TestRecipeBuilder.buildRecipe(  
    GET( "http://petstore.swagger.io/v2/store/inventory" ),
    POST( "http://petstore.swagger.io/v2/store/order" ),
    DELETE( "http://petstore.swagger.io/v2//pet/{petId}"),
    GET( "http://petstore.swagger.io/v2/pet/findByStatus" )
):
``` 

### Parameters

Both the DELETE and last GET in this example require parameter values - let's add them:

```java
TestRecipe recipe = TestRecipeBuilder.buildRecipe(  
    GET( "http://petstore.swagger.io/v2/store/inventory" ),
    POST( "http://petstore.swagger.io/v2/store/order" ),
    DELETE( "http://petstore.swagger.io/v2//pet/{petId}").
        withPathParameter( "petId", "1" )
    ,
    GET( "http://petstore.swagger.io/v2/pet/findByStatus" ).
        withQueryParameter( "status", "test")
):
``` 
 
If you need to add multiple parameters you can use 
 
```java
TestRecipe recipe = TestRecipeBuilder.buildRecipe(  
  GET( "http://petstore.swagger.io/v2/pet/findByStatus" ).
     withParameters( 
         query( "status", "test"),
         query("limit", "10")
     )
):
 ``` 

### Content

Adding content to a POST or PUT is straight-forward:

```java
TestRecipe recipe = TestRecipeBuilder.buildRecipe(  
    POST( "http://petstore.swagger.io/v2/store/order" ).
        withMediaType( "application/json").
        withRequestBody( ...some object that can be serialized to JSON... )
):
``` 

the built in serialization support json, yaml and xml media types.

### Authentication

If you need to add authentication to your request, then use one of the factory methods
provided by the [Authentications](https://smartbear.github.io/swagger-assert4j/apidocs/index.html?io/swagger/assert4j/auth/Authentications.html) class.

```java
TestRecipe recipe = TestRecipeBuilder.buildRecipe(  
  GET( "http://petstore.swagger.io/v2/pet/findByStatus" ).
     withParameters( 
         query( "status", "test"),
         query("limit", "10")
     ).
     withAuthentication(
        basic( "username", "password"),
        oAuth2().
            withAccessToken( ... ) 
     )
):
 ``` 

### Attachments

If you'd like to attach a file to the body of a request instead of providing it as content as shown above, you can 
 use the `withAttachments(...)` method together with the factory methods in the 
  [Attachments](https://smartbear.github.io/swagger-assert4j/apidocs/index.html?io/swagger/assert4j/attachments/Attachments.html) class

```java
TestRecipe recipe = TestRecipeBuilder.buildRecipe(  
  POST( "http://petstore.swagger.io/v2/store/order" ).
     withAttachments(
         file( "request.json", "application/json" )
     )
):
 ``` 

## SOAP Requests

The built in SOAP support makes it super-easy to call SOAP Services; you'll need to provide the underlying WSDL and
info on which binding and operation to call, filling out the actual message body can either be done using utility
methods or manually by providing the request XML:

```java
TestRecipe recipe = TestRecipeBuilder.buildRecipe(  
   soapRequest(new URL("http://www.webservicex.com/globalweather.asmx?WSDL"))
                  .forBinding("GlobalWeatherSoap11")
                  .forOperation("GetWeather")
                  .withParameter("CountryName", "Sweden")
                  .withPathParameter("//*:CityName", "Stockholm")
):
 ``` 
As you can see in this example we're making a call to the `GetOperation` method defined in the WSDL. The actual XML 
request for this operation looks as follows:

```xml
<soapenv:Envelope
    xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
    xmlns:web="http://www.webserviceX.NET">
    <soapenv:Header/>
    <soapenv:Body>
        <web:GetWeather>
            <web:CityName>?</web:CityName>
            <web:CountryName>?</web:CountryName>
        </web:GetWeather>
    </soapenv:Body>
</soapenv:Envelope>
```

Fortunately we never have to create any XML, we simply set the `CountryName` parameter
using simple the corresponding element name ignoring namespaces. The `CityName` parameter is set using an XPath 
pointer - but it could equally have been done with the `withParameter` method.

## Property Transfers

Transferring values from the response of one TestStep to the request of another is a very common task when creating
multi-step tests. The Property Transfer TestStep handles this for you:

```java
TestRecipe recipe = TestRecipeBuilder.buildRecipe(
    GET( "http://petstore.swagger.io/v2/pet/findByStatus?status=test" ).named("getPets"),
    propertyTransfer(
        fromPreviousResponse( "$.[0].id" ).toNextRequestProperty("petId")
    ),
    GET( "http://petstore.swagger.io/v2/pet/{petId}" ).named( "getPet" ).
        withAssertions(
            statusCodes( 200 )
        )
);
```
The example above uses XPath to extract the `id` property of the first item in the response to the `petId` path 
parameter in the following request - using the `fromPreviousResponse` and `toNextRequest` convenience methods in the
[PropertyTransferBuilder](https://smartbear.github.io/swagger-assert4j/apidocs/index.html?io/swagger/assert4j/teststeps/propertytransfer/PropertyTransferBuilder.html) class.

## Delay

Use the `delayStep` to insert a delay between two TestSteps:

```java
TestRecipe recipe = TestRecipeBuilder.buildRecipe(  
    GET( "http://petstore.swagger.io/v2/pet/findByStatus?status=test" ),
    delayStep( 1000 ), // wait for one second...
    GET( "http://petstore.swagger.io/v2/pet/(id}" )
):
``` 

## Script

## MockResponse

## JDBC Request

## Properties

## Plugin

# Assertions

## HTTP Assertions

## Content Assertions

### JSON

### XML

## Script

# Extractors

# Executing Recipes

## Execution Listeners

## Recipe Filters

# Results

## Transaction Logs







