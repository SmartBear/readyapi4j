package io.swagger.assert4j.cucumber;

import com.google.common.collect.Lists;

import java.util.ArrayList;

/**
 * Custom Cucumber Runner that adds the built in StepDefs to the invocation of
 * the cucumber.api.cli.Main class
 */

public class CucumberRunner {

    /**
     * Invokes the Cucumber CLI with the internal StepDefs package
     *
     * @param args command line arguments
     * @throws Throwable
     */

    public static void main(String [] args) throws Throwable {
        System.out.println( "Swagger Assert4j Cucumber Runner" );

        ArrayList<String> argsList = Lists.newArrayList(args);
        argsList.add(0, RestStepDefs.class.getPackage().getName());
        argsList.add(0, "-g");

        cucumber.api.cli.Main.main( argsList.toArray( new String[argsList.size()]) );
    }
}
