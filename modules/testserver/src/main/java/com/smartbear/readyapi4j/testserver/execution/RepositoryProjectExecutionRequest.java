package com.smartbear.readyapi4j.testserver.execution;

import org.apache.commons.lang3.StringUtils;

/**
 * Request object used to execute tests in/on a project available in a repository on TestServer
 */

public class RepositoryProjectExecutionRequest extends ProjectExecutionRequestBase {
    private String repositoryName;
    private String projectFileName;

    String getRepositoryName() {
        return repositoryName;
    }

    String getProjectFileName() {
        return projectFileName;
    }

    public static class Builder extends AbstractProjectExecutionRequestBuilder<Builder, RepositoryProjectExecutionRequest> {
        private Builder() {
            super(new RepositoryProjectExecutionRequest());
        }

        /**
         * @param projectFileName (mandatory) name of the project file in repository
         * @return Builder
         */
        public Builder withProject(String projectFileName) {
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
         * @return builds the RepositoryProjectExecutionRequest as configured
         */
        @Override
        public RepositoryProjectExecutionRequest build() {
            if (StringUtils.isEmpty(projectExecutionRequest.projectFileName)) {
                throw new IllegalArgumentException("Project file name is a mandatory parameter.");
            }
            return projectExecutionRequest;
        }

        /**
         * @return a new instance of this builder for the specified repository project
         */

        public static Builder forProject(String projectFileName) {
            return new Builder().withProject(projectFileName);
        }
    }
}
