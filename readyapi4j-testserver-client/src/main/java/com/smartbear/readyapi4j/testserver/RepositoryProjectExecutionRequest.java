package com.smartbear.readyapi4j.testserver;

import com.smartbear.readyapi.client.model.CustomProperties;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class RepositoryProjectExecutionRequest {
    private String repositoryName;
    private String projectFileName;
    private String testSuiteName;
    private String testCaseName;
    private String environment;
    private Map<String, CustomProperties> customPropertiesMap = new HashMap<>();

    private RepositoryProjectExecutionRequest() {
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public String getProjectFileName() {
        return projectFileName;
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
        private Builder() {
        }

        private RepositoryProjectExecutionRequest projectExecutionRequest = new RepositoryProjectExecutionRequest();

        /**
         * @param projectFileName (mandatory) name of the project file in repository
         * @return Builder
         */
        public Builder forProject(String projectFileName) {
            projectExecutionRequest.projectFileName = projectFileName;
            return this;
        }

        /**
         * @param repositoryName (optional) name of the repository on TestServer.
         *                       Default repository will be used if repository name is not provided.
         *                       TestServer will return with error code if rpository name is not provided and default
         *                       repository doesn't exist.
         * @return Builder
         */
        public Builder fromRepository(String repositoryName) {
            projectExecutionRequest.repositoryName = repositoryName;
            return this;
        }

        /**
         * @param testSuiteName (optional) Name of the test suite in the project if specific test suite needs to be run.
         * @return
         */
        public Builder forTestSuite(String testSuiteName) {
            projectExecutionRequest.testSuiteName = testSuiteName;
            return this;
        }

        /**
         * @param testCaseName (optional) Name of the test case if specific test case needs to be run.
         * @return
         */
        public Builder forTestCase(String testCaseName) {
            projectExecutionRequest.testCaseName = testCaseName;
            return this;
        }

        /**
         * @param environmentName (optional) Name of the environment (defined in project) if project has multiple environments defined and
         *                        request is to execute for a particular environment
         * @return
         */
        public Builder forEnvironment(String environmentName) {
            projectExecutionRequest.environment = environmentName;
            return this;
        }

        public Builder withCustomProperty(String targetName, String propertyName, String value) {
            CustomProperties customProperties = getCustomPropertiesForTargetName(targetName);
            customProperties.getProperties().put(propertyName, value);
            return this;
        }

        public Builder withCustomProperties(String targetName, Map<String, String> properties) {
            CustomProperties customProperties = getCustomPropertiesForTargetName(targetName);
            customProperties.getProperties().putAll(properties);
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

        public RepositoryProjectExecutionRequest build() {
            if (StringUtils.isEmpty(projectExecutionRequest.projectFileName)) {
                throw new IllegalArgumentException("Project file name is a mandatory parameter.");
            }
            return projectExecutionRequest;
        }

        public static Builder newInstance() {
            return new Builder();
        }
    }
}
