package io.swagger.assert4j.cucumber.samples;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty", "html:target/cucumber"},
        features = {"src/test/resources/cucumber"},
        glue = {"io.swagger.assert4j.cucumber", "com.smartbear.cucumber.samples.extension"})
public class CucumberTest {
}