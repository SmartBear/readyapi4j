package io.swagger.assert4j.teststeps.restrequest;

import io.swagger.assert4j.support.ContentUtils;
import io.swagger.assert4j.teststeps.TestSteps;

public class RestRequestStepWithBodyBuilder extends RestRequestStepBuilder<RestRequestStepWithBodyBuilder> {

    public RestRequestStepWithBodyBuilder(String uri, TestSteps.HttpMethod post) {
        super(uri, post);
    }

    public RestRequestStepWithBodyBuilder withRequestBody(Object requestBody) {
        getTestStep().setRequestBody(
            ContentUtils.serializeContent(requestBody, getTestStep().getMediaType()));

        return this;
    }

    public RestRequestStepWithBodyBuilder withMediaType(String mediaType) {
        getTestStep().setMediaType(mediaType);
        return this;
    }

    public RestRequestStepWithBodyBuilder withEncoding(String encoding) {
        getTestStep().setEncoding(encoding);
        return this;
    }
}
