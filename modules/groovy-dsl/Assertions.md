# Assertions

Until you've added assertions to your DSL script, you're not really testing your application. You're just checking that 
it doesn't blow up in your face when you access it.

If you do add assertions, steps in your test will be marked as FAILED if the assertions fail. If not, they will have status
ERROR if the request itself fails (e.g. because of network connectivity issues), but if a response is received the 
status will be set to UNKNOWN. If no steps have the status FAILED or ERROR, the test will automatically be considered
successful.

Assertions are always added in an ```asserting``` section right after the request step. See the assertion descriptions below 
for examples.

## Standard assertions

This group of assertions can be applied to anything that returns a response: REST and SOAP requests as well as JDBC (database) 
requests.

### responseContains

Checks whether the body of the HTTP response contains a string or something matching a regular expression.

**Parameters:** A string to look for, and optionally parameters that indicate whether the string should be 
used as a regular expression (```useRegexp```)and whether the search should ignore case (```ignoreCase```).


**Examples:** 
```groovy
 get 'https://staging-server/customers/1', {
     asserting {
         /* matches Smith, SMITH etc */
         responseContains 'smith', ignoreCase: true
     }
 }
 get 'https://staging-server/customers/1', {
      asserting {
          /* matches Smith, SMITH etc, but also any of those strings with an s appended to it */
          responseContains 'smith(s)?', ignoreCase: true, useRegexp: true
      }
  }
 ```
The first assertion will accept Smith, SMITH etc. The second one will use "smith(s)" as a regular expression, so it will
also accept Smiths and SMITHS, for instance.

### responseDoesNotContain

This is simply the inverse of ```responseContains``` (see above). It will assert that the response content does **not**
contain a string or match a regular expression.

**Parameters:** A string to look for, and optionally key-value pairs that indicate whether the string should be 
used as a regular expression (```useRegexp```)and whether the search should ignore case (```ignoreCase```).


**Examples:** 
```groovy
 get 'https://staging-server/customers/1', {
     asserting {
         /* matches content not containing the text "smith" */
         responseDoesNotContain 'smith', ignoreCase: false
     }
 }
 get 'https://staging-server/customers/1', {
      asserting {
          /* will fail for all content that contains either "smith" or "smiths" */
          responseContains 'smith(s)?', ignoreCase: false, useRegexp: true
      }
  }
 ```
The first assertion will accept Smith, SMITH etc. The second one will use "smith(s)" as a regular expression and will thus
also accept e.g. Smiths and SMITHS.

### maxResponseTime

Verifies that the response time is lower than the specified max time.

**Parameter:** A time expression in the format ```<number> [millisecond[s])|ms|second[s])|minute[s])]```. If the time unit is omitted,
milliseconds will be assumed.
   Here are some valid time expressions:
   + ```1 second```
   + ```1.5 minutes```
   + ```50 milliseconds```
   + ```50 ms```
   + ```50```
Note that the last two expressions are equivalent.

**Examples:** 
```groovy
 get 'https://staging-server/customers/1', {
     asserting {
         maxResponseTime 2 seconds
     }
 }
 post 'https://staging-server/customers/1', {
      asserting {
          maxResponseTime 500 milliseconds
      }
  }
 ```
 
### script
 
Uses a Groovy script to perform an assertion using the current test state. This is an advanced assertion that 
assumes that the user is familiar with the SoapUI object model. For information about how Groovy script assertions
work, see [this page](https://www.soapui.org/functional-testing/validating-messages/using-script-assertions.html).

Usually this script will be in the form ```assert <BOOLEAN_EXPRESSION>``` - if the boolean expression is false,
the assertion will fail.
 
**Parameter:** A valid SoapUI assertion Groovy script. 

**Examples:** 
 ```groovy
  get 'https://staging-server/customers/1', {
      asserting {
          script 'assert messageExchange.responseAttachments.length == 2'
      }
  }
  ```
  
## XPath/XQuery assertions



XPath and XQuery assertions have identical syntax - the only difference between them is the former start with
```xpath``` and the latter with ```xQuery```.

### xpath/xQuery <EXPRESSION> contains <EXPECTED_CONTENT>

Extracts the element in the response matching the XPath in ```EXPRESSION``` and fails the assertion 
if it isn't equal to ```EXPECTED_CONTENT```.

When validating response content, the XPath and XQuery are much more useful than you'd think.
HTML responses, JSON responses and JDBC (databases) responses can all be coerced to XML, which means you can
use XPath or XQuery to make advanced assertions on the content.

**Parameters:** A string containing the XPath/XQuery expression and another string with the expected content.

**Examples:** 
```groovy
 get 'https://staging-server/customers/1', {
     asserting {
         xpath '/customer/id' contains 'JonSnow'
     }
 }
 get 'https://staging-server/products/1', {
      asserting {
          xQuery '/customers[id == "JonSnow"]/id' contains 'JonSnow'
      }
  }
 ```
 
### xQuery <EXPRESSION> contains <EXPECTED_CONTENT>

Extracts the element in the response matching the XPath in ```EXPRESSION``` and fails the assertion 
if it isn't equal to ```EXPECTED_CONTENT```.

When validating response content, the XPath and XQuery are much more useful than you'd think.
HTML responses, JSON responses and JDBC (databases) responses can all be coerced to XML, which means you can
use XPath or XQuery to make advanced assertions on the content.

**Parameters:** A string containing the XPath/XQuery expression and another string with the expected content.

**Examples:** 
```groovy
 get 'https://staging-server/customers/1', {
     asserting {
         xpath '/customer/id' contains 'JonSnow'
     }
 }
 get 'https://staging-server/products/1', {
      asserting {
          xQuery '/customers[id == "JonSnow"]/id' contains 'JonSnow'
      }
  }
 ```

## HTTP-specific assertions

These assertions can be added to both REST and SOAP requests but don't make any sense for test steps that are not HTTP-based.

### status

Checks the HTTP status of the response.

**Parameter:** One status, or a a comma-separated list of statuses.

**Examples:** 
```groovy
 get 'https://staging-server/customers/1', {
     asserting {
         status 200
     }
 }
 get 'https://staging-server/orders/1/status', {
     asserting {
         status 200, 201
     }
 }
     
 ```

### statusNotIn

Checks that the HTTP status of the response is not in a list of unacceptable statuses.

**Parameter:** A comma-separated list of unacceptable statuses, or just one single status.

**Example:** 
```groovy
 get 'https://staging-server/customers/1', {
     asserting {
         statusNotIn 400, 401, 500
     }
 }
 ```
 
### responseContentType
 
Verifies that the content type of the HTTP response matches the expected value.
 
**Parameter:** A comma-separated list of unacceptable statuses.
 
**Examples:** 
 ```groovy
  get 'https://staging-server/customers/1', {
      asserting {
          responseContentType 'text/xml'
      }
  }
  ```
 
 