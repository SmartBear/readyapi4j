package io.swagger.assert4j.support;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class JsonUtils {
    private static ObjectMapper mapper;
    private static ObjectMapper pathMapper;
    private static ObjectMapper responseMapper;

    public JsonUtils() {
    }

    public static ObjectMapper mapper() {
        if (mapper == null) {
            mapper = ObjectMapperFactory.createJson();
        }

        return mapper;
    }

    public static ObjectWriter pretty() {
        return mapper().writer(new DefaultPrettyPrinter());
    }

    public static String pretty(Object o) {
        try {
            return pretty().writeValueAsString(o);
        } catch (Exception var2) {
            var2.printStackTrace();
            return null;
        }
    }

    public static void prettyPrint(Object o) {
        try {
            System.out.println(pretty().writeValueAsString(o).replace("\r", ""));
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    protected static ObjectMapper pathMapper() {
        if (pathMapper == null) {
            pathMapper = ObjectMapperFactory.createJson(false, true);
        }

        return pathMapper;
    }

    protected static ObjectMapper responseMapper() {
        if (responseMapper == null) {
            responseMapper = ObjectMapperFactory.createJson(false, false);
        }

        return responseMapper;
    }

}
