package io.swagger.assert4j;

import com.migcomponents.migbase64.Base64;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaClientCodegen", date = "2019-08-27T17:58:38.260+02:00")
public class HttpBasicAuth implements Authentication {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void applyToParams(List<Pair> queryParams, Map<String, String> headerParams) {
        if (username == null && password == null) {
            return;
        }
        String str = (username == null ? "" : username) + ":" + (password == null ? "" : password);
        try {
            headerParams.put("Authorization", "Basic " + Base64.encodeToString(str.getBytes("UTF-8"), false));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
