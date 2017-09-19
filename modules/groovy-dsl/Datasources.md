# Datasources

Arguably, Datasources are the most powerful feature in the DSL. 
Datasources allow you to easily make your test data-driven and thus much more relevant and to generate test data if you don't have
it. Read on to learn the basic concepts and syntax of DataSources.

***NOTE:*** Datasources are only available for TestServer executions, not for local executions. In other words you need a ReadyApi TestServer 
to be able to use them. The most straightforward way to do this is to use the method ```executeRecipeOnServer``` -
see the code samples below.

## How Datasources work
 
The idea of a Datasource is that it will loop over a number of data records. In every iteration, it will set one property 
per column in the datasource, and will pass these property to a number of test steps.

To insert the variables in your test you will then use SoapUI [property expansion](https://www.soapui.org/scripting---properties/property-expansion.html).

For instance, assume that you have an Excel file called ```cities.xls``` with the following contents:

|Country|City|
|-------|-----|
|France | Paris |
|Sweden | Stockholm |
|Italy | Rome |


We're now going to use this data to execute the same test (two test steps) once for every row in the Excel sheet:

```groovy
 import static io.swagger.assert4j.dsl.execution.RecipeExecution.executeRecipe
 
  executeRecipeOnServer 'https://testserver_host:8080/', 'user', 'pwd', {
    /* Loop over all the rows in the Excel file. */
    usingExcelFile 'cities.xls', {
    
        get 'https://staging-server/cities_by_country', {
            parameters { 
                // set the query parameter to the value in the Country column
                query 'country', '${ExcelDataSource#Country}'
            }
            asserting {
               /* assert that the response contains the value in the City column */
               responseContains '${ExcelDataSource#City}'
            }
        }
        get 'https://staging-server/city_search', {
            parameters { 
                /* set the query parameter to the value in the Country column */
                query 'country', '${ExcelDataSource#Country}'
            }
            asserting {
                statusNotIn 404
            }
        }
        
        /* end loop */
     }
  }
```

The somewhat cryptic strings ```${ExcelFile#Country}``` and ```${ExcelFile#City}``` are **property expansions**, which
retrieve the current values of the columns ```Country``` and ```City``` for every row.

In addition to Excel data sources, the following data sources can be created from the Assert4J Test DSL:
* Grid data sources
* CSV data sources

See below for descriptions of these different data sources.

### usingData

The ```usingData``` data loop allows you to simply create a data source programmatically, using a Groovy map object. The names 
of the data columns will be the keys in the map. Each column name will be mapped to a list with all the names. For instance,
the data that we saw in the Excel example above:

|Country|City|
|-------|-----|
|France | Paris |
|Sweden | Stockholm |
|Italy | Rome |

... will be represented by the following Groovy map:

```groovy
[Country: ['France', 'Sweden', 'Italy'], City: ['Paris', 'Stockholm', 'Rome']]
```

```groovy
 import static io.swagger.assert4j.dsl.execution.RecipeExecution.executeRecipe
 
  def data = [Country: ['France', 'Sweden', 'Italy'], City: ['Paris', 'Stockholm', 'Rome']]
  executeRecipeOnServer 'https://testserver_host:8080/', 'user', 'pwd', {
    /* Loop over all the rows in the data. */
    usingData data, {
    
        get 'https://staging-server/cities_by_country', {
            parameters { 
                // set the query parameter to the value in the Country column
                query 'country', '${GridDataSource#Country}'
            }
            asserting {
               /* assert that the response contains the value in the City column */
               responseContains '${GridDataSource#City}'
            }
        }
        get 'https://staging-server/city_search', {
            parameters { 
                /* set the query parameter to the value in the Country column */
                query 'country', '${GridDataSource#Country}'
            }
            asserting {
                statusNotIn 404
            }
        }
        
        /* end loop */
     }
  }
```

### usingCsvFile

The ```usingCsvFile``` data loop allows you to use a CSV file as the data source. For instance, let's assume that the CSV 
```countries.csv``` has the following contents:

```
Country,City
France,Paris
Sweden,Stockholm
Italy,Rome
```
The following code will then execute the same tests as the Excel example above:

```groovy
 import static io.swagger.assert4j.dsl.execution.RecipeExecution.executeRecipe
 
  executeRecipeOnServer 'https://testserver_host:8080/', 'user', 'pwd', {
    /* Loop over all the rows in the Excel file. */
    usingCsvFile 'cities.xls', {
    
        get 'https://staging-server/cities_by_country', {
            parameters { 
                // set the query parameter to the value in the Country column
                query 'country', '${CsvDataSource#Country}'
            }
            asserting {
               /* assert that the response contains the value in the City column */
               responseContains '${CsvDataSource#City}'
            }
        }
        get 'https://staging-server/city_search', {
            parameters { 
                /* set the query parameter to the value in the Country column */
                query 'country', '${CsvDataSource#Country}'
            }
            asserting {
                statusNotIn 404
            }
        }
        
        /* end loop */
     }
  }
```


