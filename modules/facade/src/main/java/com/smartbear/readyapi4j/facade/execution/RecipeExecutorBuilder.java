package com.smartbear.readyapi4j.facade.execution;

import com.smartbear.readyapi4j.execution.ExecutionListener;
import com.smartbear.readyapi4j.execution.RecipeExecutor;
import com.smartbear.readyapi4j.execution.RecipeFilter;
import com.smartbear.readyapi4j.local.execution.SoapUIRecipeExecutor;
import com.smartbear.readyapi4j.support.ExecutionLogger;
import com.smartbear.readyapi4j.support.RecipeLogger;
import com.smartbear.readyapi4j.testengine.execution.TestEngineClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Builder class for building a configured RecipeExecutor. The following env/system properties will be used if available:
 * - testengine.endpoint - endpoint to a TestEngine installation
 * - testengine.user - used for TestEngine authentication
 * - testengine.password - for TestEngine authentication
 * - readyapi4j.log.executions.folder - all executions will be logged to this folder
 * - readyapi4j.log.recipes.folder - all recipes will be logged to this folder
 */
public class RecipeExecutorBuilder {

    private final static Logger LOG = LoggerFactory.getLogger(RecipeExecutorBuilder.class);

    private static final String TESTENGINE_ENDPOINT_PROPERTY = "testengine.endpoint";
    private static final String TESTENGINE_USER_PROPERTY = "testengine.user";
    private static final String TESTENGINE_PASSWORD_PROPERTY = "testengine.password";
    private static final String EXECUTION_LOG_FOLDER_PROPERTY = "readyapi4j.log.executions.folder";
    private static final String RECIPE_LOG_FOLDER_PROPERTY = "readyapi4j.log.recipes.folder";

    private String testEngineUser;
    private String testEnginePassword;
    private String testEngineEndpoint;

    private List<RecipeFilter> filters = new ArrayList<>();
    private List<ExecutionListener> listeners = new ArrayList<>();

    /**
     * @param filter RecipeFilter to add to the resulting executor
     */
    public RecipeExecutorBuilder withRecipeFilter(RecipeFilter filter) {
        filters.add(filter);
        return this;
    }

    /**
     * @param listener ExecutionListener to add to the resulting executor
     */
    public RecipeExecutorBuilder withExecutionListener(ExecutionListener listener) {
        listeners.add(listener);
        return this;
    }

    /**
     * Builds a RecipeExecutor as configured; if an endpoint has been set this builder will
     * first attempt to build a remote executor using that endpoint. Second it will check for
     * a testengine.endpoint env or system property to use for a remote executor - if neither is found
     * a local executor will be built instead. If you want to force a local executor use the
     * buildLocal() method instead.
     *
     * @return the resulting RecipeExecutor
     */
    public RecipeExecutor build() {
        if (testEngineEndpoint != null) {
            try {
                return buildRemote(testEngineEndpoint);
            } catch (Exception e) {
                LOG.error("Failed to build remote RecipeExecutor for", e);
            }
        }

        Map<String, String> env = System.getenv();
        String endpoint = env.getOrDefault(TESTENGINE_ENDPOINT_PROPERTY, System.getProperty(TESTENGINE_ENDPOINT_PROPERTY));
        if (endpoint != null) {
            try {
                return buildRemote(endpoint);
            } catch (Exception e) {
                LOG.error("Failed to build remote RecipeExecutor", e);
            }
        }

        return buildLocal();
    }

    private RecipeExecutor addFilters(RecipeExecutor executor) {
        for (RecipeFilter filter : filters) {
            executor.addRecipeFilter(filter);
        }
        for (ExecutionListener listener : listeners) {
            executor.addExecutionListener(listener);
        }

        Map<String, String> env = System.getenv();
        String recipeLogFolder = env.getOrDefault(RECIPE_LOG_FOLDER_PROPERTY, System.getProperty(RECIPE_LOG_FOLDER_PROPERTY));
        if (recipeLogFolder != null) {
            executor.addRecipeFilter(new RecipeLogger(recipeLogFolder));
        }

        String executionLogFolder = env.getOrDefault(EXECUTION_LOG_FOLDER_PROPERTY, System.getProperty(EXECUTION_LOG_FOLDER_PROPERTY));
        if (executionLogFolder != null) {
            executor.addExecutionListener(new ExecutionLogger(executionLogFolder));
        }

        return executor;
    }

    /**
     * @param testServerEndpoint the remote TestEngine endpoint to use when building an executor
     */
    public RecipeExecutorBuilder withEndpoint(String testServerEndpoint) {
        this.testEngineEndpoint = testServerEndpoint;
        return this;
    }

    /**
     * @param testServerUser the remote TestEngine user to use for authentication
     */
    public RecipeExecutorBuilder withUser(String testServerUser) {
        this.testEngineUser = testServerUser;
        return this;
    }

    /**
     * @param testServerPassword the remote TestEngine password to user
     */
    public RecipeExecutorBuilder withPassword(String testServerPassword) {
        this.testEnginePassword = testServerPassword;
        return this;
    }

    /**
     * @param recipeLogFolder folder to log recipes to before execution
     */
    public RecipeExecutorBuilder withRecipeLog(String recipeLogFolder) {
        return withRecipeFilter(new RecipeLogger(recipeLogFolder));
    }

    /**
     * @param executionLogFolder folder to log executions to after execution
     */
    public RecipeExecutorBuilder withExecutionLog(String executionLogFolder) {
        return withExecutionListener(new ExecutionLogger(executionLogFolder));
    }

    /**
     * @return a local RecipeExecutor - ignores any TestEngine related configurations
     */
    public RecipeExecutor buildLocal() {
        RecipeExecutor executor = new SoapUIRecipeExecutor();
        return addFilters(executor);
    }

    /**
     * Builds a remote executor with the configured username and password, if not set those will
     * be taken from testengine.user and testengine.password env/system properties respectively.
     *
     * @param endpoint the remote endpoint to use
     * @return a remote RecipeExecutor
     * @throws MalformedURLException if the specified endpoint is not a valid URL
     */
    public RecipeExecutor buildRemote(String endpoint) throws MalformedURLException {
        Map<String, String> env = System.getenv();

        URL url = new URL(endpoint);
        TestEngineClient testServerClient = TestEngineClient.fromUrl(url.toString());

        String user = testEngineUser != null ? testEngineUser :
                env.getOrDefault(TESTENGINE_USER_PROPERTY, System.getProperty(TESTENGINE_USER_PROPERTY));

        String password = testEnginePassword != null ? testEnginePassword :
                env.getOrDefault(TESTENGINE_PASSWORD_PROPERTY, System.getProperty(TESTENGINE_PASSWORD_PROPERTY));

        testServerClient.setCredentials(user, password);
        RecipeExecutor executor = testServerClient.createRecipeExecutor();
        return addFilters(executor);
    }

    /**
     * @return a default executor, remote if corresponding env/system properties are set correctly
     * - local otherwise
     */
    public static RecipeExecutor buildDefault() {
        return new RecipeExecutorBuilder().build();
    }
}
