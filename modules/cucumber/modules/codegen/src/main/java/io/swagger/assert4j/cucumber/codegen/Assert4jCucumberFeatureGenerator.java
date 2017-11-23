package io.swagger.assert4j.cucumber.codegen;

import io.swagger.codegen.CodegenConfig;
import io.swagger.codegen.CodegenOperation;
import io.swagger.codegen.CodegenType;
import io.swagger.codegen.DefaultCodegen;
import io.swagger.models.Operation;
import io.swagger.models.Swagger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Custom Swagger Codegen module for generating json recipes from Swagger Definitions using the
 * included mustache template
 */

public class Assert4jCucumberFeatureGenerator extends DefaultCodegen implements CodegenConfig {

    protected String sourceFolder = "features";

    public CodegenType getTag() {
        return CodegenType.CLIENT;
    }

    public String getName() {
        return "Assert4jCucumberFeatureGenerator";
    }

    public String getHelp() {
        return "Generates Swagger Assert4j Feature file(s)";
    }

    public Assert4jCucumberFeatureGenerator() {
        super();

        outputFolder = "generated-test-resources/features";
        apiTemplateFiles.put("feature.mustache", ".feature");
        templateDir = "Assert4jCucumberFeatureGenerator";
    }

    @Override
    public void addOperationToGroup(String tag, String resourcePath, Operation operation, CodegenOperation co, Map<String, List<CodegenOperation>> operations) {
        List<CodegenOperation> group = operations.get(operation.getOperationId());
        if (group == null) {
            group = new ArrayList<CodegenOperation>();
        }

        group.add(co);
        operations.put(operation.getOperationId(), group);
    }

    @Override
    public String toApiName(String name) {
        return super.toApiName(name == null ? "" : name);
    }

    @Override
    public String snakeCase(String name) {
        return super.snakeCase(name == null ? "" : name);
    }

    @Override
    public void preprocessSwagger(Swagger swagger) {
        super.preprocessSwagger(swagger);
    }

    @Override
    public String apiFileFolder() {
        return outputFolder + "/" + sourceFolder;
    }
}