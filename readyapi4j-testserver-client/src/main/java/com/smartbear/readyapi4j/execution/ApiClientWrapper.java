package com.smartbear.readyapi4j.execution;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.HTTPSProperties;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;
import io.swagger.client.ApiClient;
import io.swagger.client.Pair;
import io.swagger.client.auth.Authentication;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Most of this code is auto generated, with additional code for accepting self-signed server certificates.
 */
public class ApiClientWrapper extends ApiClient {

    private Client client;

    public <T> T invokeAPI(String path, String method, List<Pair> queryParams, Object body, Map<String, File> formParams, String accept, String contentType, String[] authNames, GenericType<T> returnType) throws ApiException {
        Map<String, String> headerParams = new HashMap<>();
        updateParamsForAuth(authNames, queryParams, headerParams);

        Client client = getClient();
        if (client == null) {
            throw new IllegalStateException("Could not create client instance");
        }

        StringBuilder b = new StringBuilder();
        b.append("?");
        if (queryParams != null) {
            for (Pair queryParam : queryParams) {
                if (!queryParam.getName().isEmpty()) {
                    b.append(escapeString(queryParam.getName()));
                    b.append("=");
                    b.append(escapeString(queryParam.getValue()));
                    b.append("&");
                }
            }
        }

        String querystring = b.substring(0, b.length() - 1);

        WebResource.Builder builder;
        if (accept == null) {
            builder = client.resource(getBasePath() + path + querystring).getRequestBuilder();
        } else {
            builder = client.resource(getBasePath() + path + querystring).accept(accept);
        }

        for (String key : headerParams.keySet()) {
            builder = builder.header(key, headerParams.get(key));
        }

        String encodedFormParams = null;
        if (contentType.startsWith("multipart/form-data")) {
            FormDataMultiPart mp = new FormDataMultiPart();
            for (Map.Entry<String, File> param : formParams.entrySet()) {
                File file = param.getValue();
                mp.bodyPart(new FileDataBodyPart(param.getKey(), file, MediaType.APPLICATION_OCTET_STREAM_TYPE));
            }
            body = mp;
        }

        ClientResponse response = null;

        if ("GET".equals(method)) {
            response = (ClientResponse) builder.get(ClientResponse.class);
        } else if ("POST".equals(method)) {
            if (encodedFormParams != null) {
                response = builder.type(contentType).post(ClientResponse.class, encodedFormParams);
            } else if (body == null) {
                response = builder.post(ClientResponse.class, null);
            } else if (body instanceof FormDataMultiPart) {
                response = builder.type(contentType).post(ClientResponse.class, body);
            } else {
                response = builder.type(contentType).post(ClientResponse.class, serialize(body, contentType));
            }
        } else if ("PUT".equals(method)) {
            if (encodedFormParams != null) {
                response = builder.type(contentType).put(ClientResponse.class, encodedFormParams);
            } else if (body == null) {
                response = builder.put(ClientResponse.class, serialize(body, contentType));
            } else {
                response = builder.type(contentType).put(ClientResponse.class, serialize(body, contentType));
            }
        } else if ("DELETE".equals(method)) {
            if (encodedFormParams != null) {
                response = builder.type(contentType).delete(ClientResponse.class, encodedFormParams);
            } else if (body == null) {
                response = builder.delete(ClientResponse.class);
            } else {
                response = builder.type(contentType).delete(ClientResponse.class, serialize(body, contentType));
            }
        } else {
            throw new ApiException(500, "unknown method type " + method);
        }

        if (response.getStatusInfo() == ClientResponse.Status.NO_CONTENT) {
            return null;
        } else if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
            if (returnType == null) {
                return null;
            } else {
                return deserialize(response, returnType);
            }
        } else if (response.getStatusInfo().getStatusCode() == 429) {
            throw new UsageLimitException(response.getStatusInfo().getStatusCode(), getResponseBody(response),
                response.getHeaders());
        } else {
            throw new ApiException(response.getStatusInfo().getStatusCode(), getResponseBody(response),
                response.getHeaders());
        }
    }

    private String getResponseBody(ClientResponse response) {
        String respBody = null;
        if (response.hasEntity()) {
            try {
                respBody = String.valueOf(response.getEntity(String.class));
            } catch (RuntimeException e) {
                // e.printStackTrace();
            }
        }
        return respBody;
    }

    private Client getClient() {
        if (this.client == null) {
            try {
                ClientConfig clientConfig = getClientConfigWithoutCertificateValidation();
                this.client = Client.create(clientConfig);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            }
        }
        return this.client;
    }

    public Object serialize(Object obj, String contentType) throws ApiException {
        try {
            if (contentType.startsWith("application/json") && !(obj instanceof byte[])) {
                ObjectMapper mapper = getObjectMapper();
                mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
                return mapper.writeValueAsString(obj);
            } else {
                return obj;
            }
        } catch (JsonProcessingException e) {
            throw new ApiException(400, "can not serialize object into Content-Type: " + contentType);
        }
    }

    public ObjectMapper getObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.registerModule(new JodaModule());
        return mapper;
    }

    private void updateParamsForAuth(String[] authNames, List<Pair> queryParams, Map<String, String> headerParams) {
        for (String authName : authNames) {
            Authentication auth = getAuthentications().get(authName);
            if (auth == null) {
                throw new ApiException(400, "Authentication undefined: " + authName);
            }
            auth.applyToParams(queryParams, headerParams);
        }
    }

    public <T> T deserialize(ClientResponse response, GenericType<T> returnType) throws ApiException {
        String contentType = null;
        List<String> contentTypes = response.getHeaders().get("Content-Type");
        if (contentTypes != null && !contentTypes.isEmpty()) {
            contentType = contentTypes.get(0);
        }
        if (contentType == null) {
            throw new ApiException(500, "missing Content-Type in response");
        }

        String body = "";
        if (response.hasEntity()) {
            body = response.getEntity(String.class);
        }

        if (contentType.startsWith("application/json")) {
            return deserialize(returnType, body);
        } else {
            throw new ApiException(500, "can not deserialize Content-Type: " + contentType);
        }
    }

    private <T> T deserialize(GenericType<T> returnType, String body) throws ApiException {
        ObjectMapper mapper = getObjectMapper();
        JavaType javaType = mapper.constructType(returnType.getType());
        try {
            return mapper.readValue(body, javaType);
        } catch (IOException e) {
            if (returnType.getType().equals(String.class)) {
                return (T) body;
            } else {
                throw new ApiException(500, e.getMessage());
            }
        }
    }

    private ClientConfig getClientConfigWithoutCertificateValidation() throws NoSuchAlgorithmException, KeyManagementException {

        SSLContext sslContext = SSLContext.getInstance("TLS");
        TrustManager[] certs = new TrustManager[]{
            new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException {
                }

                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException {
                }
            }
        };
        sslContext.init(null, certs, new SecureRandom());

        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

        ClientConfig config = new DefaultClientConfig();
        config.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES,
            new HTTPSProperties(new HostnameVerifier() {

                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            }, sslContext));

        return config;
    }
}
