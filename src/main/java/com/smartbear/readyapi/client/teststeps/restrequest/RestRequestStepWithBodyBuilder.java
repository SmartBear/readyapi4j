package com.smartbear.readyapi.client.teststeps.restrequest;

import com.smartbear.readyapi.client.teststeps.TestSteps;

public class RestRequestStepWithBodyBuilder extends RestRequestStepBuilder<RestRequestStepWithBodyBuilder> {

    public RestRequestStepWithBodyBuilder(String uri, TestSteps.HttpMethod post) {
        super(uri, post);
    }

    public RestRequestStepWithBodyBuilder withRequestBody(String requestBody) {
        testStep.setRequestBody(requestBody);
        return this;
    }

    public RestRequestStepWithBodyBuilder withMediaType(String mediaType) {
        testStep.setMediaType(mediaType);
        return this;
    }

    public RestRequestStepWithBodyBuilder withEncoding(String encoding) {
        testStep.setEncoding(encoding);
        return this;
    }

}
