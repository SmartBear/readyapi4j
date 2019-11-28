package com.smartbear.readyapi4j.cucumber;

import com.google.api.client.util.Lists;
import com.google.common.collect.Maps;
import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.core.models.AuthorizationValue;
import io.swagger.v3.parser.core.models.ParseOptions;
import io.swagger.v3.parser.core.models.SwaggerParseResult;

import javax.inject.Singleton;
import java.util.List;
import java.util.Map;

/**
 * Utility class for loading and caching OAS definitions
 */

@Singleton
public class OASCache {

    private final OpenAPIParser parser;
    private Map<String, OpenAPI> cache = Maps.newHashMap();

    public OASCache() {
        parser = new OpenAPIParser();
    }

    public OpenAPI getOAS(String swaggerUrl) {
        if (!cache.containsKey(swaggerUrl)) {
            ParseOptions options = new ParseOptions();
            options.setResolveFully(true);
            options.setResolve(true);
            options.setFlatten(true);
            options.setResolveCombinators(true);

            List<AuthorizationValue> authorizationValues = getSystemAuthorizationValues();

            SwaggerParseResult swaggerParseResult = parser.readLocation(swaggerUrl,
                    authorizationValues, options);

            cache.put(swaggerUrl, swaggerParseResult.getOpenAPI());
        }

        return cache.get(swaggerUrl);
    }

    public static List<AuthorizationValue> getSystemAuthorizationValues() {
        List<AuthorizationValue> authorizationValues = Lists.newArrayList();
        String swaggerhubApikey = System.getProperty( "swaggerhub.apikey", System.getenv( "swaggerhub.apikey"));
        if( swaggerhubApikey != null && swaggerhubApikey.length() > 0 ){
            authorizationValues.add( new AuthorizationValue()
                    .keyName("Authorization")
                    .value(swaggerhubApikey)
                    .type("header"));
        }
        return authorizationValues;
    }
}
