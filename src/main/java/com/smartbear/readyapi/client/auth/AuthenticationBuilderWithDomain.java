package com.smartbear.readyapi.client.auth;

public interface AuthenticationBuilderWithDomain extends AuthenticationBuilder {
    AuthenticationBuilderWithDomain setDomain(String domain);
}
