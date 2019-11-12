package com.smartbear.readyapi4j.cucumber.hiptest;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.smartbear.readyapi4j.cucumber.CucumberRunner;
import io.swagger.util.Json;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@CommandLine.Command( name = "run", description = "Downloads and runs a HipTest scenario")
public class DownloadAndRunScenario extends CommandBase {
    private static final Logger LOG = LoggerFactory.getLogger(DownloadAndRunScenario.class);

    @CommandLine.Parameters(description = "Cucumber CLI arguments")
    String[] args;

    @CommandLine.Option(names = {"-p", "--project"}, required = true, description = "a hiptest project id")
    String hiptestProject;

    @CommandLine.Option(names = {"-f", "--folder"}, required = true, description = "a hiptest folder id")
    String hiptestFolder;

    @CommandLine.Option(names = {"-t", "--target"}, required = false, description = "an optional target folder for where to save the scenario")
    String targetFolder;

    @Override
    public void run() {
        try {
            String featureFile = getFeatureFile();
            List<String> argsList = Lists.newArrayList(args);
            argsList.add(featureFile);

            CucumberRunner.main(argsList.toArray(new String[argsList.size()]));
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private String getFeatureFile() throws Exception {

        LOG.info("Reading Features from HipTest");
        Request request = buildHiptestRequest(new Request.Builder().url(hipTestEndpoint + "projects/" + hiptestProject + "/folders/"
                + hiptestFolder + "/feature").get());

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response + " getting feature file");

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

        File targetFile = getTargetFile(targetFolder);
        FileWriter fileWriter = new FileWriter(targetFile);
        fileWriter.write(feature.asText());
        fileWriter.close();

        LOG.info("Cucumber feature(s) saved to temporary file " + targetFile.toPath().toString());
        return targetFile.toPath().toString();
    }

    private File getTargetFile(String hiptestTargetFolder) throws IOException {
        if (!Strings.isNullOrEmpty(hiptestTargetFolder)) {
            File targetFolder = new File(hiptestTargetFolder);
            if (!targetFolder.exists()) {
                targetFolder.mkdirs();
            }

            return new File(targetFolder, "hiptest-" + hiptestProject + "-" + hiptestFolder + ".feature");
        }

        File targetFile = File.createTempFile("hiptest", ".feature");
        targetFile.deleteOnExit();
        return targetFile;
    }
}
