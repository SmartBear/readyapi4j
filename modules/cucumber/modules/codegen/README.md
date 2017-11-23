# Swagger Assert4j Cucumber Feature Codegen 

Generates default Cucumber feature files from a OpenAPI Spec (OAS) definition using [Swagger CodeGen](https://github.com/swagger-api/swagger-codegen).
The generated features use the precreated (Step definitions)[https://github.com/SmartBear/swagger-assert4j/blob/master/modules/cucumber/README.md#api-testing-vocabulary-reference]


The generated files are in many cases incomplete as many OAS definitions do not contain default 
parameter values, for example the following is generated for the findPetsByStatus operation in the 
(petstore definition)[http://petstore.swagger.io/v2/swagger.json]

```gherkin
Feature: This is a sample server Petstore server.  You can find out more about Swagger at [http://swagger.io](http://swagger.io) or on [irc.freenode.net, #swagger](http://swagger.io/irc/).  For this sample, you can use the api key &#x60;special-key&#x60; to test the authorization filters. - Version 1.0.0

Background:
  Given the OAS definition at http://petstore.swagger.io/v2/swagger.json

  Scenario: 
    When a request to findPetsByStatus is made
    And status is 
    Then a 200 response is returned

  Scenario: 
    When a request to findPetsByStatus is made
    And status is 
    Then a 400 response is returned
```

Since the `status` parameter lacks a default value in the definition - the above And statement is incomplete and needs to 
be filled out.

## Usage

Clone this repo and build it with 

```
mvn clean package
```

Then you're all set to use it with 

```
java -jar target/swagger-assert4j-cucumber-codegen-1.0.0.jar generate -l Assert4jCucumberFeatureGenerator 
    -i <url/path to Swagger definition> 
```

which will generate one feature file for each operation in the specified Swagger Definition. By default
files will be generated into the {build.dir}/generated-test-resources/features folder.

## Docker image

You can run this CodeGen without having to install anything (except Docker) by running the 
smartbear/oas2cucumber image on DockerHub. By default the contained Codegen will write the output
 to an /output folder, which you can override by providing your own mount. 
 
For example:

```
docker run -v <output folder>:/output smartbear/oas2cucumber http://petstore.swagger.io/v2/swagger.json
```

Will generate feature files for each of the operations in the PetStore Swagger definition to the
folder specified by &lt;output folder&gt; above.

Any additional arguments added to the run command will be passed on the standard Swagger Codegen CLI.

