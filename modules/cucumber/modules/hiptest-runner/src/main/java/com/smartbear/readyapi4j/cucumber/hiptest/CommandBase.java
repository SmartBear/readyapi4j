package com.smartbear.readyapi4j.cucumber.hiptest;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

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

    @NotNull
    protected String loadActionWords(String hiptestProject) throws IOException {
        Request request = buildHiptestRequest(new Request.Builder().url(hipTestEndpoint + "projects/" + hiptestProject + "/actionwords").get());

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response + " getting actionwords");

        return response.body().string();
    }
}
