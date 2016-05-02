package com.smartbear.readyapi.client.teststeps.restrequest;

public interface RestRequestStepWithBodyBuilder extends RestRequestStepBuilder<RestRequestStepWithBodyBuilder> {
    RestRequestStepWithBodyBuilder withRequestBody(String requestBody);

    RestRequestStepWithBodyBuilder withMediaType(String mediaType);

    RestRequestStepWithBodyBuilder withEncoding(String encoding);
}
