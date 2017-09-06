# Assertions

Until you've added assertions to your DSL script, you're not really testing your application. You're just checking that 
it doesn't blow up in your face when you access it.

## Generally useful assertions

### responseContains

Checks whether the body of the HTTP response contains a string or something matching a regular expression.

**Arguments:** A string to look for, and optionally parameters that indicate whether the string should be 
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

**Arguments:** A string to look for, and optionally key-value pairs that indicate whether the string should be 
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

## HTTP-specific assertions

These assertions can be added to both REST and SOAP requests but obviously don't make any sense for database responses.

### status

Checks the HTTP status of the response.

**Argument:** One status, or a a comma-separated list of statuses.

**Example:** 
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

**Argument:** A comma-separated list of unacceptable statuses.

**Examples:** 
```groovy
 get 'https://staging-server/customers/1', {
     asserting {
         statusNotIn 400, 401, 500
     }
 }
 ```