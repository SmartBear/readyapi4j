package cucumber.runtime.oas;

import com.google.common.collect.Lists;
import com.smartbear.readyapi4j.cucumber.CucumberUtils;
import com.smartbear.readyapi4j.cucumber.RestStepDefs;
import cucumber.api.java.ObjectFactory;
import cucumber.runtime.*;
import cucumber.runtime.io.ResourceLoader;
import cucumber.runtime.java.JavaBackend;
import cucumber.runtime.snippets.FunctionNameGenerator;
import gherkin.pickles.PickleStep;
import io.cucumber.stepexpression.Argument;
import io.cucumber.stepexpression.TypeRegistry;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Custom Backend that reads OAS BDD extensions and translates them to the readyapi4j OAS StepDefs
 */

public class OASBackend implements Backend {

    private JavaBackend javaBackend;
    private List<StepDefinitionWrapper> wrapperList = Lists.newArrayList();
    private OASWrapper oasWrapper;

    public OASBackend(ResourceLoader resourceLoader, TypeRegistry typeRegistry) {
        javaBackend = new JavaBackend(resourceLoader, typeRegistry);
    }

    @Override
    public void loadGlue(Glue glue, List<URI> list) {
        ArrayList<URI> glueList = Lists.newArrayList(list);

        try {
            glueList.add( new URI("classpath:com/smartbear/readyapi4j/cucumber" ));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        GlueWrapper glueWrapper = new GlueWrapper(glue);
        javaBackend.loadGlue(glueWrapper, glueList);
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

    /**
     * Glue wrapper so we can wrap step definitions for runtime interception
     */

    private class GlueWrapper implements Glue {
        private Glue glue;

        public GlueWrapper(Glue glue) {
            this.glue = glue;
        }

        @Override
        public void addStepDefinition(StepDefinition stepDefinition) throws DuplicateStepDefinitionException {
            glue.addStepDefinition(new StepDefinitionWrapper(stepDefinition));
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

    /**
     * StepDefinition Wrapper that allows us to intercept custom bdd statements defined in OAS extensions and translate
     * them to defined StepDefs
     */

    private class StepDefinitionWrapper implements StepDefinition {

        private final StepDefinition stepDefinition;

        StepDefinitionWrapper( StepDefinition stepDefinition ){
            this.stepDefinition = stepDefinition;
        }

        @Override
        public List<Argument> matchedArguments(PickleStep pickleStep) {
            if( pickleStep.getText().startsWith( "the OAS definition at ")){
                try {
                    String oas = pickleStep.getText().substring("the OAS definition at ".length());
                    oas = CucumberUtils.stripQuotes( oas );
                    if( oasWrapper == null ) {
                        oasWrapper = new OASWrapper(oas);
                    }
                    else {
                        oasWrapper.loadDefinition( oas );
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if( stepDefinition.getPattern().equalsIgnoreCase("^a request to ([^ ]*) with parameters$")){
                if( oasWrapper != null ){
                    OASWrapper.WhenOperationWrapper operationWrapper = oasWrapper.getWhen( pickleStep.getText());
                    if( operationWrapper != null ){
                        return extractOperationArguments(operationWrapper);
                    }
                }
            }
            else if( stepDefinition.getPattern().equalsIgnoreCase("^the response is (.*)$")){
                if( oasWrapper != null ){
                    OASWrapper.ThenResponseWrapper responseWrapper = oasWrapper.getThen( pickleStep.getText());
                    if( responseWrapper != null ){
                        return Lists.newArrayList( new ApiResponseArgument(responseWrapper));
                    }
                }
            }

            return stepDefinition.matchedArguments( pickleStep );
        }

        private List<Argument> extractOperationArguments(OASWrapper.WhenOperationWrapper operation) {
            Map<String, String> parameters = operation.getParameters();
            if( parameters != null && !parameters.isEmpty()){
                StringBuilder builder = new StringBuilder();
                for( String name : parameters.keySet()){
                    builder.append(name).append('=').append(parameters.get(name)).append('\n');
                }
                return Lists.newArrayList(new StringArgument(operation.getOperation().getOperationId()),
                        new StringArgument(builder.toString()));
            }
            else {
                return Lists.newArrayList(new StringArgument(operation.getOperation().getOperationId()),
                        new StringArgument(""));
            }
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
            if( objects.length == 1 && objects[0] instanceof OASWrapper.ThenResponseWrapper) {
                OASWrapper.ThenResponseWrapper argument = (OASWrapper.ThenResponseWrapper) objects[0];
                stepDefinition.execute( new Object[]{ argument.getApiResponse().getDescription() });

                if( argument.getAssertions() != null ) {
                    try {
                        ObjectFactory objectFactory = (ObjectFactory) FieldUtils.readField(stepDefinition, "objectFactory", true);
                        RestStepDefs stepDefs = objectFactory.getInstance(RestStepDefs.class);

                        addAssertions( argument.getAssertions(), stepDefs );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            else {
                stepDefinition.execute(objects);
            }
        }

        @Override
        public boolean isDefinedAt(StackTraceElement stackTraceElement) {
            return stepDefinition.isDefinedAt(stackTraceElement);
        }

        @Override
        public String getPattern() {
            return stepDefinition.getPattern();
        }

        @Override
        @Deprecated
        public boolean isScenarioScoped() {
            return stepDefinition.isScenarioScoped();
        }
    }

    private void addAssertions(List<Map<String, Object>> assertions, RestStepDefs stepDefs) {
        assertions.forEach( assertion -> {
            String type = (String) assertion.get("type");
            if( "json".equalsIgnoreCase( type )){
                extractJsonPathAssertion(stepDefs, assertion);
            }
            else if( "header".equalsIgnoreCase( type )){
                extractHeaderAssertion(stepDefs, assertion);
            }
            else if( "contains".equalsIgnoreCase( type )){
                extractContainsAssertion(stepDefs, assertion);
            }
            else if( "xpath".equalsIgnoreCase( type )){
                extractXPathAssertion(stepDefs, assertion);
            }
        });
    }

    private void extractContainsAssertion(RestStepDefs stepDefs, Map<String, Object> assertion) {
        String value = (String) assertion.get( "content");
        if( value != null ){
            stepDefs.theResponseBodyContains( value );
        }
        else {
            String regex = (String) assertion.get("regex");
            if (regex != null) {
                stepDefs.theResponseBodyMatches(regex);
            }
        }
    }

    private void extractHeaderAssertion(RestStepDefs stepDefs, Map<String, Object> assertion) {
        String name = (String) assertion.get( "name");
        if( name != null ){
            String value = (String) assertion.get( "value");
            if( value != null ){
                stepDefs.theResponseHeaderIs( name, value );
            }
            else {
                String regex = (String) assertion.get("regex");
                if (regex != null) {
                    stepDefs.theResponseHeaderMatches(name, regex);
                }
                else {
                    stepDefs.theResponseContainsHeader(name);
                }
            }
        }
    }

    private void extractJsonPathAssertion(RestStepDefs stepDefs, Map<String, Object> assertion) {
        String path = (String) assertion.get( "path");
        if( path != null ){
            String value = assertion.containsKey( "value") ? String.valueOf( assertion.get( "value")) : null;
            String regex = assertion.containsKey( "regex") ? String.valueOf( assertion.get( "regex")) : null;
            String count = String.valueOf(assertion.get( "count"));
            if( value != null ) {
                stepDefs.thePathEquals(path, value);
            }
            else if( regex != null ){
                stepDefs.thePathMatches( path, regex );
            }
            else if( !count.equals("null") ){
                stepDefs.thePathFinds( path, count );
            }
            else {
                stepDefs.thePathExists( path );
            }
        }
    }

    private void extractXPathAssertion(RestStepDefs stepDefs, Map<String, Object> assertion) {
        String path = (String) assertion.get( "path");
        String value = (String) assertion.get( "value");
        if( path != null && value != null ) {
           stepDefs.theXPathMatches(path, value);
        }
    }

    private class StringArgument implements Argument {

        private final String value;

        StringArgument(String string ){
            this.value = string;
        }

        @Override
        public String getValue() {
            return value;
        }
    }

    private class ApiResponseArgument implements Argument {

        private final OASWrapper.ThenResponseWrapper value;

        ApiResponseArgument(OASWrapper.ThenResponseWrapper apiResponse ){
            this.value = apiResponse;
        }

        @Override
        public OASWrapper.ThenResponseWrapper getValue() {
            return value;
        }
    }
}
