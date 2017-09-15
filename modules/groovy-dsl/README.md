# Domain-specific language (DSL) for tests

We've created Swagger Assert4J to make the full functionality of SoapUI and ReadyApi 
TestServer accessible to coders. However, we also had a more ambitious vision: to let testers and 
other people with limited coding skills understand or even write API tests.

We're trying to achieve this both with the [Cucumber functionality](../cucumber/README.md), and with our **Domain-specific language for tests**.

## What is a domain-specific language?

While most programming languages are general-purpose, a domain-specific language (DSL) by definition has a more limited scope. 
In our case, the scope is to create API test and other integration tests. A DSL usually
has simpler syntax and is more similar to a natural language, such as English.

## The Assert4J DSL

The Assert4J DSL is a so-called [internal DSL](https://martinfowler.com/bliki/InternalDslStyle.html), based on Groovy. 
In other words the DSL scripts are Groovy code, which can be run inside any Groovy class or script, but we're trying hard 
to make that code as similar to plain English as possible.

Here's an example (without import statements):
 
 ```groovy
 runRecipe {
    get 'https://staging-server/customers/1'
    transfer '$.customerId' to '$.customerId' of request
    post 'https://staging-server/orders', '{ "customerId": "id", "sku": "ABC-123", "quantity": "1"}'
 }
 ```
 
This test recipe will send a GET request to ```https://staging-server/customers/1```, then use a [JSONPath](http://goessner.net/articles/JsonPath/)
expression to find a value inside the response and insert it into a piece of JSON. Then it will send a POST with the modified JSON to the 
endpoint ```https://staging-server/orders```.

However, as it stands this is not really a test, because we're not checking what we're getting back from the server. Let's try
something slightly more interesting, introducing the concepts of *assertions* and *test results*:

```groovy
 def testResult = executeRecipe {
    post 'https://staging-server/orders', '{ "customerId": "JonSnow", "sku": "ABC-123", "quantity": "1"}'
    wait 2 seconds
    get 'https://staging-server/orders?filter=JonSnow', {
        asserting {
            status 200
            responseContains 'PROCESSED'
        }
    }
 }
 assert testResult.currentReport.status != FAILED
 ```
 
 This piece of code will do the following:
 1. Send a POST with order data to the endpoint ```https://staging-server/orders```.
 2. Wait for 2 seconds after receiving the response (probably because some background processing task needs to complete)
 3. Send a GET to the URL ```https://staging-server/orders?filter=JonSnow``` and verify
    + that the request returned 200 OK
    + that the response contains the text PROCESSED
 4. After running the recipe, either locally or on a TestServer, use a standard Groovy assert to check that the requests completed, 
 and that the HTTP status and response met our expectations. 
 
Although we hope that these two examples got you interested in the Assert4J DSL, we've really only
scratched the surface of what you can do with it. In addition to REST requests, content transfer and a wide range of assertions,
the DSL can e.g. send SOAP requests with a minimum of code, execute database queries using JDBC, read test data from
Excel and CSV files and generate random test data values (such as ZIP codes).

For detailed descriptions of the DSL features, click on the links below, or check out the [samples](../samples) submodule for more examples.

* [REST requests](Rest-Request.md)
* [Assertions and test results](Assertions.md)
* [Content transfer (Property transfer)](Property-Transfer.md)
* [SOAP requests](Soap-Request.md) 
* [Database queries](Jdbc-Request.md)
* [Datasources](Datasources.md)

## How to use the DSL in your build

To compile and run tests using the Assert4J DSL in a Maven build, simply add this dependency (assuming that you are using
version 1.0.0; if not just replace the contents of the ```version``` element):

```xml
<dependency>
    <groupId>io.swagger.assert</groupId>
    <artifactId>swagger-assert4j-groovy-dsl</artifactId>
    <version>1.0.0</version>
    <scope>test</scope>
</dependency>
 ```

Generally ```test``` is the scope you want to us. If you don't the Assert4J DSL and its dependencies may be shipped with
your production code.

To do the same thing in Gradle, add this line to the ```dependencies``` block:

```groovy
runtime group: 'org.groovy', name: 'groovy', version: '2.2.0', ext: 'jar'
```

## Tell us what's missing!

If you've found a bug or are missing some kind of vocabulary, functionality etc, please contribute or 
open an issue!
