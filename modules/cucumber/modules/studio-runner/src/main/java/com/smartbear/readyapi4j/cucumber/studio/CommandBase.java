package com.smartbear.readyapi4j.cucumber.studio;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

abstract public class CommandBase extends CucumberStudioProperties implements Runnable {

    protected OkHttpClient client = new OkHttpClient();
    protected static MediaType JSON_MEDIA_TYPE = MediaType.get("application/json");

    protected Request buildStudioRequest(Request.Builder builder) {
        return builder.header("Accept", CucumberStudioProperties.studioAccept)
                .header("access-token", studioToken)
                .header("client", studioClientId)
                .header("uid", studioUid)
                .build();
    }

    @Override
    public void run() {

    }

    @NotNull
    protected String loadActionWords(String studioProject) throws IOException {
        Request request = buildStudioRequest(new Request.Builder().url(studioEndpoint + "projects/" + studioProject + "/actionwords").get());

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response + " getting actionwords");

        return response.body().string();
    }
}
