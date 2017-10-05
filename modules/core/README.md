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
  * [JDBC Request](#jdbc-request)
* [Assertions](#assertions)
  * [HTTP Assertions](#http-assertions)
  * [Content Assertions](#content-assertions)
  * [Miscellaneous Assertions](#miscellaneous-assertions)
* [Property Transfers](#property-transfers)
* [Extractors](#extractors)
* [Executing Recipes](#executing-recipes)
  * [Execution Listeners](#execution-listeners)
  * [Recipe Filters](#recipe-filters)
* [Execution Results](#execution-results)
  
  
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

Script TestSteps allow you to execute an arbitrary block of Groovy code as part of your test, for example 

```java
```java
TestRecipe recipe = TestRecipeBuilder.buildRecipe(  
    GET( "http://petstore.swagger.io/v2/pet/findByStatus?status=test" ),
    delayStep( 1000 ), // wait for one second...
    groovyScriptStep( "System.out.println(\"Hello world!\")" ),
    GET( "http://petstore.swagger.io/v2/pet/(id}" )
):
``` 
The specified code has access to the complete underlying object-model of the executing test, see 
https://www.soapui.org/scripting---properties/the-soapui-object-model.html and https://www.soapui.org/functional-testing/working-with-scripts.html
for more info. 

## JDBC Request

Test recipes can contain JDBC requests to interact with relational databases as part of a test. This can be valuable for either 
initializing data needed for a test - or validating data that previous TestSteps are meant to create/modify. To use the JDBC
TestStep make sure you have the corresponding JDBC driver(s) in your classpath - then create a 
[JdbcConnection](https://smartbear.github.io/swagger-assert4j/apidocs/index.html?io/swagger/assert4j/teststeps/jdbcrequest/JdbcConnection.html) 
object to the database which can be used to build the actual TestSteps:

```java
JdbcConnection connection = jdbcConnection("org.mysql.Driver", "jdbc:mysql://localhost/mydb" );

TestRecipe recipe = TestRecipeBuilder.buildRecipe(  
   connection.jdbcRequest("select * from some_table")
):
```
Internally the result returned from the query is converted to XML format - which you could for example use this in 
combination with a property-transfer:  

```java
JdbcConnection connection = jdbcConnection("org.mysql.Driver", "jdbc:mysql://localhost/mydb" );

TestRecipe recipe = TestRecipeBuilder.buildRecipe(  
    connection.jdbcRequest("select id from pets").named( "SelectPetIds"),
    propertyTransfer(
           // use xpath to select first returned id
           fromPreviousResponse( "//id[0]" ).toNextRequestProperty("petId")
       ),
       GET( "http://petstore.swagger.io/v2/pet/{petId}" ).named( "getPet" ).
           withAssertions(
               statusCodes( 200 )
           )
):
```

# Assertions

Assertions can be added to REST, SOAP and JDBC request teststeps via the `withAssertions(...)` method to assert the content of their respective responses.
The [Assertions](https://smartbear.github.io/swagger-assert4j/apidocs/index.html?io/swagger/assert4j/assertions/Assertions.html)
method contains factory methods that allow you to easily create any of the supported assertions, for example;

```java
TestRecipe recipe = TestRecipeBuilder.buildRecipe(  
   soapRequest(new URL("http://www.webservicex.com/globalweather.asmx?WSDL"))
                  .forBinding("GlobalWeatherSoap11")
                  .forOperation("GetWeather")
                  .withParameter("CountryName", "Sweden")
                  .withPathParameter("//*:CityName", "Stockholm")
      .withAssertions(
          validStatusCodes( 200 ),
          notSoapFault(),
          xPathContent( "//*:CityName", "Stockholm")
      )
):
```

## HTTP Assertions

The following HTTP-specific assertions are available:
* `contentType( String contentType )` - asserts the content-type of the response 
* `headerExists( String header )` - asserts that the specified HTTP header exists in the response
* `headerValue( String header, String value)` - asserts that the specified HTTP header has the specified value in the response 
* `validStatusCodes( Integer... statusCodes)` - asserts that the response has one of the specified HTTP status codes 
* `invalidStatusCodes( Integer... statusCodes)contentType` - asserts that the response does not have one of the specified HTTP status codes 

## Content Assertions

These assertions are for validating the body content of a response:
* `contains(String token)` - asserts that the response body contains the specified token
* `notContains(String token)` - asserts that the response body does not contain the specified token
* `matches( String regexToken)` - asserts that the response matches the specified regular expression
* `json(String jsonPath, String expectedContent)` - asserts that the node identified by the specified jsonPath contains the expected value
* `jsonCount( String jsonPath, int count)` - asserts that the number of nodes selected by the specified jsonPath matches the specified count
* `jsonExists( String jsonPath )` - asserts that a node exists at the specified JSON Path
* `jsonNotExists( String jsonPath )` - asserts that a node does not exist at the specified JSON Path
* `xPathContains( String xpath, String expectedValue )` - asserts that the XML node at the specified XPath contains the exptected value
* `xQueryContains( String xquery, String expectedValue )` - asserts that the value selected by the specified XQuery contains the exptected value

Internally the execution engine uses:
* [JsonPath](https://github.com/json-path/JsonPath) for JSON Path evalution
* [Saxonica](http://www.saxonica.com/documentation/documentation.xml) for XPath/XQuery evaluation

## SOAP Assertions

The following SOAP-specific assertions are available:
* `notSoapFault()` - assert that the SOAP response it not a SOAP Fault
* `soapFault()` - assert that the SOAP response is a SOAP Fault (for negative testing) 
* `schemaCompliance()` - asserts that the response is compliant with the XML Schema defined for the response message

## JDBC Assertions

The following assertions are available for JDBC TestSteps:
* `jdbcRequestStatusOk()` - asserts that the JDBC statement was executed successfully
* `jdbcRequestTimeout( long timeout)` - asserts the JDBC response-time in milliseconds

## Miscellaneous Assertions

* `maxResponseTime( long timeInMillis)` - assert the response time of the request to be within the specified time
* `script(String script)` - execute the specified groovy script for asserting the response - see [Using Script Assertions](https://www.soapui.org/functional-testing/validating-messages/using-script-assertions.html) for some examples

# Properties and Property Expansion

Properties are a common concept in the SoapUI execution engine; all TestSteps expose properties and there is even 
a dedicated Properties TestStep for defining properties for a Test:

```java
TestRecipe testRecipe = newTestRecipe(
            properties(
                property( "username", "..."),
                property( "password", "...")
            )
            .named("Properties"),
            ...more TestSteps...
        ).buildTestRecipe();
``` 

Using defined properties is done via [Property Expansion](https://www.soapui.org/scripting---properties/property-expansion.html), 
for example, the above defined properties could be user for authentication:

```java
TestRecipe testRecipe = newTestRecipe(
           properties(
               property( "username", "..."),
               property( "password", "...")
           )
           .named("Properties"),
           GET("..some endpoint..")
               .withAuthentication(basic("${Properties#username}", "${Properties#password}")
               )
           )
        ).buildTestRecipe();
``` 

Of course you could simply define username/password as java variables when building the recipe; the advantage of
using properties is related to the augmentation and reuse of recipes as described under recipe filters below.

# Extractors

Extractors allow you to easily extract values from a response message using XPath or JSONPath. For example you 
might want to use those values as input to other tests that are executed as part of an 
orchestrated end-to-end test suite:

```java
String conversionRate;

TestRecipe recipe = TestRecipeBuilder.buildRecipe(  
    soapRequest(new URL("http://www.webservicex.com/CurrencyConvertor.asmx?wsdl"))
        .forBinding("CurrencyConvertorSoap")
        .forOperation("ConversionRate")
        .named( "GetConversionRate")
        .withParameter("FromCurrency", "USD")
        .withParameter("ToCurrency", "SEK")
        .withAssertions(
            schemaCompliance(),
            notSoapFault()
        )
        .withExtractors(
            fromResponse( "//*:ConversionRateResult/text()", v -> {
                 conversionRate = v;
            })
        )
        
// execute recipe and use extracted conversionRate for something else
RecipeExecutionFacade.executeRecipe( recipe );    
```

# Executing Recipes

The [Concepts](../../CONCEPTS.md) document shows how to execute recipes using both the local and remote execution 
engines via the [RecipeExecutionFacade]() class. Using the underlying [RecipeExecutorBuilder]() makes it possible to 
augment execution using execution listeners and recipe filters as described below.

The actual [Execution](https://smartbear.github.io/swagger-assert4j/apidocs/index.html?io/swagger/assert4j/execution/Execution.html)
object available for the execution of a recipe is useful for querying asynchronous test execution status:

```java
// use submitRecipe for async execution
Execution execution = RecipeExecutionFacade.submitRecipe( ... );

while( execution.getCurrentStatus() == RUNNING ){
    // do something useful
}

System.out.println( "Test finished with status: " + execution.getCurrentStatus() );
``` 

## Execution Listeners

[ExecutionListener](https://smartbear.github.io/swagger-assert4j/apidocs/index.html?io/swagger/assert4j/ExecutionListener.html)s 
get notified of specific events related to recipe execution - for example the provided 
[ExecutionLogger](https://smartbear.github.io/swagger-assert4j/apidocs/index.html?io/swagger/assert4j/support/ExecutionLogger.html) 
writes all execution transaction logs as HAR files to a specified folder:

```java
// build a local executor that logs executions to a logs folder
RecipeExecutor executor = new RecipeExecutorBuilder()
    .withExecutionListener(new ExecutionLogger("logs"))
    .buildLocal();

executor.executeRecipe( ... )
```

Since the `ExecutionLogger` is commonly used the `RecipeExecutorBuilder` actually has a 
dedicated `withExecutionLog( String logFolder)` method:

```java
// build a local executor that logs executions to a logs folder
RecipeExecutor executor = new RecipeExecutorBuilder()
    .withExecutionLog("logs")
    .buildLocal();

executor.executeRecipe( ... )
```  

## Recipe Filters

[RecipeFilter](https://smartbear.github.io/swagger-assert4j/apidocs/index.html?io/swagger/assert4j/execution/RecipeFilter.html)s
can be used to augment the underlying [TestRecipe](https://smartbear.github.io/swagger-assert4j/apidocs/index.html?io/swagger/assert4j/TestRecipe.html)
for a test before it is executed; for example it could add common authentication settings to all REST Requests, or 
common property values to all TestSteps of a certain type.

The included [RecipeLogger](https://smartbear.github.io/swagger-assert4j/apidocs/index.html?io/swagger/assert4j/support/RecipeLogger.html)
filter logs all generated recipes to a specified folder - which can be useful for debugging/logging purposes or if you want to repurposes
these recipes as LoadTests in [LoadUI](https://smartbear.com/product/ready-api/loadui) or API monitors using [AlertSite](https://smartbear.com/product/alertsite/integrations/ready-api-and-soapui/) 

```java
// build a local executor that logs json recipes to a logs folder
RecipeExecutor executor = new RecipeExecutorBuilder()
    .withRecipeFilter(new RecipeLogger("logs"))
    .buildLocal();

executor.executeRecipe( ... )
```

Similar to the `ExecutionLogger` above, the `RecipeExecutorBuilder` has a 
dedicated `withRecipeLog( String logFolder)` method:

```java
// build a local executor that logs executions to a logs folder
RecipeExecutor executor = new RecipeExecutorBuilder()
    .withRecipeLog("logs")
    .buildLocal();

executor.executeRecipe( ... )
```  

# Execution Results

When an execution finishes the `Execution` object exposes a 
[RecipeExecutionResult](https://smartbear.github.io/swagger-assert4j/apidocs/index.html?io/swagger/assert4j/result/RecipeExecutionResult.html)
 that gives access to a [TestStepResult](https://smartbear.github.io/swagger-assert4j/apidocs/index.html?io/swagger/assert4j/result/TestStepResult.html) 
for each executed TestStep. For example if you want to log the response content for all executed TestSteps you can do:

```java
RecipeExecutionResult result = executor.executeRecipe(...);

for( TestStepResult testStepResult : result.getTestStepResults()){
    System.out.println( "TestStep [" + testStepResult.getTestStepName() + "] returned [" + testStepResult.getResponseContent() + "]");
}
``` 






