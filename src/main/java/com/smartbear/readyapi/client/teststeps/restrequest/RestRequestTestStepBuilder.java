package com.smartbear.readyapi.client.teststeps.restrequest;

import com.smartbear.readyapi.client.teststeps.TestSteps;

public class RestRequestTestStepBuilder {

    public RestRequestBuilder get(String uri) {
        return new BaseRestRequest(uri, TestSteps.HttpMethod.GET);
    }

    public RestRequestBuilderWithBody post(String uri) {
        return new RestRequestWithBody(uri, TestSteps.HttpMethod.POST);
    }

    public RestRequestBuilderWithBody put(String uri) {
        return new RestRequestWithBody(uri, TestSteps.HttpMethod.PUT);
    }

    public RestRequestBuilder delete(String uri) {
        return new BaseRestRequest(uri, TestSteps.HttpMethod.DELETE);
    }
}
