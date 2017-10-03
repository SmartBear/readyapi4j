package io.swagger.assert4j.auth;

import io.swagger.assert4j.client.model.Authentication;

import static io.swagger.assert4j.Validator.validateNotEmpty;

/**
 * Builds an authentication object for basic authentication
 */

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
