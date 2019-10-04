package com.smartbear.readyapi4j.util.rest.local;

import com.google.gson.Gson;
import spark.ResponseTransformer;

public class LocalServerJsonTransformer implements ResponseTransformer {

    private Gson gson = new Gson();

    @Override
    public String render(Object javaObject) throws Exception {
        return gson.toJson(javaObject);
    }
}
