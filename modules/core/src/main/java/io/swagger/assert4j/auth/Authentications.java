package io.swagger.assert4j.auth;

/**
 * Utility class for adding authentication to a request
 */

public class Authentications {
    public static AuthenticationBuilder basic(String username, String password) {
        return new BasicAuthenticationBuilder(username, password);
    }

    public static NTLMAuthenticationBuilder ntlm(String username, String password) {
        return new NTLMAuthenticationBuilder(username, password);
    }

    public static KerberosAuthenticationBuilder kerberos(String username, String password) {
        return new KerberosAuthenticationBuilder(username, password);
    }

    public static OAuth2AuthenticationBuilder oAuth2() {
        return new OAuth2AuthenticationBuilder();
    }
}
