# Database requests in Assert4J DSL

The Assert4J DSL can access relational databases, and also some NoSQL databases. using JDBC - the standard Java API
for database access. To add a database request to a test recipe, you call the method ```jdbcRequest``` and specify
parameters in a configuration block enclosed by curly braces – { and }.

 
Here's an example where you connect to a MySQL database on the server staging-server and execute the query 
```select * from customers```:
 
 ```groovy
 import static io.swagger.assert4j.dsl.execution.RecipeExecution.executeRecipe
 
  executeRecipe {
     jdbcRequest {
            driver 'com.mysql.Driver'
            connectionString 'jdbc:mysql://staging-server/staging'
            query 'select * from customers'
     }
     
  }
  ```

For this to work, you need to specify which JDBC driver to use and also make sure that the driver in question is on 
the classpath where the recipe is executed. For more information about JDBC, see Oracle's 
[JDBC tutorial](https://docs.oracle.com/javase/tutorial/jdbc/index.html).

Because of the way in which JDBC handles stored procedures, you need to set an additional parameter if you are 
executing a procedure:

```groovy
 import static io.swagger.assert4j.dsl.execution.RecipeExecution.executeRecipe
 
  executeRecipe {
     jdbcRequest {
            driver 'com.mysql.Driver'
            connectionString 'jdbc:mysql://staging-server/staging'
            query "register_customer('JonSnow')"
            storedProcedure true
     }
     
  }
  ```
Note that because the query itself contains single quotes, in this case we quote the query using double quotes. In the Test DSL,
and in Groovy generally, it often doesn't matter if you use single or double quotes. If you want to learn more about 
Groovy string literals, see [this page](http://docs.groovy-lang.org/latest/html/documentation/index.html#all-strings).

**Technical note**: A configuration block is a Groovy closure, which will be executed before the request is sent. 
In other words, you can insert Groovy code inside it. You can also add comments to document what you're doing.
 
## Adding assertions to the JDBC response

To veryify that the database request didn't just complete but also returned what you expected, you need to add assertions
to your JDBC request step. To detect that an error was returned from the database, you need to add the 
[jdbcStatusOk assertion](#jdbcStatusOk) The standard assertions [general-purpose assertions](Assertions.md#standard). 

The following complete code example shows you how to verify that you got a result containing the text "JonSnow" back within
3 seconds:

```groovy
 import static io.swagger.assert4j.dsl.execution.RecipeExecution.executeRecipe
 
  executeRecipe {
     jdbcRequest {
         driver 'com.mysql.Driver'
         connectionString 'jdbc:mysql://staging-server/staging'
         query 'select * from customers'
         asserting {
            jdbcStatusOk
            responseContains 'JonSnow'
            maxResponseTime 3 seconds
         }
     }
  }
  ```
The XPath assertions are surprisingly useful for verifying the results you get back. This is because the results are
wrapped in XML. For instance, if you're selecting one row with the columns CustomerId and Age from a table, you will get XML like the 
following back:

```xml
<Results fetchSize="0">
   <ResultSet>
        <Row>
            <CustomerId>JonSnow</CustomerId>
            <Age>25</Age>
        </Row>
    </ResultSet>
</Results>
```
The following code will assert that the returned CustomerId value is "JonSnow":

```groovy
 import static io.swagger.assert4j.dsl.execution.RecipeExecution.executeRecipe
 
  executeRecipe {
     jdbcRequest {
         driver 'com.mysql.Driver'
         connectionString 'jdbc:mysql://staging-server/staging'
         query "select CustomerId, Age from Customers where SSN = 'AC-34983-AA'"
         asserting {
            xpath '//Row/CustomerId' contains 'JonSnow'
         }
     }
  }
  ```
  
Below you find descriptions of the two JDBC specific assertions.

### <a name="jdbcStatusOk></a>jdbcStatusOk

Asserts that no error was returned from the database. Fails if an error was returned.

**Arguments:** None

**Example:** 
```groovy
 import static io.swagger.assert4j.dsl.execution.RecipeExecution.executeRecipe
  
   executeRecipe {
      jdbcRequest {
          driver 'com.mysql.Driver'
          connectionString 'jdbc:mysql://staging-server/staging'
          query "select CustomerId, Age from Customers where SSN = 'AC-34983-AA'"
          asserting {
             jdbcStatusOk
          }
      }
   }
 ```

### timeout

Asserts that the JDBC request completes before the timeout expires. Fails if the request takes longer than the 
specified timeout.

**Arguments:** An integer value specifying the timout in milliseconds.

**Example:** 
```groovy
 import static io.swagger.assert4j.dsl.execution.RecipeExecution.executeRecipe
  
   executeRecipe {
      jdbcRequest {
          driver 'com.mysql.Driver'
          connectionString 'jdbc:mysql://staging-server/staging'
          query "select CustomerId, Age from Customers where SSN = 'AC-34983-AA'"
          asserting {
             timeout 3000
          }
      }
   }
 ```


