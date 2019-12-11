package com.smartbear.readyapi4j.cucumber.studio;

import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import okhttp3.Request;
import okhttp3.Response;
import picocli.CommandLine;

import java.io.IOException;

@CommandLine.Command(name = "clear", description = "Clears Cucumber Studio ActionWords from specified project")
public class ClearActionWords extends CommandBase {
    @CommandLine.Option(names = {"-p", "--project"}, required = true, description = "a Cucumber Studio project id")
    String studioProject;

    @Override
    public void run() {
        try {
            clearExistingActionWords();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clearExistingActionWords() throws IOException {
        System.out.println( "Loading ActionWords");
        String content = loadActionWords(studioProject);
        JSONArray ids = JsonPath.read(content, "$.data[*].id");

        System.out.println( "Clearing " + ids.size() + " ActionWord" + ((ids.size() == 1 ) ? "" : "s"));
        for( int c = 0; c < ids.size(); c++ ){
            String id = ids.get(c ).toString();

            System.out.print( ".");
            Request request = buildStudioRequest(new Request.Builder().url(studioEndpoint + "projects/" + studioProject + "/actionwords/" + id ).delete());
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()){
                System.err.println("Unexpected code " + response + " deleting actionword with id " + id );
            }
            response.close();
        }
        System.out.println("\nFinished!");
    }
}
