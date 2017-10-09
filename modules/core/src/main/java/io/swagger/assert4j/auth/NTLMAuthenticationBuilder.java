package io.swagger.assert4j.auth;

import io.swagger.assert4j.client.model.Authentication;

import static io.swagger.assert4j.support.Validations.validateNotEmpty;

/**
 * Builds an authentication object for NTLM authentication
 */

public class NTLMAuthenticationBuilder extends BasicAuthenticationBuilder {

    public NTLMAuthenticationBuilder(String username, String password) {
        super(username, password);
    }

    public NTLMAuthenticationBuilder setDomain(String domain) {
        authentication.setDomain(domain);
        return this;
    }

    @Override
    public Authentication build() {
        validateNotEmpty(authentication.getUsername(), "Missing username, it's a required parameter for NTLM Auth.");
        validateNotEmpty(authentication.getPassword(), "Missing password, it's a required parameter for NTLM Auth.");
        authentication.setType("NTLM");
        return authentication;
    }
}
