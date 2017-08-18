package com.smartbear.ready.jenkins;

import hudson.AbortException;
import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.model.Result;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;
import io.swagger.assert4j.TestRecipe;
import io.swagger.assert4j.TestRecipeBuilder;
import io.swagger.assert4j.client.model.ProjectResultReport;
import io.swagger.assert4j.client.model.UnresolvedFile;
import io.swagger.assert4j.execution.Execution;
import io.swagger.assert4j.junitreport.JUnitReport;
import io.swagger.assert4j.testserver.execution.ProjectExecutionRequest;
import io.swagger.assert4j.testserver.execution.ProjectExecutor;
import io.swagger.assert4j.testserver.execution.RepositoryProjectExecutionRequest;
import io.swagger.assert4j.testserver.execution.TestServerClient;
import io.swagger.assert4j.testserver.execution.TestServerExecution;
import io.swagger.assert4j.testserver.execution.TestServerRecipeExecutor;
import io.swagger.client.ApiException;
import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@SuppressWarnings("unused")
public class RemoteTestStarter extends Builder {

    private Logger log = LoggerFactory.getLogger(RemoteTestStarter.class);

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
    private final String extraFiles;
    private final String reportFile;
    private File workspace;

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
                             String projectProperties, String extraFiles, String reportFile) {
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
        this.extraFiles = extraFiles;
        this.reportFile = reportFile;
    }

    private TestType testTypeForString(String typeString) {
        return Arrays.stream(TestType.values()).filter(type -> type.name().equals(typeString))
                .findFirst()
                .orElse(TestType.LOCAL);
    }

    @Override
    public boolean perform(AbstractBuild build, Launcher launcher, BuildListener listener) throws AbortException {
        try {
            workspace = new File(build.getWorkspace().toURI());
        } catch (Exception e) {
            throw new AbortException("Could not read path to workspace");
        }
        try {
            List<TestServerExecution> execution = testType == TestType.REPOSITORY ? runRepositoryTests() :
                    runTestFiles(build);
            log.info("Finished TestServer execution - {} projects executed", execution.size());
        } catch (IOException e) {
            log.error("TestServer API call failed", e);
            throw new AbortException("Could not start tests from project " + pathToProjectFile + " on server " + serverUrl);
        } catch (ApiException e) {
            log.error("TestServer API call failed when adding files", e);
            throw new AbortException("Failed to attached dependent files to the project " + pathToProjectFile);
        } catch (Exception e) {
            String message = "Unexpected error when running " + pathToProjectFile;
            log.error(message, e);
            throw new AbortException(message + ": " + e.getMessage());
        }
        return true;
    }

    private List<TestServerExecution> runRepositoryTests() throws IOException, ApiException {
        TestServerClient client = makeTestServerClient();
        TestServerExecution repositoryExecution = executeRepositoryProject(client);
        String repositoryProjectPath = repositoryName + "/" + pathToProjectFile;
        repositoryExecution = addFilesIfExecutionIsPending(client, repositoryProjectPath, repositoryExecution);
        return Collections.singletonList(repositoryExecution);
    }

    private Collection<File> getFilesToRun(File targetFile) throws AbortException {
        if (targetFile.isDirectory()) {
            String extension = testType == TestType.LOCAL_RECIPE ? ".json" : ".xml";
            File[] filesInDirectory = targetFile.listFiles((dir, name) -> name.endsWith(extension));
            if (filesInDirectory == null) {
                throw new AbortException("No project files or recipes found in path " + pathToProjectFile);
            } else {
                return Arrays.asList(filesInDirectory);
            }
        } else {
            return Collections.singletonList(targetFile);
        }
    }

    private List<File> findAllFiles(Set<String> extraFileNames) {
        List<File> returnValue = new ArrayList<>();
        for (String fileName : extraFileNames) {
            Optional<File> extraFile = findFile(fileName);
            if (extraFile.isPresent()) {
                File file = extraFile.get();
                if (file.isDirectory()) {
                    File[] filesInDirectory = file.listFiles();
                    if (filesInDirectory != null) {
                        Set<String> filePaths = Arrays.stream(filesInDirectory).map(File::getAbsolutePath).collect(toSet());
                        returnValue.addAll(findAllFiles(filePaths));
                    }
                } else if (file.exists()) {
                    returnValue.add(file);
                }
            } else {
                log.info("Specified extra file not found: {}", fileName);
            }
        }
        return returnValue;
    }

    private Set<String> buildStringSet(String commaSeparatedStrings) {
        return isBlank(commaSeparatedStrings) ? Collections.emptySet() :
                new HashSet<>(Arrays.asList(commaSeparatedStrings.split(",")));
    }

    private List<TestServerExecution> runTestFiles(AbstractBuild build) throws AbortException, ApiException, MalformedURLException {
        JUnitReport report = isNotBlank(reportFile) ? new JUnitReport(getProjectProperties()) : null;
        TestServerClient client = makeTestServerClient();
        boolean wildcard = pathToProjectFile.matches(".+(\\\\|/)\\*");
        String realPath = wildcard ? pathToProjectFile.substring(0, pathToProjectFile.length() - 2) :
                pathToProjectFile;
        Optional<File> targetFile = findFile(realPath);
        if (!targetFile.isPresent()) {
            throw new AbortException("Cannot load test files. File not found: " + realPath);
        }
        Collection<File> projectFiles = wildcard ? getFilesToRun(targetFile.get()) :
                Collections.singletonList(new File(realPath));
        List<TestServerExecution> executions = new ArrayList<>();
        for (File projectFile : projectFiles) {
            TestServerExecution execution;
            if (testType == TestType.LOCAL_RECIPE) {
                if (projectFile.isDirectory()) {
                    throw new AbortException("Cannot run directory " + realPath + " as a recipe");
                }
                execution = executeRecipe(client, projectFile);
            } else {
                execution = executeXmlProject(client, projectFile);
            }
            execution = addFilesIfExecutionIsPending(client, realPath, execution);
            if (report != null) {
                report.handleResponse(execution.getCurrentReport(), projectFile.getName());
            }
            log.info("Project result report received from TestServer: {}", execution.getCurrentReport());
            if (execution.getCurrentReport().getStatus() == ProjectResultReport.StatusEnum.FAILED) {
                build.setResult(Result.FAILURE);
            }
        }
        if (report != null) {
            report.finishReport();
            File targetReportFile = new File(workspace, reportFile);
            try {
                if (targetReportFile.getParentFile().isDirectory() || targetReportFile.mkdirs()) {
                    report.save(targetReportFile);
                }
            } catch (IOException e) {
                System.err.println("Could not write report to report file " + targetFile);
            }
        }
        return executions;
    }

    private TestServerExecution addFilesIfExecutionIsPending(TestServerClient client, String realPath, TestServerExecution execution) throws ApiException, AbortException {
        if (execution.getCurrentStatus() != ProjectResultReport.StatusEnum.PENDING) {
            return execution;
        }
        List<UnresolvedFile> unresolvedFiles = execution.getCurrentReport().getUnresolvedFiles();
        Set<String> extraFileNames = buildStringSet(extraFiles);
        List<File> extraFiles = findAllFiles(extraFileNames);
        List<File> filesToAttach = new ArrayList<>();
        for (UnresolvedFile unresolvedFile : unresolvedFiles) {
            String fileName = unresolvedFile.getFileName();
            extraFiles.stream().filter(file -> file.getName().equals(fileName))
                    .findFirst()
                    .ifPresent(filesToAttach::add);
        }
        if (filesToAttach.size() == unresolvedFiles.size()) {
            execution = client.addFiles(execution, filesToAttach, false);
        } else {
            Set<String> unresolvedFileNames = new HashSet<>(unresolvedFiles.stream().map(UnresolvedFile::getFileName).collect(toSet()));
            Set<String> attachmentFileNames = filesToAttach.stream().map(File::getName).collect(toSet());
            unresolvedFileNames.removeAll(attachmentFileNames);
            throw new AbortException("Tests aborted because the project " + realPath +
                    " has the following unsatisfied file dependencies: " + unresolvedFileNames);
        }
        return execution;
    }

    private TestServerClient makeTestServerClient() throws MalformedURLException {
        return TestServerClient.fromUrl(serverUrl).withCredentials(userName, password);
    }

    private TestServerExecution executeRecipe(TestServerClient client, File recipeFile) throws AbortException {
        TestServerRecipeExecutor recipeExecutor = client.createRecipeExecutor();
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

    private TestServerExecution executeXmlProject(TestServerClient client, File projectFile) {
        ProjectExecutor executor = client.createProjectExecutor();
        Map<String, String> projectProperties = getProjectProperties();
        ProjectExecutionRequest.Builder executionRequest = ProjectExecutionRequest.Builder.forProjectFile(projectFile)
                .forTestSuite(testSuiteName)
                .forTestCase(testCaseName)
                .forEnvironment(environment)
                .forTags(buildStringSet(this.tags))
                .withProjectPassword(projectFilePassword)
                .withEndpoint(hostAndPort);
        if (!projectProperties.isEmpty()) {
            executionRequest.withCustomProperties(null, projectProperties);
        }
        return (TestServerExecution) executor.executeProject(executionRequest.build());
    }

    private TestServerExecution executeRepositoryProject(TestServerClient client) {
        ProjectExecutor executor = client.createProjectExecutor();
        Map<String, String> projectProperties = getProjectProperties();
        Set<String> tagNames = buildStringSet(tags);
        RepositoryProjectExecutionRequest.Builder executionRequest = RepositoryProjectExecutionRequest.Builder.forProject(pathToProjectFile)
                .fromRepository(repositoryName)
                .forTestSuite(testSuiteName)
                .forTestCase(testCaseName)
                .forEnvironment(environment)
                .forTags(tagNames)
                .withProjectPassword(projectFilePassword)
                .withEndpoint(hostAndPort);
        if (!projectProperties.isEmpty()) {
            executionRequest.withCustomProperties(null, projectProperties);
        }
        return (TestServerExecution) executor.executeRepositoryProject(executionRequest.build());
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

    public String getExtraFiles() {
        return extraFiles;
    }

    public String getReportFile() {
        return reportFile;
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

    private Optional<File> findFile(String path) {
        if (StringUtils.isBlank(path)) {
            return Optional.empty();
        }
        File file = new File(path);
        if (file.exists()) {
            return Optional.of(file);
        } else {
            file = new File(workspace, path);
            if (file.exists()) {
                return Optional.of(file);
            } else {
                try {
                    URL projectFileUrl = new URL(pathToProjectFile);
                    File projectFile = File.createTempFile("project", ".xml");
                    projectFile.deleteOnExit();
                    FileUtils.copyURLToFile(projectFileUrl, projectFile);
                    return Optional.of(projectFile);
                } catch (IOException e) {
                    return Optional.empty();
                }
            }
        }
    }

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
