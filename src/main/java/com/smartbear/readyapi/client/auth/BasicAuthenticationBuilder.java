package com.smartbear.readyapi.client.auth;

import io.swagger.client.model.Authentication;

import static com.smartbear.readyapi.client.Validator.validateNotEmpty;

public class BasicAuthenticationBuilder extends AbstractAuthenticationBuilder {
    protected Authentication authentication = new Authentication();

    public BasicAuthenticationBuilder(String username, String password) {
        authentication.setUsername(username);
        authentication.setPassword(password);
    }

    @Override
    public Authentication build() {
        validateNotEmpty(authentication.getUsername(), "Missing username, it's a required parameter for Basic Auth.");
        validateNotEmpty(authentication.getPassword(), "Missing password, it's a required parameter for Basic Auth.");
        authentication.setType("Basic");
        return authentication;
    }
}
