package com.smartbear.readyapi4j.auth;

import com.smartbear.readyapi.client.model.Authentication;

import static com.smartbear.readyapi4j.Validator.validateNotEmpty;

public class BasicAuthenticationBuilder implements AuthenticationBuilder {
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
