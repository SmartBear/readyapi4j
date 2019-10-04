package io.swagger.assert4j.cucumber;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Set;

/**
 * Custom Cucumber Runner that adds the built in StepDefs to the invocation of
 * the cucumber.api.cli.Main class
 */

public class CucumberRunner {

    private static final Logger LOG = LoggerFactory.getLogger(CucumberRunner.class);

    /**
     * Invokes the Cucumber CLI with the internal StepDefs package and properties defined in a cucumber.properties file
     *
     * @param args command line arguments
     * @throws Throwable
     */

    public static void main(String[] args) throws Throwable {
        System.out.println("Swagger Assert4j Cucumber Runner");

        ArrayList<String> argsList = Lists.newArrayList(args);
        argsList.add(0, RestStepDefs.class.getPackage().getName());
        argsList.add(0, "-g");

        if (new File("cucumber.properties").exists()) {
            Properties properties = new Properties();
            properties.load(new FileReader("cucumber.properties"));

            extractCucumberProperties(argsList, properties);
        }

        io.cucumber.core.cli.Main.main(argsList.toArray(new String[argsList.size()]));
    }

    private static void extractCucumberProperties(ArrayList<String> argsList, Properties properties) {
        Set<String> keys = properties.stringPropertyNames();
        for (String key : keys) {
            if (key.startsWith("cucumber.")) {
                String param = key.substring("cucumber.".length());
                if (!param.isEmpty()) {
                    argsList.add(0, properties.getProperty(key));
                    if (param.length() == 1) {
                        argsList.add(0, "-" + param);
                    } else {
                        argsList.add(0, "--" + param);
                    }
                }
            }
        }
    }
}
