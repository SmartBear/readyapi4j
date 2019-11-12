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
import cucumber.runtime.OASWrapper;
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

    @CommandLine.Option(names = {"-p", "--project"}, description = "a hiptest project id")
    String hiptestProject;

    @CommandLine.Option(names = {"-d", "--default"}, required = false, description = "if import should include readyapi4j REST/OAS StepDefs")
    boolean importDefault;

    @CommandLine.Option(names = {"-l", "--list"}, required = false, description = "if import should only list found ActionWords instead of importing them. Will ignore project-id option.")
    boolean listOnly;

    @Override
    public void run() {
        try {
            Set<String> existingWords = listOnly ? Sets.newHashSet() : getExistingActionWords();
            if( oasUrl != null ) {
                addOasWhensAndThensActionWords(oasUrl, existingWords);
            }

            if( importDefault ) {
                addStepDefsActionWordsFromAnnotations(existingWords, RestStepDefs.class);
                addStepDefsActionWordsFromAnnotations(existingWords, OASStepDefs.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        Request request = buildHiptestRequest(new Request.Builder().url(hipTestEndpoint + "projects/" + hiptestProject + "/actionwords").get());

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response + " getting actionwords");

        String content = response.body().string();
        JSONArray names = JsonPath.read(content, "$.data[*].attributes.name");
        Set<String> existingWords = Sets.newHashSet(names.toArray(new String[names.size()]));
        LOG.info("Found " + existingWords.size() + " actionswords in project");
        return existingWords;
    }

    private void addActionWord(String from, String key, boolean hasFreetext, String description) throws IOException {
        if( listOnly ){
            LOG.info("Found actionword '" + key + "' for " + from);
            return;
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

        if (hasFreetext) {
            JsonNode node = mapper.readTree(response.body().string());
            response.close();

            String id = node.get("data").get("id").asText();

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
        }
        response.close();
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
