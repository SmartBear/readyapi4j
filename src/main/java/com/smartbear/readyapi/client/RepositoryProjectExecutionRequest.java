package com.smartbear.readyapi.client;

import org.apache.commons.lang3.StringUtils;

public class RepositoryProjectExecutionRequest {
    private String repositoryName;
    private String projectFileName;
    private String testSuiteName;
    private String testCaseName;
    private String environment;

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
        public Builder testSuite(String testSuiteName) {
            projectExecutionRequest.testSuiteName = testSuiteName;
            return this;
        }

        /**
         * @param testCaseName (optional) Name of the test case if specific test case needs to be run.
         * @return
         */
        public Builder testCase(String testCaseName) {
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
