package com.smartbear.readyapi4j.facade.execution;

import com.smartbear.readyapi4j.ExecutionListener;
import com.smartbear.readyapi4j.execution.Execution;
import com.smartbear.readyapi4j.execution.RecipeExecutor;
import com.smartbear.readyapi4j.execution.RecipeFilter;
import com.smartbear.readyapi4j.local.execution.SoapUIRecipeExecutor;
import com.smartbear.readyapi4j.support.ExecutionLogger;
import com.smartbear.readyapi4j.support.RecipeLogger;
import com.smartbear.readyapi4j.testserver.execution.TestServerClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Builder class for building a configured RecipeExecutor.
 */
public class RecipeExecutorBuilder {

    private final static Logger LOG = LoggerFactory.getLogger(RecipeExecutorBuilder.class);

    private static final String TESTSERVER_ENDPOINT_PROPERTY = "testserver.endpoint";
    private static final String TESTSERVER_USER_PROPERTY = "testserver.user";
    private static final String TESTSERVER_PASSWORD_PROPERTY = "testserver.password";

    private String testServerUser;
    private String testServerPassword;
    private String testServerEndpoint;

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
    public RecipeExecutorBuilder withExecutionListener(ExecutionListener listener ){
        listeners.add(listener);
        return this;
    }

    /**
     * Builds a RecipeExecutor as configured; if an endpoint has been set this builder will
     * first attempt to build a remote executor using that endpoint. Second it will check for
     * a testserver.endpoint env or system property to use for a remote executor - if neither is found
     * a local executor will be built instead. If you want to force a local executor use the
     * buildLocal() method instead.
     *
     * @return the resulting RecipeExecutor
     */
    public RecipeExecutor build() {
        if (testServerEndpoint != null) {
            try {
                return buildRemote(testServerEndpoint);
            } catch (Exception e) {
                LOG.error("Failed to build remote RecipeExecutor for",e);
            }
        }

        Map<String, String> env = System.getenv();
        String endpoint = env.getOrDefault(TESTSERVER_ENDPOINT_PROPERTY, System.getProperty(TESTSERVER_ENDPOINT_PROPERTY));
        if (endpoint != null) {
            try {
                return buildRemote(endpoint);
            } catch (Exception e) {
                LOG.error("Failed to build remote RecipeExecutor",e);
            }
        }

        return buildLocal();
    }

    private RecipeExecutor addFilters(RecipeExecutor executor) {
        for (RecipeFilter filter : filters) {
            executor.addRecipeFilter(filter);
        }
        for( ExecutionListener listener : listeners){
            executor.addExecutionListener(listener);
        }
        return executor;
    }

    /**
     * @param testServerEndpoint the remote TestServer endpoint to use when building an executor
     */
    public RecipeExecutorBuilder withEndpoint(String testServerEndpoint) {
        this.testServerEndpoint = testServerEndpoint;
        return this;
    }

    /**
     * @param testServerUser the remote TestServer user to use for authentication
     */
    public RecipeExecutorBuilder withUser(String testServerUser) {
        this.testServerUser = testServerUser;
        return this;
    }

    /**
     * @param testServerPassword the remote TestServer password to user
     */
    public RecipeExecutorBuilder withPassword(String testServerPassword) {
        this.testServerPassword = testServerPassword;
        return this;
    }

    /**
     * @param recipeLogFolder folder to log recipes to before execution
     */
    public RecipeExecutorBuilder withRecipeLog( String recipeLogFolder ){
        return withRecipeFilter( new RecipeLogger( recipeLogFolder ));
    }

    /**
     * @param executionLogFolder folder to log executions to after execution
     */
    public RecipeExecutorBuilder withExecutionLog( String executionLogFolder ){
        return withExecutionListener( new ExecutionLogger( executionLogFolder ));
    }

    /**
     * @return a local RecipeExecutor - ignores any TestServer related configurations
     */
    public RecipeExecutor buildLocal() {
        RecipeExecutor executor = new SoapUIRecipeExecutor();
        return addFilters(executor);
    }

    /**
     * Builds a remote executor with the configured username and password, if not set those will
     * be taken from testserver.user and testserver.password env/system properties respectively.
     *
     * @param endpoint the remote endpoint to use
     * @return a remote RecipeExecutor
     * @throws MalformedURLException if the specified endpoint is not a valid URL
     */
    public RecipeExecutor buildRemote(String endpoint) throws MalformedURLException {
        Map<String, String> env = System.getenv();

        URL url = new URL(endpoint);
        TestServerClient testServerClient = TestServerClient.fromUrl(url.toString());

        String user = testServerUser != null ? testServerUser :
            env.getOrDefault(TESTSERVER_USER_PROPERTY, System.getProperty(TESTSERVER_USER_PROPERTY));

        String password = testServerPassword != null ? testServerPassword :
            env.getOrDefault(TESTSERVER_PASSWORD_PROPERTY, System.getProperty(TESTSERVER_PASSWORD_PROPERTY));

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
