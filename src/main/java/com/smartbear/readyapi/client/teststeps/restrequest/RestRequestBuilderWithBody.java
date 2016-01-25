package com.smartbear.readyapi.client.teststeps.restrequest;

public interface RestRequestBuilderWithBody extends RestRequestBuilder<RestRequestBuilderWithBody> {
    RestRequestBuilderWithBody withRequestBody(String requestBody);

    RestRequestBuilderWithBody withMediaType(String mediaType);

    RestRequestBuilderWithBody withEncoding(String encoding);
}
