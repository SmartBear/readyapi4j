## The command-line runner for running Cucumber API Tests stored in HipTest

This module extends the [Command-line Runner](../runner/README.md) with the possibility to download Scenarios from HipTest 
for execution.

### Using with HipTest

Provide a hiptest.properties file with the following properties:
```
hiptest.token=<your hiptest token>
hiptest.uid=<your hiptest user id>
hiptest.clientid=<your hiptest clientid>
hiptest.project=<the id of the project to generate from>
hiptest.folder=<the id of the project folder to generate from>
hiptest.targetFolder=<an optional target folder for where to save the retrieved scenario>
``` 

You can find the project and folder ids by looking at the url of the corresponding HipTest folder, for example 

```
https://app.hiptest.com/projects/141233/test-plan/folders/1040071
```

would result in project 141233 and folder 1040071.

The tokens and clientid can get generated from your [HipTest profile page](https://app.hiptest.com/profile)

The underlying API call made to the [HipTest API](https://api-doc.hiptest.com/) is described at 
https://api-doc.hiptest.com/?http#get-feature-from-a-given-folder.

Run the command-line runner without specifing a feature file but with regular Cucumber CLI arguments

```
java -jar target/swagger-assert4j-cucumber-hiptest-runner-1.0.0-SNAPSHOT.jar -p pretty
[main] INFO com.smartbear.readyapi4j.cucumber.hiptest.HiptestCucumberRunner - Reading Features from HipTest
[main] INFO com.smartbear.readyapi4j.cucumber.hiptest.HiptestCucumberRunner - Cucumber feature(s) saved to temporary file target/scenarios/cucumber2105632627517087917.feature
ReadyAPI4j Cucumber Runner
...
```




