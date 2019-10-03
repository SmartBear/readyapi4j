package io.swagger.assert4j.cucumber;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import io.swagger.util.Json;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Set;

/**
 * Custom Cucumber Runner that adds the built in StepDefs to the invocation of
 * the cucumber.api.cli.Main class
 */

public class CucumberRunner {

    private static final Logger LOG = LoggerFactory.getLogger(CucumberRunner.class);

    public static final String HIPTEST_CLIENTID = "hiptest.clientid";
    public static final String HIPTEST_PROJECT = "hiptest.project";
    public static final String HIPTEST_TOKEN = "hiptest.token";
    public static final String HIPTEST_UID = "hiptest.uid";
    public static final String HIPTEST_FOLDER = "hiptest.folder";
    public static final String HIPTEST_ENDPOINT = "hiptest.endpoint";
    public static final String HIPTEST_DEFAULT_ENDPOINT = "https://app.hiptest.com/api/";
    public static final String HIPTEST_ACCEPT = "hiptest.accept";
    public static final String HIPTEST_DEFAULT_ACCEPT = "application/vnd.api+json; version=1";

    /**
     * Invokes the Cucumber CLI with the internal StepDefs package and properties defined in a cucumber.properties file
     *
     * @param args command line arguments
     * @throws Throwable
     */

    public static void main(String[] args) throws Throwable {
        System.out.println("Swagger Assert4j Cucumber Runner");

        ArrayList<String> argsList = Lists.newArrayList(args);
        argsList.add(0, RestStepDefs.class.getPackage().getName());
        argsList.add(0, "-g");

        if (new File("cucumber.properties").exists()) {
            Properties properties = new Properties();
            properties.load(new FileReader("cucumber.properties"));

            extractCucumberProperties(argsList, properties);
            extractHipTestProperties(argsList, properties);
        }

        io.cucumber.core.cli.Main.main(argsList.toArray(new String[argsList.size()]));
    }

    private static void extractHipTestProperties(ArrayList<String> argsList, Properties properties) throws Exception {
        String hiptestToken = properties.getProperty(HIPTEST_TOKEN, System.getProperty(HIPTEST_TOKEN));
        if (Strings.isNullOrEmpty(hiptestToken))
            return;

        String hiptestClientId = properties.getProperty(HIPTEST_CLIENTID, System.getProperty(HIPTEST_CLIENTID));
        if (Strings.isNullOrEmpty(hiptestClientId))
            return;

        String hiptestUid = properties.getProperty(HIPTEST_UID, System.getProperty(HIPTEST_UID));
        if (Strings.isNullOrEmpty(hiptestUid))
            return;

        String hiptestProject = properties.getProperty(HIPTEST_PROJECT, System.getProperty(HIPTEST_PROJECT));
        if (Strings.isNullOrEmpty(hiptestProject))
            return;

        String hiptestFolder = properties.getProperty(HIPTEST_FOLDER, System.getProperty(HIPTEST_FOLDER));
        if (Strings.isNullOrEmpty(hiptestFolder))
            return;

        String hipTestEndpoint = properties.getProperty(HIPTEST_ENDPOINT,
                System.getProperty(HIPTEST_ENDPOINT, HIPTEST_DEFAULT_ENDPOINT));

        String hipTestAccept = properties.getProperty(HIPTEST_ACCEPT,
                System.getProperty(HIPTEST_ACCEPT, HIPTEST_DEFAULT_ACCEPT));

        LOG.info("Reading Features from HipTest");

        final OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(hipTestEndpoint + "projects/" + hiptestProject + "/folders/"
                + hiptestFolder + "/feature").get()
                .header("Accept", hipTestAccept)
                .header("access-token", hiptestToken)
                .header("client", hiptestClientId)
                .header("uid", hiptestUid)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        LOG.debug("Got JSON response from HipTest: " + response.body());

        JsonNode tree = Json.mapper().readTree(response.body().string());
        if (tree == null) {
            throw new Exception("Failed to parse HipTest JSON response");
        }
        JsonNode data = tree.get("data");
        if (data == null) {
            throw new Exception("Missing data node in HipTest JSON response");
        }
        JsonNode attributes = data.get("attributes");
        if (attributes == null) {
            throw new Exception("Missing data.attributes node in HipTest JSON response");
        }
        JsonNode feature = attributes.get("feature");
        if (feature == null || Strings.isNullOrEmpty(feature.asText())) {
            throw new Exception("Missing data.attributes.feature node in HipTest JSON response");
        }

        File tempFile = File.createTempFile("cucumber", ".feature");
        tempFile.deleteOnExit();
        FileWriter fileWriter = new FileWriter(tempFile);
        fileWriter.write(feature.asText());
        fileWriter.close();

        LOG.info("Cucumber feature(s) saved to temporary file " + tempFile.toPath().toString());
        argsList.add(tempFile.toPath().toString());
    }

    private static void extractCucumberProperties(ArrayList<String> argsList, Properties properties) {
        Set<String> keys = properties.stringPropertyNames();
        for (String key : keys) {
            if (key.startsWith("cucumber.")) {
                String param = key.substring("cucumber.".length());
                if (!param.isEmpty()) {
                    argsList.add(0, properties.getProperty(key));
                    if (param.length() == 1) {
                        argsList.add(0, "-" + param);
                    } else {
                        argsList.add(0, "--" + param);
                    }
                }
            }
        }
    }
}
