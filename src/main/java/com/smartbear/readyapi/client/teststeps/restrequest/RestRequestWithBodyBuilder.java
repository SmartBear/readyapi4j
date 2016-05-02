package com.smartbear.readyapi.client.teststeps.restrequest;

import com.smartbear.readyapi.client.teststeps.TestSteps;

public class RestRequestWithBodyBuilder extends BaseRestRequestBuilder<RestRequestBuilderWithBody> implements RestRequestBuilderWithBody {

    public RestRequestWithBodyBuilder(String uri, TestSteps.HttpMethod post) {
        super(uri, post);
    }

    public RestRequestBuilderWithBody withRequestBody(String requestBody) {
        testStep.setRequestBody(requestBody);
        return this;
    }

    public RestRequestBuilderWithBody withMediaType(String mediaType) {
        testStep.setMediaType(mediaType);
        return this;
    }

    public RestRequestBuilderWithBody withEncoding(String encoding) {
        testStep.setEncoding(encoding);
        return this;
    }

}
