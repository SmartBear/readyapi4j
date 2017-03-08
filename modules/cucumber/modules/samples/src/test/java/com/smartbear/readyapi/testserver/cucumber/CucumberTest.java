package com.smartbear.readyapi.testserver.cucumber;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    plugin = {"pretty", "html:target/cucumber"},
    features = {"src/test/resources/cucumber"},
    glue = {"com.smartbear.samples.cucumber.extension", "com.smartbear.readyapi.testserver.cucumber" })
public class CucumberTest {
}