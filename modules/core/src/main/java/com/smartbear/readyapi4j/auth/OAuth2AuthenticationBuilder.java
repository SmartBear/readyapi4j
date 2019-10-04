package com.smartbear.readyapi4j.auth;

import com.smartbear.readyapi4j.client.model.Authentication;

/**
 * Builds an authentication object for OAuth2 authentication
 */

public class OAuth2AuthenticationBuilder implements AuthenticationBuilder {

    protected Authentication authentication = new Authentication();

    public OAuth2AuthenticationBuilder withAccessToken(String accessToken) {
        authentication.setAccessToken(accessToken);
        return this;
    }

    public OAuth2AuthenticationBuilder withAccessTokenUri(String accessTokenUri) {
        authentication.setAccessTokenUri(accessTokenUri);
        return this;
    }

    public OAuth2AuthenticationBuilder withAccessTokenPosition(String accessTokenPosition) {
        authentication.setAccessTokenPosition(accessTokenPosition);
        return this;
    }

    public OAuth2AuthenticationBuilder withClientId(String clientId) {
        authentication.setClientId(clientId);
        return this;
    }

    public OAuth2AuthenticationBuilder withClientSecret(String clientSecret) {
        authentication.setClientSecret(clientSecret);
        return this;
    }

    public OAuth2AuthenticationBuilder withRefreshToken(String refreshToken) {
        authentication.setRefreshToken(refreshToken);
        return this;
    }

    @Override
    public Authentication build() {
        authentication.setType("OAuth 2.0");
        return authentication;
    }
}
