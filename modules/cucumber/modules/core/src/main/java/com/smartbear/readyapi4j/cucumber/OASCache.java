package com.smartbear.readyapi4j.cucumber;

import com.google.api.client.util.Lists;
import com.google.common.collect.Maps;
import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.core.models.ParseOptions;
import io.swagger.v3.parser.core.models.SwaggerParseResult;

import javax.inject.Singleton;
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
            SwaggerParseResult swaggerParseResult = parser.readLocation(swaggerUrl,
                    Lists.newArrayList(),
                    options);
            cache.put(swaggerUrl, swaggerParseResult.getOpenAPI());
        }

        return cache.get(swaggerUrl);
    }
}
