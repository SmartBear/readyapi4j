package com.smartbear.ready.jenkins;

import hudson.AbortException;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;
import io.swagger.assert4j.TestRecipe;
import io.swagger.assert4j.TestRecipeBuilder;
import io.swagger.assert4j.client.model.ProjectResultReport;
import io.swagger.assert4j.execution.Execution;
import io.swagger.assert4j.execution.RecipeExecutor;
import io.swagger.assert4j.testserver.execution.ProjectExecutionRequest;
import io.swagger.assert4j.testserver.execution.ProjectExecutor;
import io.swagger.assert4j.testserver.execution.RepositoryProjectExecutionRequest;
import io.swagger.assert4j.testserver.execution.TestServerClient;
import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@SuppressWarnings("unused")
public class RemoteTestStarter extends Builder {

    enum TestType {
        LOCAL("Local project (XML/composite)"),
        LOCAL_RECIPE("Test Recipe"),
        REPOSITORY("Repository Project");

        private String displayName;

        TestType(String displayName) {
            this.displayName = displayName;
        }

    }

    private final String pathToProjectFile;
    private final String testCaseName;
    private final String testSuiteName;
    private final TestType testType;
    private final String serverUrl;
    private final String userName;
    private final String password;
    private final String repositoryName;
    private final String environment;
    private final String hostAndPort;
    private final String tags;
    private final String projectFilePassword;
    private final String projectProperties;

    // Fields in config.jelly must match the parameter names in the "DataBoundConstructor"
    @DataBoundConstructor
    public RemoteTestStarter(String pathToProjectFile,
                             String testType,
                             String serverUrl,
                             String userName,
                             String password,
                             String testCaseName,
                             String testSuiteName,
                             String repositoryName, String environment,
                             String hostAndPort, String tags,
                             String projectFilePassword,
                             String projectProperties) {
        this.pathToProjectFile = pathToProjectFile;
        this.testType = testTypeForString(testType);
        this.serverUrl = serverUrl;
        this.userName = userName;
        this.password = password;
        this.testCaseName = testCaseName;
        this.testSuiteName = testSuiteName;
        this.repositoryName = repositoryName;
        this.environment = environment;
        this.hostAndPort = hostAndPort;
        this.tags = tags;
        this.projectFilePassword = projectFilePassword;
        this.projectProperties = projectProperties;
    }

    private TestType testTypeForString(String typeString) {
        return Arrays.stream(TestType.values()).filter(type -> type.name().equals(typeString))
                .findFirst()
                .orElse(TestType.LOCAL);
    }

    @Override
    public boolean perform(AbstractBuild build, Launcher launcher, BuildListener listener) throws AbortException {
        try {
            TestServerClient client = TestServerClient.fromUrl(serverUrl).withCredentials(userName, password);
            Execution execution = runTests(build, client);

            if (execution.getCurrentStatus() == ProjectResultReport.StatusEnum.PENDING) {
                throw new AbortException("The project " + pathToProjectFile + " has unsatisfied dependencies");
            }
        } catch (IOException e) {
            throw new AbortException("Couldn't start tests from project " + pathToProjectFile + " on server " + serverUrl);
        }
        return true;
    }

    private Execution runTests(AbstractBuild build, TestServerClient client) throws AbortException {
        switch (testType) {
            case LOCAL_RECIPE:
                return executeRecipe(build, client);
            case REPOSITORY:
                return executeRepositoryProject(build, client);
            default:
                return executeXmlProject(build, client);
        }
    }

    private Execution executeRecipe(AbstractBuild build, TestServerClient client) throws AbortException {
        RecipeExecutor recipeExecutor = client.createRecipeExecutor();
        File recipeFile = getProjectFile(build.getWorkspace());
        if (recipeFile == null || !recipeFile.exists()) {
            throw new AbortException("Recipe file " + pathToProjectFile + " does not exist.");
        }
        TestRecipe recipeToExecute;
        try {
            recipeToExecute = TestRecipeBuilder.createFrom(FileUtils.readFileToString(recipeFile));
        } catch (IOException e) {
            throw new AbortException("Could not read or parse recipe file " + pathToProjectFile);
        }
        return recipeExecutor.executeRecipe(recipeToExecute);
    }

    private Execution executeXmlProject(AbstractBuild build, TestServerClient client) {
        ProjectExecutor executor = client.createProjectExecutor();
        File projectFile = getProjectFile(build.getWorkspace());
        Map<String, String> projectProperties = getProjectProperties();
        Set<String> tags = isBlank(this.tags) ? Collections.emptySet() : new HashSet<>(Arrays.asList(this.tags.split(",")));
        ProjectExecutionRequest.Builder executionRequest = ProjectExecutionRequest.Builder.forProjectFile(projectFile)
                .forTestSuite(testSuiteName)
                .forTestCase(testCaseName)
                .forEnvironment(environment)
                .forTags(tags)
                .withProjectPassword(projectFilePassword)
                .withEndpoint(hostAndPort);
        if (!projectProperties.isEmpty()) {
            executionRequest.withCustomProperties(null, projectProperties);
        }
        return executor.executeProject(executionRequest.build());
    }

    private Execution executeRepositoryProject(AbstractBuild build, TestServerClient client) {
        ProjectExecutor executor = client.createProjectExecutor();
        Map<String, String> projectProperties = getProjectProperties();
        Set<String> tags = isBlank(this.tags) ? Collections.emptySet() : new HashSet<>(Arrays.asList(this.tags.split(",")));
        RepositoryProjectExecutionRequest.Builder executionRequest = RepositoryProjectExecutionRequest.Builder.forProject(pathToProjectFile)
                .fromRepository(repositoryName)
                .forTestSuite(testSuiteName)
                .forTestCase(testCaseName)
                .forEnvironment(environment)
                .forTags(tags)
                .withProjectPassword(projectFilePassword)
                .withEndpoint(hostAndPort);
        if (!projectProperties.isEmpty()) {
            executionRequest.withCustomProperties(null, projectProperties);
        }
        return executor.executeRepositoryProject(executionRequest.build());
    }

    public String getPathToProjectFile() {
        return pathToProjectFile;
    }

    public String getTestCaseName() {
        return testCaseName;
    }

    public String getTestSuiteName() {
        return testSuiteName;
    }

    public String getServerUrl() {
        return isNotBlank(serverUrl) ? serverUrl : getDescriptor().getServerUrl();
    }

    public String getUserName() {
        return isNotBlank(userName) ? userName : getDescriptor().getUserName();
    }

    public String getPassword() {
        return isNotBlank(password) ? password : getDescriptor().getPassword();
    }

    public String getEnvironment() {
        return environment;
    }

    public String getHostAndPort() {
        return hostAndPort;
    }

    public String getTags() {
        return tags;
    }

    public String getProjectFilePassword() {
        return projectFilePassword;
    }

    public String getTestType() {
        return testType.toString();
    }

    private Map<String, String> getProjectProperties() {
        if (isBlank(projectProperties)) {
            return Collections.emptyMap();
        } else {
            Map<String, String> returnValue = new HashMap<>();
            Properties properties = new Properties();
            try {
                properties.load(new StringReader(projectProperties));
                for (Object key : properties.keySet()) {
                    String keyString = String.valueOf(key);
                    returnValue.put(keyString, properties.getProperty(keyString));
                }
            } catch (IOException ignore) {

            }
            return returnValue;
        }
    }

    private boolean seemsToBeJson(String projectProperties) {
        String trimmed = projectProperties.trim();
        return trimmed.startsWith("{") && trimmed.endsWith("}");
    }

    private File getProjectFile(FilePath workspace) {
        String absolutePath = getAbsolutePath(pathToProjectFile, workspace);
        if (absolutePath == null) {
            try {
                URL projectFileUrl = new URL(pathToProjectFile);
                File projectFile = File.createTempFile("project", ".xml");
                projectFile.deleteOnExit();
                FileUtils.copyURLToFile(projectFileUrl, projectFile);
                return projectFile;
            } catch (IOException e) {
                return null;
            }
        }
        return new File(absolutePath);
    }

    private String getAbsolutePath(String path, FilePath workspace) {
        if (StringUtils.isBlank(path)) {
            return null;
        }
        File file = new File(path);
        if (file.exists()) {
            return file.getAbsolutePath();
        }
        try {
            file = new File(new File(workspace.toURI()), path);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (file.exists()) {
            return file.getAbsolutePath();
        }
        return null;
    }

    // Overridden for better type safety.
    // If your plugin doesn't really define any property on Descriptor,
    // you don't have to do this.
    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl) super.getDescriptor();
    }

    /**
     * Descriptor for {@link RemoteTestStarter}. Used as a singleton.
     * The class is marked as public so that it can be accessed from views.
     */
    @SuppressWarnings("unused") // fields are accessed using reflection when saving
    @Extension // This indicates to Jenkins that this is an implementation of an extension point.
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {

        private String serverUrl;
        private String userName;
        private String password;

        /**
         * In order to load the persisted global configuration, you have to
         * call load() in the constructor.
         */
        public DescriptorImpl() {
            load();
        }

        /**
         * Performs on-the-fly validation of the form field.
         *
         * @param value This parameter receives the value that the user has typed.
         * @return Indicates the outcome of the validation. This is sent to the browser.
         * <p></p>
         * Note that returning {@link FormValidation#error(String)} does not
         * prevent the form from being saved. It just means that a message
         * will be displayed to the user.
         */
        public FormValidation doCheckServerUrl(@QueryParameter String value)
                throws IOException, ServletException {
            if (isNotBlank(value)) {
                try {
                    new URL(value);
                } catch (MalformedURLException e) {
                    return FormValidation.error("Invalid URL");
                }
                if (!value.matches("^https?://.+")) {
                    return FormValidation.error("Protocol needs to be http or https");
                }
            }
            return FormValidation.ok();
        }

        public FormValidation doTestServerConnection(@QueryParameter String serverUrl, @QueryParameter String userName,
                                                     @QueryParameter String password)
                throws IOException, ServletException {
            try {
                TestServerClient client = TestServerClient.fromUrl(serverUrl).withCredentials(userName, password);
                List<Execution> executions = client.getExecutions();
                return FormValidation.ok("Connection successful");
            } catch (Exception e) {
                return FormValidation.error("Connection to server failed with error: " + e.getMessage());
            }
        }

        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            // Indicates that this builder can be used with all kinds of project types
            return true;
        }

        /**
         * This human readable name is used in the configuration screen.
         */
        public String getDisplayName() {
            return "Run remote ReadyApi test";
        }

        @Override
        public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
            // To persist global configuration information,
            // set that to properties and call save().
            serverUrl = formData.getString("serverUrl");
            userName = formData.getString("userName");
            password = formData.getString("password");

            // ^Can also use req.bindJSON(this, formData);
            //  (easier when there are many fields; need set* methods for this, like setUseFrench)

            save();
            return super.configure(req, formData);
        }

        public String getServerUrl() {
            return serverUrl;
        }

        public String getUserName() {
            return userName;
        }

        public String getPassword() {
            return password;
        }

        public ListBoxModel doFillTestTypeItems() {
            ListBoxModel items = new ListBoxModel();
            for (TestType type : TestType.values()) {
                items.add(type.displayName, type.name());
            }
            return items;
        }

    }

}
