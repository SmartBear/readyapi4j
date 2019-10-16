package cucumber.runtime;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import cucumber.runtime.io.ResourceLoader;
import cucumber.runtime.java.JavaBackend;
import cucumber.runtime.snippets.FunctionNameGenerator;
import gherkin.pickles.PickleStep;
import io.cucumber.stepexpression.Argument;
import io.cucumber.stepexpression.TypeRegistry;
import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.parser.core.models.ParseOptions;
import io.swagger.v3.parser.core.models.SwaggerParseResult;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class OASBackend implements Backend {

    private JavaBackend javaBackend;
    private List<StepDefinitionWrapper> wrapperList = Lists.newArrayList();

    public OASBackend(ResourceLoader resourceLoader, TypeRegistry typeRegistry) {
        javaBackend = new JavaBackend(resourceLoader, typeRegistry);
    }

    @Override
    public void loadGlue(Glue glue, List<URI> list) {
        ArrayList<URI> glueList = Lists.newArrayList(list);

        try {
            glueList.add( new URI("classpath:com/smartbear/readyapi4j/cucumber" ));
            glueList.add( new URI("classpath:com/smartbear/cucumber/samples/extension" ));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        GlueWrapper glueWrapper = new GlueWrapper(glue);
        javaBackend.loadGlue(glueWrapper, glueList);

        String oas = System.getProperty("readyapi4j.oas");
        if( oas == null || oas.isEmpty()){
            return;
        }

        System.out.println( "Loading OAS definition from " + oas);

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
                    if( bddWhen instanceof List ){
                        List<Object> bddWhens = (List<Object>) bddWhen;
                        bddWhens.forEach( i -> addWhenARequestToAnOperation( glueWrapper, i.toString(), operation.getOperationId()));
                    }
                    else if( bddWhen instanceof String ){
                        addWhenARequestToAnOperation( glueWrapper, bddWhen.toString(), operation.getOperationId() );
                    }
                }

                for( ApiResponse  apiResponse : operation.getResponses().values() ){
                    extensions = apiResponse.getExtensions();
                    if( extensions != null ){
                        Object bddThen = extensions.get( "x-bdd-then" );
                        if( bddThen instanceof List ){
                            List<Object> bddThens = (List<Object>) bddThen;
                            bddThens.forEach( i -> addThenAResponseIs( glueWrapper, i.toString(), operation.getOperationId()));
                        }
                        else if( bddThen instanceof String ){
                            addThenAResponseIs( glueWrapper, bddThen.toString(), apiResponse.getDescription() );
                        }
                    }
                }
            }
        }

        System.out.println( "Added " + wrapperList.size() + " wrappers");
    }

    private void addThenAResponseIs(GlueWrapper glueWrapper, String bddThen, String operationId) {
        StepDefinition stepDefinition = glueWrapper.definitionMap.get("^the response is (.*)$");
        if( stepDefinition != null ) {
            StepDefinitionWrapper stepWrapper = new StepDefinitionWrapper(stepDefinition, "^" + bddThen + "$",
                    Lists.newArrayList( new StringArgument(operationId)));
            glueWrapper.glue.addStepDefinition(stepWrapper);
            wrapperList.add(stepWrapper);
        }
    }

    private void addWhenARequestToAnOperation(GlueWrapper glueWrapper, String bddWhen, String operationId) {
        StepDefinition stepDefinition = glueWrapper.definitionMap.get("^a request to ([^ ]*) is made$");
        if( stepDefinition != null ) {
            StepDefinitionWrapper stepWrapper = new StepDefinitionWrapper(stepDefinition, "^" + bddWhen + "$",
                    Lists.newArrayList( new StringArgument(operationId)));
            glueWrapper.glue.addStepDefinition(stepWrapper);
            wrapperList.add(stepWrapper);
        }
    }

    @Override
    public void buildWorld() {
        javaBackend.buildWorld();
    }

    @Override
    public void disposeWorld() {
        javaBackend.disposeWorld();
    }

    @Override
    public List<String> getSnippet(PickleStep pickleStep, String s, FunctionNameGenerator functionNameGenerator) {
        return javaBackend.getSnippet( pickleStep, s, functionNameGenerator);
    }

    private class GlueWrapper implements Glue {
        private Glue glue;
        private Map<String,StepDefinition> definitionMap = Maps.newHashMap();

        public GlueWrapper(Glue glue) {
            this.glue = glue;
        }

        @Override
        public void addStepDefinition(StepDefinition stepDefinition) throws DuplicateStepDefinitionException {
            definitionMap.put( stepDefinition.getPattern(), stepDefinition );
            glue.addStepDefinition(stepDefinition);
        }

        @Override
        public void addBeforeHook(HookDefinition hookDefinition) {
            glue.addBeforeHook(hookDefinition);
        }

        @Override
        public void addAfterHook(HookDefinition hookDefinition) {
            glue.addAfterHook(hookDefinition);
        }

        @Override
        public void addBeforeStepHook(HookDefinition hookDefinition) {
            glue.addBeforeStepHook(hookDefinition);
        }

        @Override
        public void addAfterStepHook(HookDefinition hookDefinition) {
            glue.addAfterStepHook(hookDefinition);
        }

        @Override
        public void removeScenarioScopedGlue() {
            glue.removeScenarioScopedGlue();
        }
    }

    private class StepDefinitionWrapper implements StepDefinition {

        private final StepDefinition stepDefinition;
        private final String pattern;
        private List<Argument> arguments;

        StepDefinitionWrapper(StepDefinition stepDefinition, String pattern, List<Argument> arguments ){
            this.stepDefinition = stepDefinition;
            this.pattern = pattern;
            this.arguments = arguments;
        }

        @Override
        public List<Argument> matchedArguments(PickleStep pickleStep) {
            return pickleStep.getText().matches( pattern ) ? arguments : null;
        }

        @Override
        public String getLocation(boolean b) {
            return stepDefinition.getLocation(b);
        }

        @Override
        public Integer getParameterCount() {
            return stepDefinition.getParameterCount();
        }

        @Override
        public void execute(Object[] objects) throws Throwable {
            stepDefinition.execute(objects);
        }

        @Override
        public boolean isDefinedAt(StackTraceElement stackTraceElement) {
            return stepDefinition.isDefinedAt(stackTraceElement);
        }

        @Override
        public String getPattern() {
            return pattern;
        }

        @Override
        @Deprecated
        public boolean isScenarioScoped() {
            return stepDefinition.isScenarioScoped();
        }
    }

    private class StringArgument implements Argument {

        private final String value;

        StringArgument(String string ){
            this.value = string;
        }

        @Override
        public Object getValue() {
            return value;
        }
    }
}
