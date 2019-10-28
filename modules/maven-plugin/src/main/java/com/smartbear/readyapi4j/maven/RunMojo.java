package com.smartbear.readyapi4j.maven;

/*
 * Copyright 2004-2015 SmartBear Software
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.google.common.collect.Lists;
import com.smartbear.readyapi4j.TestRecipe;
import com.smartbear.readyapi4j.TestRecipeBuilder;
import com.smartbear.readyapi4j.client.model.TestCaseResultReport;
import com.smartbear.readyapi4j.client.model.TestJobReport;
import com.smartbear.readyapi4j.client.model.TestStepResultReport;
import com.smartbear.readyapi4j.client.model.TestSuiteResultReport;
import com.smartbear.readyapi4j.execution.Execution;
import com.smartbear.readyapi4j.execution.RecipeExecutor;
import com.smartbear.readyapi4j.facade.execution.RecipeExecutorBuilder;
import com.smartbear.readyapi4j.junitreport.JUnitReport;
import com.smartbear.readyapi4j.junitreport.TestFailureException;
import com.smartbear.readyapi4j.testengine.execution.ProjectExecutionRequest;
import com.smartbear.readyapi4j.testengine.execution.ProjectExecutor;
import com.smartbear.readyapi4j.testengine.execution.TestEngineClient;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.filtering.MavenFilteringException;
import org.apache.maven.shared.filtering.MavenResourcesExecution;
import org.apache.maven.shared.filtering.MavenResourcesFiltering;
import org.apache.maven.shared.model.fileset.FileSet;
import org.apache.maven.shared.model.fileset.util.FileSetManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

import static com.smartbear.readyapi4j.client.model.TestStepResultReport.AssertionStatusEnum.FAIL;
import static com.smartbear.readyapi4j.testengine.execution.ProjectExecutionRequest.Builder.forProjectFile;

@Mojo(name = "run")
public class RunMojo extends AbstractMojo {
    @Component
    private MavenResourcesFiltering resourcesFiltering;

    @Component
    private MavenProject mavenProject;

    @Component
    private MavenSession mavenSession;

    @Parameter
    private Map properties;

    @Parameter
    private String environment;

    @Parameter(defaultValue = "false")
    private boolean async;

    @Parameter
    private String callback;

    @Parameter
    private boolean disableFiltering;

    @Parameter(defaultValue = "true")
    private boolean failOnFailures;

    @Parameter(required = false, property = "testengine.username")
    private String username;

    @Parameter(required = false, property = "testengine.password")
    private String password;

    @Parameter(required = false, property = "testengine.endpoint")
    private String server;

    @Parameter(defaultValue = "${project.basedir}/src/test/resources/recipes", required = true)
    private File recipeDirectory;

    @Parameter(defaultValue = "${project.basedir}/src/test/resources/projects", required = true)
    private File xmlProjectDirectory;

    @Parameter(defaultValue = "${project.basedir}/target/test-recipes", required = true)
    private File targetDirectory;

    @Parameter(required = true, defaultValue = "false")
    private boolean ignoreRecipes;

    @Parameter(required = true, defaultValue = "false")
    private boolean ignoreProjectFiles;

    @Parameter(defaultValue = "${basedir}/target/surefire-reports")
    private File reportTarget;

    private RecipeExecutor recipeExecutor;

    public void execute()
            throws MojoExecutionException, MojoFailureException {
        try {
            if (propertySetAndNotFalse("skipApiTests") || propertySetAndNotFalse("skipTests") ||
                    "true".equals(getProperty("maven.test.skip"))) {
                return;
            }

            List<String> recipeFiles = null;
            List<String> xmlProjectFiles = null;
            if (shouldRunRecipes()) {
                recipeFiles = getIncludedFiles(recipeDirectory, "**/*.json");
            }

            if (shouldRunProjects()) {
                xmlProjectFiles = getIncludedFiles(xmlProjectDirectory, "**/*.xml");
            }

            if (shouldRunRecipes() && notPresent(recipeFiles)) {
                getLog().warn("No recipe present to be executed in recipe directory: " + recipeDirectory);
            }
            if (shouldRunProjects() && notPresent(xmlProjectFiles)) {
                getLog().warn("No XML projects present to be executed in xml-project directory: " + xmlProjectDirectory);
            }

            if (notPresent(recipeFiles) && notPresent(xmlProjectFiles)) {
                getLog().warn("No recipe or project found to run.");
                return;
            }

            readRecipeProperties();
            initRecipeExecutor();

            JUnitReport report = async ? null : new JUnitReport(properties, new MavenErrorLog());

            Result recipeExecutionResult = runRecipes(recipeFiles, report);
            Result projectExecutionResult = runProjects(xmlProjectFiles, report);


            getLog().info("ReadyAPI TestEngine Maven Plugin");
            getLog().info("--------------------------------------");
            getLog().info("Recipes run: " + recipeExecutionResult.executionCount);
            getLog().info("Projects run: " + projectExecutionResult.executionCount);
            int totalFailures = recipeExecutionResult.failureCount + projectExecutionResult.failureCount;
            getLog().info("Failures: " + totalFailures);

            if (report != null) {
                report.setTestSuiteName(mavenProject.getName());
                report.setNoofFailuresInTestSuite(totalFailures);

                if (!reportTarget.exists() && !reportTarget.mkdirs()) {
                    throw new MojoExecutionException("Failed to create report directory: " + reportTarget);
                }
                report.save(new File(reportTarget, "recipe-report.xml"));
            }

            if (totalFailures > 0 && failOnFailures) {
                throw new MojoFailureException(totalFailures + " failures during test execution");
            }
        } catch (MojoFailureException e) {
            throw e;
        } catch (Exception e) {
            throw new MojoExecutionException("Error running recipe", e);
        }
    }

    private boolean propertySetAndNotFalse(String skipApiTests) {
        String property = getProperty(skipApiTests);
        return property != null && !property.equals("false");
    }

    private String getProperty(String propertyName) {
        String property = mavenSession.getUserProperties().getProperty(propertyName);
        if (property == null) {
            property = mavenSession.getCurrentProject().getProperties().getProperty(propertyName);
        }
        return property;
    }

    private Result runProjects(List<String> xmlProjectFiles, JUnitReport report) throws MojoFailureException, IOException, MavenFilteringException {
        Result result = new Result();
        TestJobReport response;
        if (shouldRunProjects() && xmlProjectFiles != null) {
            for (String file : xmlProjectFiles) {
                String fileName = file.toLowerCase();
                File projectFile = new File(xmlProjectDirectory, file);
                if (fileName.endsWith(".xml")) {
                    result.incrementExecution();
                    response = runXmlProject(projectFile);
                } else {
                    getLog().warn("Unexpected filename: " + fileName);
                    continue;
                }
                try {
                    handleResponse(response, report, file);
                } catch (MojoFailureException exception) {
                    getLog().error(exception);
                    result.incrementFailure();
                }
            }
        }
        return result;
    }

    private Result runRecipes(List<String> recipeFiles, JUnitReport report) throws MojoFailureException, IOException, MavenFilteringException {
        Result result = new Result();
        TestJobReport response;
        if (shouldRunRecipes() && recipeFiles != null) {
            for (String file : recipeFiles) {
                String fileName = file.toLowerCase();
                File recipeFile = new File(recipeDirectory, file);
                if (fileName.endsWith(".json")) {
                    result.incrementExecution();
                    response = runJsonRecipe(recipeFile);
                } else {
                    getLog().warn("Unexpected filename: " + fileName);
                    continue;
                }
                try {
                    getLog().debug("Response body:" + result.toString());
                    if (report != null) {
                        report.handleResponse(response, file);
                    }
                } catch (TestFailureException e) {
                    getLog().error(e);
                    result.incrementFailure();
                }
            }
        }
        return result;
    }

    private boolean shouldRunProjects() {
        return !ignoreProjectFiles;
    }

    private boolean shouldRunRecipes() {
        return !ignoreRecipes;
    }

    private boolean notPresent(List<String> files) {
        return files == null || files.isEmpty();
    }

    private void initRecipeExecutor() {
        RecipeExecutorBuilder recipeExecutorBuilder = new RecipeExecutorBuilder();
        if (StringUtils.isNotEmpty(server)) {
            recipeExecutorBuilder.withEndpoint(server);
            recipeExecutorBuilder.withUser(username);
            recipeExecutorBuilder.withPassword(password);
        }
        recipeExecutor = recipeExecutorBuilder.build();
        getLog().info("Execution mode: " + recipeExecutor.getExecutionMode());
    }

    private void readRecipeProperties() throws IOException {
        File recipeProperties = new File(recipeDirectory, "recipe.properties");
        if (properties == null) {
            properties = new HashMap();
        }
        if (recipeProperties.exists()) {
            Properties props = new Properties();
            props.load(new FileInputStream(recipeProperties));
            getLog().debug("Read " + props.size() + " properties from recipe.properties");

            // override with properties in config section
            if (properties != null) {
                props.putAll(properties);
            }

            properties = props;
        }
    }

    private List<String> getIncludedFiles(File rootDirectory, String fileExtensionFilter) {

        FileSetManager fileSetManager = new FileSetManager();

        FileSet fileSet = new FileSet();
        fileSet.setDirectory(rootDirectory.getAbsolutePath());
        fileSet.addInclude(fileExtensionFilter);

        return Arrays.asList(fileSetManager.getIncludedFiles(fileSet));
    }

    private void handleResponse(TestJobReport result, JUnitReport report, String recipeFileName) throws IOException, MojoFailureException {
        getLog().debug("Response body:" + result.toString());

        if (report != null) {
            if (result.getStatus() == TestJobReport.StatusEnum.FAILED) {

                String message = logErrorsToConsole(result);
                report.addTestCaseWithFailure(recipeFileName, result.getTotalTime(),
                        message, "<missing stacktrace>", new HashMap<String, String>(properties));

                throw new MojoFailureException("Recipe failed, recipe file: " + recipeFileName);
            } else {
                report.addTestCase(recipeFileName, result.getTotalTime(), new HashMap<String, String>(properties));
            }
        }
    }

    private String logErrorsToConsole(TestJobReport result) {

        List<String> messages = new ArrayList<>();

        result.getTestSuiteResultReports().stream()
                .map(TestSuiteResultReport::getTestCaseResultReports) // creates List<List<TestCaseResultReport>>
                .flatMap(Collection::stream) //converts List<List<TestCaseResultReport>> to List<TestCaseResultReport>
                .map(TestCaseResultReport::getTestStepResultReports) // List<List<TestStepResultReport>>
                .flatMap(Collection::stream) // flattens List<List<TestStepResultReport>> to List<TestStepResultReport>
                .filter(testStepResult -> testStepResult.getAssertionStatus() == FAIL) //keep only failed tests
                .map(TestStepResultReport::getMessages) // creates List<List<String>>
                .flatMap(Collection::stream) // flattens List<List<String>> to List<String>
                .forEach(message -> { //process each message from List<String>
                    messages.add(message);
                    getLog().error("- " + message);
                });
        return Arrays.toString(messages.toArray());
    }

    private TestJobReport runXmlProject(File file) throws IOException, MavenFilteringException, MojoFailureException {
        if (StringUtils.isEmpty(server)) {
            throw new MojoFailureException("Project execution is supported only with TestEngine, not locally.");
        }
        getLog().info("Executing project " + file.getName());

        ProjectExecutionRequest executionRequest = forProjectFile(file)
                .forEnvironment(environment)
                .build();
        TestEngineClient testServerClient = TestEngineClient.fromUrl(server);
        testServerClient.setCredentials(username, password);
        ProjectExecutor projectExecutor = testServerClient.createProjectExecutor();
        Execution execution = async ? projectExecutor.submitProject(executionRequest) :
                projectExecutor.executeProject(executionRequest);
        return execution.getCurrentReport();
    }

    private TestJobReport runJsonRecipe(File file) throws IOException, MavenFilteringException, MojoFailureException {

        if (!disableFiltering) {
            file = filterRecipe(file);
        }

        getLog().info("Running recipe " + file.getName());
        TestRecipe testRecipe;
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            String recipeText = IOUtils.toString(fileInputStream);
            testRecipe = TestRecipeBuilder.createFrom(recipeText);
        }
        if (testRecipe == null) {
            throw new MojoFailureException(String.format("Couldn't read test recipe from file: %s, please make sure it contains a valid test recipe.", file.getName()));
        }

        Execution execution = async ? recipeExecutor.submitRecipe(testRecipe) : recipeExecutor.executeRecipe(testRecipe);
        return execution.getCurrentReport();
    }

    private File filterRecipe(File file) throws MavenFilteringException, MojoFailureException {
        if (!targetDirectory.exists()) {
            if (!targetDirectory.mkdirs()) {
                throw new MojoFailureException("Couldn't create target directory: " + targetDirectory);
            }
        }

        Resource fileResource = new Resource();
        fileResource.setDirectory(recipeDirectory.getAbsolutePath());

        String filename = file.getAbsolutePath().substring(recipeDirectory.getAbsolutePath().length());

        fileResource.addInclude(filename);
        fileResource.setFiltering(true);

        MavenResourcesExecution resourcesExecution = new MavenResourcesExecution();
        resourcesExecution.setOutputDirectory(targetDirectory);
        resourcesExecution.setResources(Lists.newArrayList(fileResource));
        resourcesExecution.setOverwrite(true);
        resourcesExecution.setSupportMultiLineFiltering(true);
        resourcesExecution.setEncoding(Charset.defaultCharset().toString());

        if (properties != null && !properties.isEmpty()) {
            Properties props = new Properties();
            props.putAll(properties);
            getLog().debug("Adding additional properties: " + properties.toString());
            resourcesExecution.setAdditionalProperties(props);
        }

        resourcesExecution.setMavenProject(mavenProject);
        resourcesExecution.setMavenSession(mavenSession);
        resourcesExecution.setUseDefaultFilterWrappers(true);

        resourcesFiltering.filterResources(resourcesExecution);

        return new File(targetDirectory, filename);
    }

    private static class Result {
        private int executionCount;
        private int failureCount;

        void incrementExecution() {
            executionCount++;
        }

        void incrementFailure() {
            failureCount++;
        }
    }

    private class MavenErrorLog implements JUnitReport.ErrorLog {
        @Override
        public void logError(String message) {
            getLog().error(message);
        }
    }
}
