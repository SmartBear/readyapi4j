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

    private Map<String,Operation> whenMap = Maps.newConcurrentMap();
    private Map<String,ApiResponse> thenMap = Maps.newConcurrentMap();
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
                Map<String, Object> extensions = operation.getExtensions();
                if( extensions != null ){
                    Object bddWhen = extensions.get( "x-bdd-when" );
                    if( bddWhen instanceof List){
                        List<Object> bddWhens = (List<Object>) bddWhen;
                        bddWhens.forEach( i -> whenMap.put( i.toString(), operation ));
                    }
                    else if( bddWhen instanceof String ){
                        whenMap.put( bddWhen.toString(), operation );
                    }
                }

                for( ApiResponse apiResponse : operation.getResponses().values() ){
                    extensions = apiResponse.getExtensions();
                    if( extensions != null ){
                        Object bddThen = extensions.get( "x-bdd-then" );
                        if( bddThen instanceof List ){
                            List<Object> bddThens = (List<Object>) bddThen;
                            bddThens.forEach( i -> thenMap.put( i.toString(), apiResponse ));
                        }
                        else if( bddThen instanceof String ){
                            thenMap.put( bddThen.toString(), apiResponse );
                        }
                    }
                }
            }
        }
    }

    public Operation getWhen(String text) {
        return whenMap.get(text);
    }

    public ApiResponse getThen(String text) {
        return thenMap.get(text);
    }
}
