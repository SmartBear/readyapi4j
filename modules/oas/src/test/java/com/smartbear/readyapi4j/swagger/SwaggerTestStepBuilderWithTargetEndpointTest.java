package com.smartbear.readyapi4j.swagger;

import com.smartbear.readyapi4j.teststeps.TestSteps;
import com.smartbear.readyapi4j.teststeps.restrequest.RestRequestStepBuilder;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SwaggerTestStepBuilderWithTargetEndpointTest {

    public static final String TARGET_ENDPOINT = "http://api.myhost.com";
    private static SwaggerTestStepBuilder petstore;

    @BeforeClass
    public static void setup() {
        petstore = new SwaggerTestStepBuilder(
                "src/test/resources/petstore-swagger.json", TARGET_ENDPOINT);
    }

    @Test
    public void testExistingOperation() throws Exception {
        RestRequestStepBuilder<? extends RestRequestStepBuilder> builder = petstore.operation("addPet");

        assertEquals(builder.build().getMethod(), "POST");
        assertEquals(builder.build().getURI(), TARGET_ENDPOINT + "/v2/pet");
    }

    @Test
    public void testRequest() throws Exception {
        RestRequestStepBuilder<? extends RestRequestStepBuilder> builder = petstore.request("/some/endpoint", TestSteps.HttpMethod.GET);

        assertEquals(builder.build().getMethod(), "GET");
        assertEquals(builder.build().getURI(), TARGET_ENDPOINT + "/v2/some/endpoint");
    }
}