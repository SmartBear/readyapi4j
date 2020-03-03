package cucumber.runtime.oas;

import com.google.common.collect.Maps;
import com.smartbear.readyapi4j.cucumber.OASCache;
import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.parser.core.models.ParseOptions;
import io.swagger.v3.parser.core.models.SwaggerParseResult;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class OASWrapper {

    private List<WhenOperationWrapper> whens = new ArrayList<>();
    private Map<String, ThenResponseWrapper> thenMap = Maps.newConcurrentMap();
    private String oas;

    public OASWrapper(String oas) throws IOException {
        loadDefinition(oas);
    }

    public void loadDefinition(String oas) throws IOException {
        File file = new File( oas );
        if( file.exists()){
            oas = file.toURI().toURL().toString();
        }

        if( oas.equalsIgnoreCase(this.oas)){
            return;
        }
        else {
            this.oas = oas;
        }

        OpenAPIParser parser = new OpenAPIParser();
        SwaggerParseResult result = parser.readLocation(oas, OASCache.getSystemAuthorizationValues(), new ParseOptions());
        OpenAPI openAPI = result.getOpenAPI();
        if( openAPI == null ){
            throw new IOException( "Failed to read OAS definition from [" + oas + "]; " + Arrays.toString( result.getMessages().toArray()));
        }

        for( PathItem pathItem : openAPI.getPaths().values()){
            for( Operation operation : pathItem.readOperations()){
                extractWhenExtensions(operation);
                extractThenExtensions(operation);
            }
        }
    }

    private void extractThenExtensions(Operation operation) {
        for( ApiResponse apiResponse : operation.getResponses().values() ){
            Map<String, Object> extensions = apiResponse.getExtensions();
            if( extensions != null ){
                Object cucumberThen = extensions.get( "x-cucumber-then" );
                if( cucumberThen instanceof List){
                    List<Object> cucumberThens = (List<Object>) cucumberThen;
                    cucumberThens.forEach( i -> {
                        if( i instanceof String ) {
                            thenMap.put(i.toString(), new ThenResponseWrapper(apiResponse, null));
                        }
                        else if( i instanceof Map ){
                            Map<String,Object> thens = (Map)i;
                            if( thens.containsKey("then")) {
                                thenMap.put(thens.get("then").toString(), new ThenResponseWrapper(apiResponse, (List<Map<String,Object>>) thens.get("assertions")));
                            }
                        }
                    } );
                }
                else if( cucumberThen instanceof String ){
                    thenMap.put( cucumberThen.toString(), new ThenResponseWrapper( apiResponse, null ));
                }
            }
        }
    }

    private void extractWhenExtensions(Operation operation) {
        Map<String, Object> extensions = operation.getExtensions();
        if( extensions != null ){
            Object cucumberWhen = extensions.get( "x-cucumber-when" );
            if( cucumberWhen instanceof List){
                List<Object> cucumberWhens = (List<Object>) cucumberWhen;
                cucumberWhens.forEach( i -> {
                    if( i instanceof String ){
                        whens.add(new WhenOperationWrapper(i.toString(), operation, null));
                    }
                    else if( i instanceof Map ) {
                        Map<String, Object> whenMap = (Map) i;
                        if (whenMap.containsKey("when")) {
                            whens.add(new WhenOperationWrapper(whenMap.get("when").toString(), operation, (Map<String, String>) whenMap.get("parameters")));
                        }
                    }
                });
            } else if (cucumberWhen instanceof String) {
                whens.add(new WhenOperationWrapper(cucumberWhen.toString(), operation, null));
            }
        }
    }

    public Map<String, ThenResponseWrapper> getThens() {
        return Collections.unmodifiableMap(thenMap);
    }

    public List<WhenOperationWrapper> getWhens() {
        return Collections.unmodifiableList(whens);
    }

    public WhenOperationWrapper getWhen(String text) {
        for (WhenOperationWrapper wrapper : whens) {
            if (wrapper.matches(text)) {
                return wrapper;
            }
        }

        return null;
    }

    public ThenResponseWrapper getThen(String text) {
        return thenMap.get(text);
    }
}
