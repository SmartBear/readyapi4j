# REST requests in Assert4J DSL

To send a REST request from a DSL script, you need to call one of the methods in the class 
```io.swagger.assert4j.dsl.TestDsl```. For every HTTP verb, there's a corresponding method with a lower case
name that you can call to send a request to a certain URI. In the case of POST and PUT, you can also
add the body you want to send to the server as the second parameter.

 ```groovy
 import static io.swagger.assert4j.dsl.execution.RecipeExecution.executeRecipe
 
 executeRecipe {
    get 'https://staging-server/customers/1?includeSSN=true'
    post 'https://staging-server/orders', '{ "customerId": "id", "sku": "ABC-123", "quantity": "1"}'
    put 'https://staging-server/orders/1', '{ "customerId": "id", "sku": "ABC-123", "quantity": "2"}'
    delete 'https://staging-server/customers/1'
    head 'https://staging-server/orders'
    options 'https://staging-server/orders'
    trace 'https://staging-server/orders'
 }
 ```
 
To modify the HTTP requests in different ways, you pass in a ***configuration block*** as the second
parameter to one of the HTTP methods inside a set of curly braces. Here is an example where you set the body and the 
content type of a post and add a Set-Cookie header to the request:
 
 ```groovy
 import static io.swagger.assert4j.dsl.execution.RecipeExecution.executeRecipe
 
  executeRecipe {
     post 'https://staging-server/orders/1/status', {
         contentType 'text/plain'
         header 'Set-Cookie', 'user=admin'
         body 'COMPLETED'
     }
     
  }
  ```
 
In the sections below we will describe all the things you can tweak in the configuration block of a request.
 
**Technical note**: The configuration block is a Groovy closure, which will be executed before the request is sent. 
In other words, you can insert Groovy code inside it. You can also add comments to document what you're doing.
 
 ## Setting parameters and request headers
 
Swagger Assert4J supports four different kinds of REST parameters, which can all be set inside a configuration block:
* query parameters, set in the query string
* path parameters, set in the resource path
* matrix parameters, appended after the resource path proper
* header parameters, set as HTTP request headers

Here's an example of how to set these four types in a configuration block:

```groovy
 get 'https://staging-server/customers/{id}', {
     parameters {
         path 'id', '123'
         matrix 'a', '1'
         query 'b', '2'
         headerParam 'c', '3'
     }
 }
```

Query, path and matrix parameters can all be manually sent as part of the URI, if you think this is clearer, and instead of
using a headerParam entry, you can simply set the header. For instance,
the following request URI contains an "implicit" path parameter with the value 123, the matrix parameter ```a``` 
and the query parameter ```b```, and a header named ```c```:
```groovy
 get 'https://staging-server/customers/123;a=1?b=2', {
     header 'c', '3'
 }
```
Note that you don't need to use the parameters section to set an HTTP request header. If you want to set several request 
headers in one go, you can use a ```headers``` entry and pass a Groovy map:

```groovy
 get 'https://staging-server/customers/123;a=1?b=2', {
     headers ['Set-Cookie': 'user=admin123', 'Max-Age': 3600]
 }
```

This is equivalent to the following:

```groovy
 get 'https://staging-server/customers/123;a=1?b=2', {
     header 'Set-Cookie', 'user=admin123'
     header 'Max-Age', 3600
 }
```

## Posting (and putting) content

If the request is a POST or PUT, you will naturally need to configure what you are posting. This is done with the entries
```contentType``` and ```body```. Here's an example:

```groovy
 put 'https://staging-server/customers/123;a=1?b=2', {
     contentType 'application/json'
     body '{ "customerId": "id", "sku": "ABC-123", "quantity": "1"}'
 }
```

As seen above, if you want to POST or PUT application/json, you can also use the short form of the put and post methods,
where you pass the body as the second parameter:
```groovy
    post 'https://staging-server/orders', '{ "customerId": "id", "sku": "ABC-123", "quantity": "1"}'
    put 'https://staging-server/orders/1', '{ "customerId": "id", "sku": "ABC-123", "quantity": "2"}'
```

Another common scenario is that you want to POST the query string. In this case using the ```body``` entry, so we recommend
that you configure your request like this:
```groovy
    post 'https://staging-server/orders?customerId=123&sku=ABC-123', {
        postQueryString true
    }
```

## Following redirects

By default the DSL will treat an HTTP redirect as just another response and not request. By setting the followRedirects.
To tell a DSL script to go and fetch the page you've been redirected to, use the ```followRedirects``` option, like this:

```groovy
    post 'https://staging-server/old_page', {
        followRedirects true
    }
```

## Adding assertions to the response

The Assert4J DSL is a **Test** DSL, you're really not putting it to good use unless you verify the responses you
get back. This is done in an ```asserting``` section in your configuration block.

In addition to the general-purpose [assertions](Assertions.md) for things like simple content and the response
time, there are more specific assertions for checking HTTP statuses and headers and using JsonPath expressions to validate
parts of the response (XPath is part of the generic assertion support).

The following complete code example shows you both generic assertions and ones that are only applicable to REST and HTTP.

```groovy
 import static io.swagger.assert4j.dsl.execution.RecipeExecution.executeRecipe
 
  executeRecipe {
     post 'https://staging-server/orders/1/status', {
         asserting {
            /* Standard assertions */
            responseContains 'COMPLETED'
            responseDoesNotContain 'CANCELLED'
            maxResponseTime 3500 // measured in milliseconds
            
            /* REST/HTTP assertions */
            status 200
            jsonExists '$.orderStatus'
         }
     }
     
  }
  ```

In addition to the [standard assertions](Assertions.md#standard) and the [HTTP-specific ones](Assertions.md#http-specific), 
the following JsonPath assertions are available for REST requests. They all use JsonPath to verify the content
in the response body.

### jsonPath <EXPRESSION> contains <EXPECTED_CONTENT>

Extracts the element in the response matching the JsonPath in ```EXPRESSION``` and fails the assertion 
if it isn't equal to ```EXPECTED_CONTENT```.

**Parameters:** A string containing the JsonPath expression and another string with the expected content.

**Example:** 
```groovy
 get 'https://staging-server/customers/1', {
     asserting {
         jsonPath '$.customerId' contains 'JonSnow'
     }
 }
 ```
 
### jsonPathExists
 
 Tries to find the element in the response matching the JsonPath in the parameter and fails the assertion if 
 it doesn't exist.
 
 **Parameter:** A string containing the JsonPath expression identifying the element.
 
 **Example:** 
 ```groovy
  get 'https://staging-server/customers/1', {
      asserting {
          jsonPathExists '$.customerId'
      }
  }
  ```
 
### jsonPath <EXPRESSION> occurs <NUMBER> times

Counts the number of elements in the response matching the JsonPath in ```EXPRESSION``` and fails the assertion 
if the number of elements is not equal to ```NUMBER```.

**Arguments:** A string containing the JsonPath expression and an integer with the expected element count.

**Example:** 
```groovy
 get 'https://staging-server/customers/1/orders', {
     asserting {
         jsonPath '$.order' occurs 3 times
     }
 }
 ```


