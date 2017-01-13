package com.smartbear.readyapi4j.testserver.execution;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.smartbear.readyapi.client.model.TestCase;
import com.smartbear.readyapi4j.RecipeFilter;
import com.smartbear.readyapi4j.TestRecipe;
import com.smartbear.readyapi4j.execution.Execution;
import com.smartbear.readyapi4j.execution.RecipeExecutor;
import com.smartbear.readyapi4j.extractor.ExtractorData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Responsible for executing test recipes and projects on a Ready! API Server, synchronously or asynchronously.
 */
public class TestServerRecipeExecutor extends AbstractTestServerExecutor implements RecipeExecutor {
    private static Logger logger = LoggerFactory.getLogger(TestServerRecipeExecutor.class);

    private final List<RecipeFilter> recipeFilters = new CopyOnWriteArrayList<>();

    private ObjectMapper objectMapper;

    TestServerRecipeExecutor(TestServerClient testServerClient) {
        super(testServerClient);
    }

    public void addRecipeFilter(RecipeFilter recipeFilter) {
        recipeFilters.add(recipeFilter);
    }

    public void removeRecipeFilter(RecipeFilter recipeFilter) {
        recipeFilters.remove(recipeFilter);
    }

    @Override
    public Execution submitRecipe(String jsonText) {
        return submitJsonText(jsonText, true);
    }

    @Override
    public Execution executeRecipe(String jsonText) {
        return submitJsonText(jsonText, false);
    }

    private Execution submitJsonText(String jsonText, boolean async) throws ApiException {
        try {
            ObjectMapper objectMapper = getObjectMapper();
            TestCase testCase = objectMapper.readValue(jsonText, TestCase.class);
            return doExecuteTestCase(testCase, null, async);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ApiException(400, e.getMessage());
        }
    }

    private ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
            StdTypeResolverBuilder typeResolverBuilder = new ObjectMapper.DefaultTypeResolverBuilder(ObjectMapper.DefaultTyping.OBJECT_AND_NON_CONCRETE);
            typeResolverBuilder.typeProperty("type");
            typeResolverBuilder.inclusion(JsonTypeInfo.As.EXISTING_PROPERTY);
            typeResolverBuilder.init(JsonTypeInfo.Id.CUSTOM, new TestStepTypeResolver());
//            objectMapper.registerModule(new TestStepDeserializerModule());
            /*objectMapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector() {
                private static final long serialVersionUID = 1L;

                @SuppressWarnings("unchecked")
                @Override
                protected <A extends Annotation> A _findAnnotation(Annotated annotated, Class<A> annoClass) {
                    A annotation = super._findAnnotation(annotated, annoClass);
                    if (annotated.getAnnotated() == null || !TestStep.class.equals(annotated.getType().getRawClass())) {
                        System.out.println(annotated.getAnnotated() + " " + TestStep.class.getName() + " " + annotated.getType().getRawClass());
                        System.out.println(annotated.getAnnotated());
                        return annotation;
                    }
                    if (JsonTypeIdResolver.class.equals(annoClass)) {
                        return (A) new JsonTypeIdResolver() {
                            @Override
                            public Class<? extends Annotation> annotationType() {
                                return JsonTypeIdResolver.class;
                            }

                            @Override
                            public Class<? extends TypeIdResolver> value() {
                                return TestStepTypeResolver.class;
                            }
                        };
                    } else if (JsonTypeInfo.class.equals(annoClass) && annotation == null) {
                        return (A) new JsonTypeInfo() {

                            @Override
                            public Class<? extends Annotation> annotationType() {
                                return null;
                            }

                            @Override
                            public Id use() {
                                return JsonTypeInfo.Id.CUSTOM;
                            }

                            @Override
                            public As include() {
                                return JsonTypeInfo.As.EXISTING_PROPERTY;
                            }

                            @Override
                            public String property() {
                                return "type";
                            }

                            @Override
                            public Class<?> defaultImpl() {
                                return JsonTypeInfo.class;
                            }

                            @Override
                            public boolean visible() {
                                return true;
                            }
                        };
                    }
                    return annotation;
                }
            });*/
        }
        return objectMapper;
    }

    @Override
    public TestServerExecution submitRecipe(TestRecipe recipe) throws ApiException {
        for (RecipeFilter recipeFilter : recipeFilters) {
            recipeFilter.filterRecipe(recipe);
        }

        TestServerExecution execution = doExecuteTestCase(recipe.getTestCase(), recipe.getExtractorData(), true);
        notifyExecutionStarted(execution);
        return execution;
    }

    @Override
    public TestServerExecution executeRecipe(TestRecipe recipe) throws ApiException {
        TestServerExecution execution = doExecuteTestCase(recipe.getTestCase(), recipe.getExtractorData(), false);
        if (execution != null) {
            notifyExecutionFinished(execution.getCurrentReport());
        }
        return execution;
    }

    private TestServerExecution doExecuteTestCase(TestCase testCase, ExtractorData optionalExtractorData, boolean async) throws ApiException {
        try {
            Optional<ExtractorData> extractorDataOptional = Optional.ofNullable(optionalExtractorData);
            extractorDataOptional.ifPresent(extractorData -> extractorDataList.add(extractorData));
            TestServerExecution execution = testServerClient.postTestRecipe(testCase, async);
            cancelExecutionAndThrowExceptionIfPendingDueToMissingClientCertificate(execution.getCurrentReport(), testCase);
            return execution;
        } catch (ApiException e) {
            notifyErrorOccurred(e);
            logger.debug("An error occurred when sending test recipe to server. Details: " + e.toString());
            throw e;
        } catch (Exception e) {
            notifyErrorOccurred(e);
            logger.debug("An error occurred when sending test recipe to server", e);
            throw new ApiException(e);
        }
    }

    @Override
    public List<Execution> getExecutions() throws ApiException {
        return testServerClient.getExecutions();
    }
}
