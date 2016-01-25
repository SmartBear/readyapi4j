package com.smartbear.readyapi.client.auth;

import com.smartbear.readyapi.client.model.Authentication;

public abstract class AbstractAuthenticationBuilder implements AuthenticationBuilder {
    public abstract Authentication build();
}
