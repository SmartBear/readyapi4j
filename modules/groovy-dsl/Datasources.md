# Datasources

Datasources is likely the most complex feature in the Assert4J Test DSL, and unless you've already used DataSources in 
[ReadyApi](https://smartbear.com/product/ready-api/overview/) or SoapUI Pro you may need some time to understand how to use them. 

Arguably, it's also the most powerful feature in the DSL, however.

***NOTE:*** Datasources are only available for TestServer executions, not for local executions. In other words you need a ReadyApi TestServer 
to be able to use them.

## How Datasources work
 
The idea of a Datasource is that it will loop over a number of data records. In every iteration, it will set one variable 
per column in the datasource, and will pass these variables to a number of test steps.

To insert the variables in your test you will then use SoapUI [property expansion](https://www.soapui.org/scripting---properties/property-expansion.html).

For instance, assume that you have an Excel file called ```cities.xls``` with the following contents:

|Country|City|
|-------|-----|
|France | Paris |
|Sweden | Stockholm |
|Italy | Rome |



```groovy
 import static io.swagger.assert4j.dsl.execution.RecipeExecution.executeRecipe
 
  executeRecipe {
    soapRequest {
           wsdl = 'http://www.webservicex.com/globalweather.asmx?WSDL'
           binding = 'GlobalWeatherSoap12'
           operation = 'GetCitiesByCountry'
           namedParam 'CountryName', 'France'
           asserting {
               responseContains 'Paris'
               responseDoesNotContain 'London'
               maxResponseTime 3500 ms
           }
       }
     
  }
```

## Adding assertions to the SOAP response

To verify that you are getting the expected response back from your web service, you should add assertions to the SOAP
 request step. This is done in an ```asserting``` section in your configuration block.

In addition to the general-purpose [assertions](Assertions.md#standard-assertions) for things like simple content and the response
time, there are more specific assertions for checking the correctness of the web service response. The XPath assertions 
are particularly useful for SOAP responses since they will always be in XML format.

The [HTTP-specific assertions](Assertions.md#http-specific-assertions) are also applicable to all SOAP requests – 

The following complete code example shows you three generic content and response time assertions:

```groovy
 import static io.swagger.assert4j.dsl.execution.RecipeExecution.executeRecipe
 
  executeRecipe {
    soapRequest {
           wsdl = 'http://www.webservicex.com/globalweather.asmx?WSDL'
           binding = 'GlobalWeatherSoap12'
           operation = 'GetCitiesByCountry'
           namedParam 'CountryName', 'France'
           asserting {
               responseContains 'Paris'
               responseDoesNotContain 'London'
               maxResponseTime 3500 ms
           }
       }
     
  }
```

The following code example demonstrates the use of the HTTP status assertion, and the two SOAP-specific assertions ```notSoapFault``` and
```schemaCompliance```. The former will fail if a SOAP fault is returned by the web service; the latter if the response
payload doesn't comply with the schema defined in the WSDL of the web service.

```groovy
 import static io.swagger.assert4j.dsl.execution.RecipeExecution.executeRecipe
 
  executeRecipe {
    soapRequest {
           wsdl = 'http://www.webservicex.com/globalweather.asmx?WSDL'
           binding = 'GlobalWeatherSoap12'
           operation = 'GetCitiesByCountry'
           namedParam 'CountryName', 'France'
           asserting {
               status 200
               notSoapFault
               schemaCompliance
           }
       }
     
  }
```


