# Swagger Assert4j Cucumber Feature Codegen 

Generates default Cucumber feature files from a Swagger definition using [Swagger CodeGen](https://github.com/swagger-api/swagger-codegen).

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
smartbear/swagger2cucumber image on DockerHub. By default the contained Codegen will write the output
 to an /output folder, which you can override by providing your own mount. 
 
For example:

```
docker run -v <output folder>:/output smartbear/swagger2cucumber http://petstore.swagger.io/v2/swagger.json
```

Will generate feature files for each of the operations in the PetStore Swagger definition to the
folder specified by &lt;output folder&gt; above.

Any additional arguments added to the run command will be passed on the standard Swagger Codegen CLI.

