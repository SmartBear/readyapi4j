## The command-line runner for running Cucumber API Tests  

This module builds a single jar that contains everything needed to run Cucumber API tests from the command-line.

### Docker image

The docker maven profile builds a Docker image containing this jar available at 
https://hub.docker.com/r/smartbear/readyapi4j-cucumber. Build it with 

```
mvn clean install -Pdocker
```

and use it as described in the [main README](../../README.md)


