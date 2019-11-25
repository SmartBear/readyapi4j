package com.smartbear.readyapi4j.cucumber.hiptest;

import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import okhttp3.Request;
import okhttp3.Response;
import picocli.CommandLine;

import java.io.IOException;

@CommandLine.Command(name = "clear", description = "Clears HipTest ActionWords from specified project")
public class ClearActionWords extends CommandBase {
    @CommandLine.Option(names = {"-p", "--project"}, required = true, description = "a HipTest project id")
    String hiptestProject;

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
        String content = loadActionWords( hiptestProject );
        JSONArray ids = JsonPath.read(content, "$.data[*].id");

        System.out.println( "Clearing " + ids.size() + " ActionWord" + ((ids.size() == 1 ) ? "" : "s"));
        for( int c = 0; c < ids.size(); c++ ){
            String id = ids.get(c ).toString();

            System.out.print( ".");
            Request request = buildHiptestRequest(new Request.Builder().url(hipTestEndpoint + "projects/" + hiptestProject + "/actionwords/" + id ).delete());
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()){
                System.err.println("Unexpected code " + response + " deleting actionword with id " + id );
            }
            response.close();
        }
        System.out.println("");
    }
}
