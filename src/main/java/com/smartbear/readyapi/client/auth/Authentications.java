package com.smartbear.readyapi.client.auth;

public class Authentications {
    public static AuthenticationBuilder basic(String username, String password) {
        return new BasicAuthenticationBuilder(username, password);
    }

    public static AuthenticationBuilderWithDomain ntlm(String username, String password) {
        return new NTLMAuthenticationBuilder(username, password);
    }

    public static AuthenticationBuilderWithDomain kerberos(String username, String password) {
        return new KerberosAuthenticationBuilder(username, password);
    }
}
