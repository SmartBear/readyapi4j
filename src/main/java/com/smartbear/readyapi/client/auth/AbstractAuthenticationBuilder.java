package com.smartbear.readyapi.client.auth;

import io.swagger.client.model.Authentication;

public abstract class AbstractAuthenticationBuilder implements AuthenticationBuilder {
    public abstract Authentication build();
}
