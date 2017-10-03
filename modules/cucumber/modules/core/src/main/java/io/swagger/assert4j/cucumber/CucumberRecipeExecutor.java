package io.swagger.assert4j.cucumber;

import io.swagger.assert4j.client.model.TestCase;
import io.swagger.assert4j.ExecutionListener;
import io.swagger.assert4j.TestRecipe;
import io.swagger.assert4j.execution.Execution;
import io.swagger.assert4j.execution.RecipeExecutor;
import io.swagger.assert4j.facade.execution.RecipeExecutorBuilder;
import cucumber.api.Scenario;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;

/**
 * Executes a Recipe class using the configured execution engine (local execution is default)
 */

public class CucumberRecipeExecutor {
    private static final Logger LOG = LoggerFactory.getLogger(CucumberRecipeExecutor.class);

    public static final String RECIPE_LOG_FOLDER = "swagger-assert4j.cucumber.logfolder";
    public static final String SILENT_EXECUTION = "swagger-assert4j.cucumber.silent";

    private RecipeExecutor executor;
    private boolean async = false;

    public CucumberRecipeExecutor() {
        executor = RecipeExecutorBuilder.buildDefault();
    }

    /**
     * Executes the specified TestCase and returns the Execution. If a scenario
     * is specified and the readyapi.cucumber.logfolder system property is set,
     * the generated recipe will be written to the specified folder.
     *
     * It is possible to temporarily "bypass" recipe execution by specifying
     * a readyapi.cucumber.silent property - in which case testcases will not be
     * submitted to the server, but still logged to the above folder.
     *
     * @param testCase the TestCase to execute
     * @param scenario the Cucumber scenario used to generate the specified Recipe
     * @return the Execution for the executed TestCase, null if execution was bypassed
     */

    public Execution runTestCase(TestCase testCase, Scenario scenario) {
        TestRecipe testRecipe = new TestRecipe(testCase);

        if (LOG.isDebugEnabled()) {
            LOG.debug(testRecipe.toString());
        }

        String logFolder = System.getProperty(RECIPE_LOG_FOLDER, System.getenv(RECIPE_LOG_FOLDER) );
        if( scenario != null && logFolder != null ){
            logScenarioToFile(testRecipe, scenario, logFolder);
        }

        if( System.getProperty(SILENT_EXECUTION, System.getenv(SILENT_EXECUTION) ) != null ){
            return null;
        }

        return async ? executor.submitRecipe( testRecipe ) : executor.executeRecipe(testRecipe);
    }

    /**
     * Writes the specified testRecipe to a folder/file name deducted from the
     * specified scenario
     *
     * @param testRecipe the test recipe to log
     * @param scenario the associated Cucumber scenario
     * @param logFolder the root folder for generated folders and files
     */

    protected void logScenarioToFile(TestRecipe testRecipe, Scenario scenario, String logFolder) {
        try {
            File folder = new File( logFolder );
            if( !folder.exists() || !folder.isDirectory()){
                if( !folder.mkdirs()){
                    LOG.warn( "Failed to created logFolder [" + logFolder + "]" );
                    return;
                }
            }

            String[] pathSegments = scenario.getId().split(";");
            File scenarioFolder = folder;
            int fileIndex = 0;

            if( pathSegments.length > 1 ) {
                scenarioFolder = new File(folder, pathSegments[0]);
                if (scenarioFolder.exists() || !scenarioFolder.isDirectory()) {
                    if( !scenarioFolder.mkdirs()){
                        LOG.warn( "Failed to created scenarioFolder [" + scenarioFolder + "]" );
                        return;
                    }
                }

                fileIndex = 1;
            }

            StringBuilder filenameBuilder = new StringBuilder( pathSegments[fileIndex] );
            for( int c = fileIndex+1; c < pathSegments.length; c++ ){
                String segment = pathSegments[c].trim();
                if( !StringUtils.isBlank( segment )){
                    filenameBuilder.append( '_' ).append( segment );
                }
            }

            filenameBuilder.append( ".json" );
            File scenarioFile = new File( scenarioFolder, filenameBuilder.toString() );

            try( FileWriter writer = new FileWriter( scenarioFile )) {
                LOG.info("Writing recipe to " + folder.getName() + File.separatorChar + scenarioFolder.getName() +
                    File.separatorChar + scenarioFile.getName());

                writer.write(testRecipe.toString());
            }
        } catch (Exception e) {
            LOG.error("Failed to write recipe to logFolder [" + logFolder + "]", e );
        }
    }

    /**
     * Adds a listener for test execution events
     *
     * @param listener the listener to add
     */

    public void addExecutionListener(ExecutionListener listener) {
        executor.addExecutionListener(listener);
    }

    /**
     * Removes a previously added listener for test execution events
     *
     * @param listener the listener to remove
     */

    public void removeExecutionListener(ExecutionListener listener) {
        executor.removeExecutionListener(listener);
    }

    /**
     * Get the underlying RecipeExecutor used to execute the generated recipes.
     *
     * @return the underlying RecipeExecutor
     */

    public RecipeExecutor getExecutor() {
        return executor;
    }

    /**
     * Tells is execution of recipes will be async
     *
     * @return execution mode
     */

    public boolean isAsync() {
        return async;
    }

    /**
     * Sets if recipe execution will be async
     *
     * @param async execution mode
     */
    public void setAsync(boolean async) {
        this.async = async;
    }
}
