package io.swagger.assert4j.testserver.execution;

import java.io.File;

/**
 * Command wrapper for executing an existing project with the ProjectExecutor
 */

public class ProjectExecutionRequest extends ProjectExecutionRequestBase {
    private File projectFile;

    File getProjectFile() {
        return projectFile;
    }

    public static class Builder extends AbstractProjectExecutionRequestBuilder<Builder, ProjectExecutionRequest> {
        protected Builder() {
            super(new ProjectExecutionRequest());
        }

        /**
         * @param projectFile (required) projectFile
         * @return Builder
         */
        public Builder withProjectFile(File projectFile) {
            projectExecutionRequest.projectFile = projectFile;
            return this;
        }

        /**
         * @return builds the ProjectExecutionRequest as configured
         */
        @Override
        public ProjectExecutionRequest build() {
            return projectExecutionRequest;
        }

        /**
         * @return a new instance of this builder for the specified project
         */
        public static Builder forProjectFile(File projectFile) {
            return new Builder().withProjectFile(projectFile);
        }
    }
}
