# Swagger Assert4j OAS/Swagger support

This module extends the [core Java API](../core) with builders/classes for creating tests that test an API based on a 
Swagger/OAS 2.0 definition. 

## Usage

The [SwaggerTestStepBuilder](https://smartbear.github.io/swagger-assert4j/apidocs/index.html?io/swagger/assert4j/swagger/SwaggerTestStepBuilder.html)
provides convenience methods for adding [REST Requests](../core/README.md#rest-requests) to a TestRecipe based on 
metadata provided by a Swagger definition. 

For example:

```java
SwaggerTestStepBuilder petstore = new SwaggerTestStepBuilder( "http://petstore.swagger.io/v2/swagger.json" );

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
these will extract the corresponding path and method for the specified operation from the Swagger definition.