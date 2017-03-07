package com.smartbear.readyapi4j.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.swagger.util.Json;
import io.swagger.util.Yaml;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContentUtils {

    private final static Logger LOG = LoggerFactory.getLogger(ContentUtils.class);

    public static String serializeContent(Object requestBody, String mediaType) {

        if (requestBody == null) {
            return null;
        }

        if (mediaType == null || requestBody instanceof String) {
            return requestBody.toString();
        }

        String result = null;
        mediaType = mediaType.toLowerCase();
        try {
            if (mediaType.endsWith("json")) {
                result = Json.mapper().writeValueAsString(requestBody);
            }
            else if (mediaType.endsWith("yaml")) {
                result = Yaml.mapper().writeValueAsString(requestBody);
            }
            else if (mediaType.endsWith("xml")) {
                result = new XmlMapper().writeValueAsString( requestBody);
            }
        } catch (JsonProcessingException e) {
            LOG.error("Failed to serialize body with mediaType " + mediaType, e);
        }

        if (result == null) {
            result = requestBody.toString();
        }

        return result;
    }
}
