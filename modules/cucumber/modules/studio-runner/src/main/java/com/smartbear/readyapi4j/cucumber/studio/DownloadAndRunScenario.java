package com.smartbear.readyapi4j.cucumber.studio;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Strings;
import io.swagger.util.Json;
import okhttp3.Request;
import okhttp3.Response;
import picocli.CommandLine;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@CommandLine.Command( name = "download", description = "Downloads and runs a Cucumber Studio Scenario")
public class DownloadAndRunScenario extends CommandBase {
    @CommandLine.Option(names = "-args", parameterConsumer = RunParameterConsumer.class)
    List<String> args = new ArrayList<>();

    @CommandLine.Option(names = {"-p", "--project"}, required = true, description = "a Cucumber Studio project id")
    String studioProject;

    @CommandLine.Option(names = {"-f", "--folder"}, required = true, description = "a Cucumber Studio folder id")
    String studioFolder;

    @CommandLine.Option(names = {"-t", "--target"}, required = false, description = "an optional target folder for where to save the scenario")
    String targetFolder;

    @Override
    public void run() {
        try {
            String featureFile = downloadFeatureFile();
            args.add( featureFile );
            io.cucumber.core.cli.Main.main(args.toArray(new String[args.size()]));
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * Used to extract all arguments that need to be passed on to the cucumber runner
     */

    static class RunParameterConsumer implements CommandLine.IParameterConsumer {
        public void consumeParameters(Stack<String> args, CommandLine.Model.ArgSpec argSpec, CommandLine.Model.CommandSpec commandSpec) {
            List<String> list = argSpec.getValue();
            while (!args.isEmpty()) {
                list.add(args.pop());
            }
        }
    }

    private String downloadFeatureFile() throws Exception {
        System.out.println("Reading Features from Cucumber Studio");
        Request request = buildStudioRequest(new Request.Builder().url(studioEndpoint + "projects/" + studioProject + "/folders/"
                + studioFolder + "/feature").get());

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response + " getting feature file");

        JsonNode tree = Json.mapper().readTree(response.body().string());
        if (tree == null) {
            throw new Exception("Failed to parse JSON response");
        }
        JsonNode data = tree.get("data");
        if (data == null) {
            throw new Exception("Missing data node in JSON response");
        }
        JsonNode attributes = data.get("attributes");
        if (attributes == null) {
            throw new Exception("Missing data.attributes node in JSON response");
        }
        JsonNode feature = attributes.get("feature");
        if (feature == null || Strings.isNullOrEmpty(feature.asText())) {
            throw new Exception("Missing data.attributes.feature node in JSON response");
        }

        File targetFile = getTargetFile(targetFolder);
        FileWriter fileWriter = new FileWriter(targetFile);
        fileWriter.write(feature.asText());
        fileWriter.close();

        System.out.println("Cucumber feature(s) saved to file " + targetFile.toPath().toString());
        return targetFile.toPath().toString();
    }

    private File getTargetFile(String studioTargetFolder) throws IOException {
        if (!Strings.isNullOrEmpty(studioTargetFolder)) {
            File targetFolder = new File(studioTargetFolder);
            if (!targetFolder.exists()) {
                targetFolder.mkdirs();
            }

            return new File(targetFolder, "studio-" + studioProject + "-" + studioFolder + ".feature");
        }

        File targetFile = File.createTempFile("studio", ".feature");
        targetFile.deleteOnExit();
        return targetFile;
    }
}
