# SOAP requests in ReadyAPI4j DSL

To send a SOAP request from a DSL script, you need to call the ```soapRequest``` method and
provide a URL to the WSDL. You also need to identify the binding and operation you want to call.

Here's a simple example where no parameters are passed into the SOAP request:

```groovy
import static com.smartbear.readyapi4j.dsl.execution.RecipeExecution.executeRecipe
 
 executeRecipe {
    soapRequest {
        wsdl = 'http://www.webservicex.com/globalweather.asmx?WSDL'
        binding = 'GlobalWeatherSoap12'
        operation = 'GetCitiesByCountry'
    }
 }
 ```
 
## Setting parameters
 
Most SOAP requests have parameters – variable data. Parameters can be set by name or by path, using the commands
```namedParam``` and ```pathParam```, respectively.

Named parameter finds an XML element using the local name of the element. 
The following code passes the value "France" into the XML element:

```groovy
 import static com.smartbear.readyapi4j.dsl.execution.RecipeExecution.executeRecipe
  
  executeRecipe {
     soapRequest {
         wsdl = 'http://www.webservicex.com/globalweather.asmx?WSDL'
         binding = 'GlobalWeatherSoap12'
         operation = 'GetCitiesByCountry'
         namedParam 'CountryName', 'France'
     }
  }
```
In some cases the local name will be ambiguous, however - the corresponding XML will have several elements with the same
name. Using a ```pathParam``` with XPath will help resolve this:
```groovy
 import static com.smartbear.readyapi4j.dsl.execution.RecipeExecution.executeRecipe
  
  executeRecipe {
     soapRequest {
         wsdl = 'http://www.webservicex.com/globalweather.asmx?WSDL'
         binding = 'GlobalWeatherSoap12'
         operation = 'GetCitiesByCountry'
         pathParam '//GetCitiesByCountry/CountryName', 'France'
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
 import static com.smartbear.readyapi4j.dsl.execution.RecipeExecution.executeRecipe
 
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
 import static com.smartbear.readyapi4j.dsl.execution.RecipeExecution.executeRecipe
 
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


