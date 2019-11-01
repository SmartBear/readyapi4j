package cucumber.runtime;

import com.google.common.collect.Maps;
import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.parser.core.models.ParseOptions;
import io.swagger.v3.parser.core.models.SwaggerParseResult;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class OASWrapper {

    private Map<String, WhenOperationWrapper> whenMap = Maps.newConcurrentMap();
    private Map<String, ThenResponseWrapper> thenMap = Maps.newConcurrentMap();
    private String oas;

    public OASWrapper(String oas) {
        loadDefinition(oas);
    }

    public void loadDefinition(String oas) {
        if( oas.equalsIgnoreCase(this.oas)){
            return;
        }
        else {
            this.oas = oas;
        }

        OpenAPIParser parser = new OpenAPIParser();
        SwaggerParseResult result = parser.readLocation(oas, null, new ParseOptions());
        OpenAPI openAPI = result.getOpenAPI();
        if( openAPI == null ){
            System.err.println( "Failed to read OAS definition from [" + oas + "]; " + Arrays.toString( result.getMessages().toArray()));
            return;
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
                Object bddThen = extensions.get( "x-bdd-then" );
                if( bddThen instanceof List){
                    List<Object> bddThens = (List<Object>) bddThen;
                    bddThens.forEach( i -> {
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
                else if( bddThen instanceof String ){
                    thenMap.put( bddThen.toString(), new ThenResponseWrapper( apiResponse, null ));
                }
            }
        }
    }

    private void extractWhenExtensions(Operation operation) {
        Map<String, Object> extensions = operation.getExtensions();
        if( extensions != null ){
            Object bddWhen = extensions.get( "x-bdd-when" );
            if( bddWhen instanceof List){
                List<Object> bddWhens = (List<Object>) bddWhen;
                bddWhens.forEach( i -> {
                    if( i instanceof String ){
                        whenMap.put( i.toString(), new WhenOperationWrapper( operation, null ));
                    }
                    else if( i instanceof Map ){
                        Map<String,Object> whens = (Map)i;
                        if( whens.containsKey("when")) {
                            whenMap.put(whens.get("when").toString(), new WhenOperationWrapper(operation, (Map<String, String>) whens.get("parameters")));
                        }
                    }
                });
            }
            else if( bddWhen instanceof String ){
                whenMap.put( bddWhen.toString(), new WhenOperationWrapper(operation, null) );
            }
        }
    }

    public static class ThenResponseWrapper {

        private ApiResponse apiResponse;
        private List<Map<String,Object>> assertions;

        public ThenResponseWrapper(ApiResponse apiResponse, List<Map<String,Object>> assertions) {

            this.apiResponse = apiResponse;
            this.assertions = assertions;
        }

        public ApiResponse getApiResponse() {
            return apiResponse;
        }

        public List<Map<String,Object>> getAssertions() {
            return assertions;
        }
    }

    public static class WhenOperationWrapper
    {
        private Operation operation;
        private Map<String, String> parameters;

        public WhenOperationWrapper(Operation operation, Map<String,String> parameters) {
            this.operation = operation;
            this.parameters = parameters;
        }

        public Operation getOperation() {
            return operation;
        }

        public Map<String, String> getParameters() {
            return parameters;
        }
    }

    public WhenOperationWrapper getWhen(String text) {
        return whenMap.get(text);
    }

    public ThenResponseWrapper getThen(String text) {
        return thenMap.get(text);
    }
}
