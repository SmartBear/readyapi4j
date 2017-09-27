# Swagger Assert4j JUnitReport

Supporting module containing classes that makes it easy to generate JUnit style XML reports that can be 
parsed/visualized by Jenkins/etc. The actual XML-schema is defined in [report.xml](src/main/resources/report.xsd) which
is then used for Pojo generation using the [XMLBeans](https://xmlbeans.apache.org/) Maven plugin.

These classes are used by the [Assert4j Maven Plugin](../maven-plugin) for generating corresponding reports after 
test execution.

  