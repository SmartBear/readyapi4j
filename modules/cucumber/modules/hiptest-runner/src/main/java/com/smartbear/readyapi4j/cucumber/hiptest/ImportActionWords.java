package com.smartbear.readyapi4j.cucumber.hiptest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.jayway.jsonpath.JsonPath;
import com.smartbear.readyapi4j.cucumber.OASStepDefs;
import com.smartbear.readyapi4j.cucumber.RestStepDefs;
import cucumber.runtime.oas.OASWrapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.minidev.json.JSONArray;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

@CommandLine.Command(name = "import", description = "Imports HipTest ActionWords from OAS x-bdd extensions and REST StepDefs")
public class ImportActionWords extends CommandBase {

    private static final Logger LOG = LoggerFactory.getLogger(ImportActionWords.class);

    @CommandLine.Parameters(arity = "0..1", description = "a valid path or URL to an OAS 2.0/3.0 definition")
    String oasUrl;

    @CommandLine.Option(names = {"-p", "--project"}, description = "a HipTest project id")
    String hiptestProject;

    @CommandLine.Option(names = {"-d", "--default"}, required = false, description = "if import should include readyapi4j REST/OAS StepDefs")
    boolean importDefault;

    @CommandLine.Option(names = {"-l", "--list"}, required = false, description = "if import should only list found ActionWords instead of importing them. Will ignore project-id option.")
    boolean listOnly;

    @CommandLine.Option(names = {"-c", "--clear"}, required = false, description = "clear existing ActionWords")
    boolean clearExisting;

    private String oasActionWordId;

    @Override
    public void run() {
        try {
            if( clearExisting && hiptestProject != null ){
                clearExistingActionWords();
            }

            if( oasUrl == null && !importDefault ){
                System.err.println("Nothing to import!");
                return;
            }

            if (listOnly) {
                System.out.println( "Listing ActionWords only - skipping import");
            }
            else if( hiptestProject == null ){
                System.err.println("Missing HipTest Project id");
                return;
            }

            Set<String> existingWords = listOnly ? Sets.newHashSet() : getExistingActionWords();
            if( oasUrl != null ) {
                addOasWhensAndThensActionWords(oasUrl, existingWords);
            }

            if( importDefault ) {
                addStepDefsActionWordsFromAnnotations(existingWords, OASStepDefs.class);
                addStepDefsActionWordsFromAnnotations(existingWords, RestStepDefs.class);
            }
            else if( oasUrl != null ){
                ensureTheOasDefinitionAt();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ensureTheOasDefinitionAt() throws IOException {
        System.out.println( "Creating OAS ActionWord" );
        if( oasActionWordId == null ){
            oasActionWordId = addActionWord( "OAS definition", "the OAS definition at \"oas-url\"", false, null );
        }

        Map<String, Object> attributes = Maps.newHashMap();
        attributes.put("definition", "actionword 'the OAS definition at \"oas-url\"' (oas-url = \"" + oasUrl + "\") do\nend");

        Map<String, Object> data = Maps.newHashMap();
        data.put("type", "actionwords");
        data.put("id", oasActionWordId);

        data.put("attributes", attributes);
        Map<String, Object> root = Maps.newHashMap();
        root.put("data", data);
        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(root);

        Request request = buildHiptestRequest(new Request.Builder().url(hipTestEndpoint + "projects/" + hiptestProject + "/actionwords/" + oasActionWordId)
                .patch(RequestBody.create(content, JSON_MEDIA_TYPE)));

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response + " updating OAS actionword definition\n" + response.body().string());
        }
        response.close();
    }

    private void clearExistingActionWords() throws IOException {
        System.out.println( "Loading ActionWords");
        String content = loadActionWords();
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

    private void addStepDefsActionWordsFromAnnotations(Set<String> existingWords, Class<?> stepDefsClass) {
        List<Method> methods = MethodUtils.getMethodsListWithAnnotation(stepDefsClass, ActionWord.class);
        methods.stream()
                .filter(method -> method.isAnnotationPresent(Given.class)
                        || method.isAnnotationPresent(Then.class)
                        || method.isAnnotationPresent(When.class))
                .filter(method -> method.isAnnotationPresent(ActionWord.class))
                .filter(method -> !existingWords.contains(method.getAnnotation( ActionWord.class ).value()))
                .forEach(method -> {
                    try {
                        ActionWord actionWord = method.getAnnotation(ActionWord.class);
                        addActionWord(stepDefsClass.getName() + "$" + method.getName(), actionWord.value(), actionWord.addFreetext(), actionWord.description());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    private void addOasWhensAndThensActionWords(String oasUrl, Set<String> existingWords) throws IOException {
        OASWrapper oasWrapper = new OASWrapper(oasUrl);
        Map<String, OASWrapper.ThenResponseWrapper> thens = oasWrapper.getThens();
        thens.keySet().forEach(key -> {
            if (!existingWords.contains(key)) {
                try {
                    addActionWord("ApiResponse: " + thens.get(key).getApiResponse().getDescription(), key, false, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Map<String, OASWrapper.WhenOperationWrapper> whens = oasWrapper.getWhens();
        whens.keySet().forEach(key -> {
            if (!existingWords.contains(key)) {
                try {
                    addActionWord( "Operation: " + whens.get(key).getOperation().getOperationId(), key, false, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @NotNull
    private Set<String> getExistingActionWords() throws IOException {
        String content = loadActionWords();
        JSONArray words = JsonPath.read(content, "$.data[*]");
        Set<String> existingWords = Sets.newHashSet();

        for( int c = 0; c < words.size(); c++ ){
            Map<String, Object> data = (Map<String, Object>) words.get( c );
            Map<String, Object> attributes = (Map<String, Object>) data.get( "attributes" );

            String word = attributes.get( "name").toString();
            existingWords.add( word );

            if( word.matches("the OAS definition at (.*)")){
                oasActionWordId = data.get( "id" ).toString();
            }
        }

        LOG.info("Found " + existingWords.size() + " actionswords in project");
        return existingWords;
    }

    @NotNull
    private String loadActionWords() throws IOException {
        Request request = buildHiptestRequest(new Request.Builder().url(hipTestEndpoint + "projects/" + hiptestProject + "/actionwords").get());

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response + " getting actionwords");

        return response.body().string();
    }

    private String addActionWord(String from, String key, boolean hasFreetext, String description) throws IOException {
        if( listOnly ){
            LOG.info("Found actionword '" + key + "' for " + from);
            return null;
        }

        LOG.info("Adding actionword '" + key + "' from " + from);

        Map<String, Object> attributes = Maps.newHashMap();
        attributes.put("name", key);
        if (!Strings.isNullOrEmpty(description)) {
            attributes.put("description", description);
        }

        Map<String, Object> data = Maps.newHashMap();
        data.put("attributes", attributes);
        Map<String, Object> root = Maps.newHashMap();
        root.put("data", data);
        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(root);

        Request request = buildHiptestRequest(new Request.Builder().url(hipTestEndpoint + "projects/" + hiptestProject + "/actionwords")
                .post(RequestBody.create(content, JSON_MEDIA_TYPE)));

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response + " creating actionword");
        }

        JsonNode node = mapper.readTree(response.body().string());
        response.close();

        String id = node.get("data").get("id").asText();

        if (hasFreetext) {
            data.put("type", "actionwords");
            data.put("id", id);

            List<String> strings = extractParameters(key);
            strings.add("__free_text");

            StringBuilder builder = new StringBuilder();
            builder.append(strings.get(0)).append(" = \"\"");
            for (int c = 1; c < strings.size(); c++) {
                builder.append(", ").append(strings.get(c)).append(" = \"\"");
            }

            attributes.remove("name");
            attributes.put("definition", "actionword '" + key + "' (" + builder.toString() + ") do \nend\n");
            attributes.remove("description");

            content = mapper.writeValueAsString(root);
            request = buildHiptestRequest(new Request.Builder().url(hipTestEndpoint + "projects/" + hiptestProject + "/actionwords/" + id)
                    .patch(RequestBody.create(content, JSON_MEDIA_TYPE)));

            response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response + " updating actionword definition\n" + response.body().string());
            }
            response.close();
        }

        return id;
    }

    @NotNull
    private List<String> extractParameters(String key) {
        List<String> result = Lists.newArrayList();
        int ix = key.indexOf('"');
        while (ix != -1) {
            int ix2 = key.indexOf('"', ix + 1);
            if (ix2 > ix + 1) {
                result.add(key.substring(ix + 1, ix2));
                ix = key.indexOf('"', ix2 + 1);
            } else break;
        }

        return result;
    }
}
