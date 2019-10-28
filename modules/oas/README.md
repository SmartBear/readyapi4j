# ReadyAPI4j OAS/Swagger support

This module extends the [core Java API](../core) with builders/classes for creating tests that test an API based on a 
Swagger/OAS definitions - both 2.0 and 3.0 are supported. 

## Usage

The [OASTestStepBuilder](https://smartbear.github.io/readyapi4j/apidocs/index.html?com/smartbear/readyapi4j/oas/OASTestStepBuilder.html)
provides convenience methods for adding [REST Requests](../core/README.md#rest-requests) to a TestRecipe based on 
metadata provided by a OAS definition. 

For example:

```java
OASTestStepBuilder petstore = new OASTestStepBuilder( "http://petstore.swagger.io/v2/swagger.json" );

TestRecipe recipe = TestRecipeBuilder.buildRecipe(  
    petstore.operationWithBody("addPet")
        .withRequestBody(...)
        .withAssertions(
            validStatusCodes( 201 )
        ),
    petstore.operation("getPetById")
        .withPathParameter( "petId", "...")
        .withAssertions(
            validStatusCodes( 200 )
        ),

):
```

As you can see one must use the `operationWithBody` method for POST/PUT methods, otherwise the `operation` is used. Either of
these will extract the corresponding path and method for the specified operation from the OAS definition.