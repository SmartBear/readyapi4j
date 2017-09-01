## Swagger Assert4j Modules

Overview of included modules and how they fit together:

| Module | Description |
|---|---|
| [core](modules/core)  | Core Recipe creation and execution classes and interfaces |
| [cucumber](modules/cucumber)  | Extensible Cucumber vocabulary for REST API testing, including commandline and Docker execution |
| [facade](modules/facade) | Execution facade for dynamically switching between local end remote execution of recipes  |
| [groovy-dsl](modules/groovy-dsl) | Grooy DSL for creating JSON Recipes  |
| [junit-report](modules/junit-report) | Utility library for creating JUnit-style XML reports  |
| [local](modules/local) | Execution engine for executing JSON recipes locally  |
| [maven-plugin](modules/maven-plugin) | Maven plugin for executing JSON recipes either locally or remotely  |
| [maven-plugin-tester](modules/maven-plugin-tester) | Maven plugin tester  |
| [oas](modules/oas) | OAS/Swagger-specific extensions to core recipe creation  |
| [samples](modules/samples) | Java and Groovy examples for how to use swagger-assert4j  |
| [testserver](samples/testserver) | Execution engine for executing JSON recipes and SoapUI projects remotely on TestServer  |