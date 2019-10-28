package com.smartbear.readyapi4j.cucumber.samples;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty", "html:target/cucumber"},
        features = {"src/test/resources/cucumber"},
        glue = {"com.smartbear.readyapi4j.cucumber", "com.smartbear.cucumber.samples.extension"})
public class CucumberTest {
}