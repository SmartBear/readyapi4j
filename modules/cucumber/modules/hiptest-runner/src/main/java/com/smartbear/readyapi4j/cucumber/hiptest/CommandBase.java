package com.smartbear.readyapi4j.cucumber.hiptest;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;

abstract public class CommandBase extends HipTestProperties implements Runnable {

    protected OkHttpClient client = new OkHttpClient();
    protected static MediaType JSON_MEDIA_TYPE = MediaType.get("application/json");

    protected Request buildHiptestRequest(Request.Builder builder) {
        return builder.header("Accept", HipTestProperties.hipTestAccept)
                .header("access-token", hiptestToken)
                .header("client", hiptestClientId)
                .header("uid", hiptestUid)
                .build();
    }

    @Override
    public void run() {

    }
}
