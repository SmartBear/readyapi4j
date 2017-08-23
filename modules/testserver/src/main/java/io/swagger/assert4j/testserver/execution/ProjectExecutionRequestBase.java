package io.swagger.assert4j.testserver.execution;

import io.swagger.assert4j.client.model.CustomProperties;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class ProjectExecutionRequestBase {
    String testSuiteName;
    String testCaseName;
    String environment;
    Map<String, CustomProperties> customPropertiesMap = new HashMap<>();
    Set<String> tags = new HashSet<>();
    String projectPassword;
    String endpoint;

    String getTestSuiteName() {
        return testSuiteName;
    }

    String getTestCaseName() {
        return testCaseName;
    }

    String getEnvironment() {
        return environment;
    }

    public Set<String> getTags() {
        return tags;
    }

    public String getProjectPassword() {
        return projectPassword;
    }

    public String getEndpoint() {
        return endpoint;
    }

    Map<String, CustomProperties> getCustomPropertiesMap() {
        return customPropertiesMap;
    }

    public static abstract class AbstractProjectExecutionRequestBuilder<Builder extends AbstractProjectExecutionRequestBuilder,
            Request extends ProjectExecutionRequestBase> {
        Request projectExecutionRequest;

        AbstractProjectExecutionRequestBuilder(Request executionRequest) {
            this.projectExecutionRequest = executionRequest;
        }

        /**
         * @param password (optional) The password used to decrypt an encrypted project.
         * @return Builder
         */
        public Builder withProjectPassword(String password) {
            projectExecutionRequest.projectPassword = password;
            return (Builder) this;
        }

        /**
         * @param testSuiteName (optional) Name of the test suite in the project if specific test suite needs to be run.
         * @return Builder
         */
        public Builder forTestSuite(String testSuiteName) {
            projectExecutionRequest.testSuiteName = testSuiteName;
            return (Builder) this;
        }

        /**
         * @param testCaseName (optional) Name of the test case if specific test case needs to be run.
         * @return Builder
         */
        public Builder forTestCase(String testCaseName) {
            projectExecutionRequest.testCaseName = testCaseName;
            return (Builder) this;
        }

        /**
         * @param environmentName (optional) Name of the environment (defined in project) if project has multiple environments defined and
         *                        request is to execute for a particular environment
         * @return Builder
         */
        public Builder forEnvironment(String environmentName) {
            projectExecutionRequest.environment = environmentName;
            return (Builder) this;
        }

        /**
         * @param tags (optional) The tags that you want to filter test cases by.
         * @return Builder
         */
        public Builder forTags(Set<String> tags) {
            projectExecutionRequest.tags = tags;
            return (Builder) this;
        }

        /**
         * @param endpoint A string in the format host:port that will be used to replace the host and port in all
         *                 HTTP requests sent by the test.
         * @return Builder
         */
        public Builder withEndpoint(String endpoint) {
            projectExecutionRequest.endpoint = endpoint;
            return (Builder) this;
        }

        /**
         * Adds a custom property to be passed on to the execution of the project
         *
         * @param targetName   the TestSuite or TestCase that will receive the properties - set to null for project-level properties
         * @param propertyName the name of the property
         * @param value        the property value
         * @return Builder
         */
        public Builder withCustomProperty(String targetName, String propertyName, String value) {
            CustomProperties customProperties = getCustomPropertiesForTargetName(targetName);
            customProperties.getProperties().put(propertyName, value);
            return (Builder) this;
        }

        /**
         * Adds custom properties to be passed on to the execution of the project
         *
         * @param targetName the TestSuite or TestCase that will receive the properties - set to null for project-level properties
         * @param properties a map containing the desired name/value pairs
         * @return Builder
         */
        public Builder withCustomProperties(String targetName, Map<String, String> properties) {
            CustomProperties customProperties = getCustomPropertiesForTargetName(targetName);
            customProperties.getProperties().putAll(properties);
            return (Builder) this;
        }

        /**
         * Adds a project property to be passed on to the execution of the project
         *
         * @param propertyName the name of the property
         * @param value        the property value
         * @return Builder
         */
        public Builder withProjectProperty(String propertyName, String value) {
            CustomProperties customProperties = getCustomPropertiesForTargetName(null);
            customProperties.getProperties().put(propertyName, value);
            return (Builder) this;
        }

        public abstract Request build();

        private CustomProperties getCustomPropertiesForTargetName(String targetName) {
            CustomProperties customProperties = projectExecutionRequest.customPropertiesMap.get(targetName);
            if (customProperties == null) {
                customProperties = new CustomProperties();
                customProperties.setTargetName(targetName);
                projectExecutionRequest.customPropertiesMap.put(targetName, customProperties);
            }
            return customProperties;
        }

    }
}
