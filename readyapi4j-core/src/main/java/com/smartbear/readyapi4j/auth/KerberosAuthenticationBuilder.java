package com.smartbear.readyapi4j.auth;

import com.smartbear.readyapi.client.model.Authentication;

import static com.smartbear.readyapi4j.Validator.validateNotEmpty;

public class KerberosAuthenticationBuilder extends NTLMAuthenticationBuilder {
    public KerberosAuthenticationBuilder(String username, String password) {
        super(username, password);
    }

    @Override
    public Authentication build() {
        validateNotEmpty(authentication.getUsername(), "Missing username, it's a required parameter for SPNEGO/Kerberos Auth.");
        validateNotEmpty(authentication.getPassword(), "Missing password, it's a required parameter for SPNEGO/Kerberos Auth.");
        authentication.setType("SPNEGO/Kerberos");
        return authentication;
    }
}
