package com.smartbear.readyapi4j.testserver.execution;

import com.smartbear.readyapi.client.model.CustomProperties;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Command wrapper for executing an existing project with the ProjectExcecutor
 */

public class ProjectExecutionRequest {
    private File projectFile;
    private String testSuiteName;
    private String testCaseName;
    private String environment;

    private Map<String, CustomProperties> customPropertiesMap = new HashMap<>();

    public File getProjectFile() {
        return projectFile;
    }

    public String getTestSuiteName() {
        return testSuiteName;
    }

    public String getTestCaseName() {
        return testCaseName;
    }

    public String getEnvironment() {
        return environment;
    }

    public Map<String, CustomProperties> getCustomPropertiesMap() {
        return customPropertiesMap;
    }

    public static class Builder {
        protected Builder() {
        }

        private ProjectExecutionRequest projectExecutionRequest = new ProjectExecutionRequest();

        /**
         * @param projectFile (required) projectFile
         * @return Builder
         */
        public Builder withProjectFile(File projectFile) {
            projectExecutionRequest.projectFile = projectFile;
            return this;
        }

        /**
         * @param testSuiteName (optional) Name of the test suite in the project if specific test suite needs to be run.
         * @return Builder
         */
        public Builder forTestSuite(String testSuiteName) {
            projectExecutionRequest.testSuiteName = testSuiteName;
            return this;
        }

        /**
         * @param testCaseName (optional) Name of the test case if specific test case needs to be run.
         * @return Builder
         */
        public Builder forTestCase(String testCaseName) {
            projectExecutionRequest.testCaseName = testCaseName;
            return this;
        }

        /**
         * @param environmentName (optional) Name of the environment (defined in project) if project has multiple environments defined and
         *                        request is to execute for a particular environment
         * @return Builder
         */
        public Builder forEnvironment(String environmentName) {
            projectExecutionRequest.environment = environmentName;
            return this;
        }

        /**
         * Adds a custom property to be passed on to the execution of the project
         * @param targetName the TestSuite or TestCase that will receive the properties - set to null for project-level properties
         * @param propertyName the name of the property
         * @param value the property value
         * @return Builder
         */

        public Builder withCustomProperty(String targetName, String propertyName, String value) {
            CustomProperties customProperties = getCustomPropertiesForTargetName(targetName);
            customProperties.getProperties().put(propertyName, value);
            return this;
        }

        /**
         * Adds custom properties to be passed on to the execution of the project
         * @param targetName the TestSuite or TestCase that will receive the properties - set to null for project-level properties
         * @param properties a map containing the desired name/value pairs
         * @return Builder
         */

        public Builder withCustomProperties(String targetName, Map<String, String> properties) {
            CustomProperties customProperties = getCustomPropertiesForTargetName(targetName);
            customProperties.getProperties().putAll(properties);
            return this;
        }

        /**
         * Adds a project property to be passed on to the execution of the project
         * @param propertyName the name of the property
         * @param value the property value
         * @return Builder
         */

        public Builder withProjectProperty(String propertyName, String value) {
            CustomProperties customProperties = getCustomPropertiesForTargetName(null);
            customProperties.getProperties().put(propertyName, value);
            return this;
        }

        private CustomProperties getCustomPropertiesForTargetName(String targetName) {
            CustomProperties customProperties = projectExecutionRequest.customPropertiesMap.get(targetName);
            if (customProperties == null) {
                customProperties = new CustomProperties();
                customProperties.setTargetName(targetName);
                projectExecutionRequest.customPropertiesMap.put(targetName, customProperties);
            }
            return customProperties;
        }

        /**
         * @return builds the ProjectExecutionRequest as configured
         */

        public ProjectExecutionRequest build() {
            return projectExecutionRequest;
        }

        /**
         * @return a new instance of this builder for the specified project
         */

        public static Builder forProjectFile(File projectFile ) {
            return new Builder().withProjectFile(projectFile);
        }
    }
}
